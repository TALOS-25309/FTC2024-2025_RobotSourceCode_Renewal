package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

import java.util.List;

public class Vision {
    private final Limelight3A limelight;

    public enum State {
        READY, REQUESTED, DETECTED, FAILED
    }

    private enum InnerState {
        WAITING_FOR_DETECTION, WAITING_FOR_OBTAINING_ORIENTATION, COMPLETED
    }

    private State state = State.READY;
    private InnerState innerState = InnerState.COMPLETED;
    private Sample sample = null;

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, VisionConstants.LIMELIGHT_NAME);
        limelight.setPollRateHz(VisionConstants.POLL_RATE_HZ);
        limelight.start();
    }

    public void update() {
        LLStatus status = limelight.getStatus();

        TelemetrySystem.addClassData("Vision", "name", status.getName());
        TelemetrySystem.addClassData("Vision", "temp", status.getTemp());
        TelemetrySystem.addClassData("Vision", "cpu", status.getCpu());
        TelemetrySystem.addClassData("Vision", "fps", (int) status.getFps());

        if (state == State.REQUESTED) {
            if (innerState == InnerState.WAITING_FOR_DETECTION) {
                if (status.getPipelineIndex() == VisionConstants.DETECTION_PIPELINE_ID) {
                    detectTarget();
                    if (sample.state == Sample.State.DETECTED) {
                        innerState = InnerState.WAITING_FOR_OBTAINING_ORIENTATION;
                        limelight.pipelineSwitch(VisionConstants.ORIENTATION_PIPELINE_ID);
                        setInputForObtainingOrientation();
                    } else {
                        state = State.FAILED;
                        innerState = InnerState.COMPLETED;
                    }
                }
            } else if (innerState == InnerState.WAITING_FOR_OBTAINING_ORIENTATION) {
                if (status.getPipelineIndex() == VisionConstants.ORIENTATION_PIPELINE_ID) {
                    obtainOrientation();
                    if (sample.state == Sample.State.DETECTED) {
                        state = State.DETECTED;
                    } else {
                        state = State.FAILED;
                    }
                    innerState = InnerState.COMPLETED;
                }
            }
        }
    }

    public void request(Sample.SampleColor color) {
        if (state == State.READY) {
            sample = new Sample();
            sample.color = color;
            sample.state = Sample.State.FAILED;
            state = State.REQUESTED;
            innerState = InnerState.WAITING_FOR_DETECTION;
            limelight.pipelineSwitch(VisionConstants.DETECTION_PIPELINE_ID);
        }
    }

    public State currentState() {
        return state;
    }

    public Sample getTargetData() {
        if (state == State.READY || state == State.REQUESTED) {
            return null;
        } else {
            state = State.READY;
            return sample;
        }
    }

    private void detectTarget() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            String label = sample.getLabel();
            List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
            double maxConfidence = 0.0;
            for (LLResultTypes.DetectorResult target : detectorResults) {
                String targetLabel = target.getClassName();
                if (targetLabel.equals(label)) {
                    if (target.getConfidence() > maxConfidence) {
                        maxConfidence = target.getConfidence();
                        sample.x_angle = Math.toRadians(target.getTargetXDegrees());
                        sample.y_angle = Math.toRadians(target.getTargetYDegrees());
                        double h = VisionConstants.LIMELIGHT_HEIGHT;
                        double theta = VisionConstants.LIMELIGHT_ANGLE;
                        double phi_x = sample.x_angle;
                        double phi_y = sample.y_angle;
                        sample.x = h * Math.tan(phi_x) / Math.cos(theta + phi_y);
                        sample.y = h * Math.tan(theta + phi_y);
                        sample.state = Sample.State.DETECTED;
                        sample.corners = target.getTargetCorners();

                        sample.x += VisionConstants.LIMELIGHT_X_DELTA;
                        sample.y += VisionConstants.LIMELIGHT_Y_DELTA;
                    }
                }
            }
        }
    }

    private void setInputForObtainingOrientation() {
        double buffer = VisionConstants.AREA_MARGIN;
        double x = (sample.corners.get(0).get(0));
        double y = (sample.corners.get(0).get(1));
        double w = (sample.corners.get(2).get(0) - x);
        double h = (sample.corners.get(2).get(1) - y);

        x /= 2;
        y /= 2;
        w /= 2;
        h /= 2;

        x -= buffer;
        y -= buffer;
        w += buffer * 2;
        h += buffer * 2;

        /*
        TelemetrySystem.addClassData("Vision", "x", x);
        TelemetrySystem.addClassData("Vision", "y", y);
        TelemetrySystem.addClassData("Vision", "w", w);
        TelemetrySystem.addClassData("Vision", "h", h);
        //*/
        double[] inputs = {
                sample.getColorIndex(),
                x, y, w, h,
                sample.x_angle,
                sample.y_angle,
                VisionConstants.LIMELIGHT_ANGLE
        };
        limelight.updatePythonInputs(inputs);
    }

    private void obtainOrientation() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            double[] pythonOutputs = result.getPythonOutput();
            if (pythonOutputs != null && pythonOutputs.length > 0) {
                double flag = pythonOutputs[0];
                if (flag == 1.0) {
                    sample.state = Sample.State.DETECTED;
                    sample.angle = pythonOutputs[1];

                    sample.angle += VisionConstants.AMAZING_CONSTANT
                            * Math.toDegrees(sample.x_angle) * Math.toDegrees(sample.y_angle);
                    /*
                    TelemetrySystem.addClassData("Vision", "contour_x", pythonOutputs[2]);
                    TelemetrySystem.addClassData("Vision", "contour_y", pythonOutputs[3]);
                    TelemetrySystem.addClassData("Vision", "contour_w", pythonOutputs[4]);
                    TelemetrySystem.addClassData("Vision", "contour_h", pythonOutputs[5]);
                    //*/
                } else {
                    sample.state = Sample.State.FAILED;
                }
            }
        }
    }
}
