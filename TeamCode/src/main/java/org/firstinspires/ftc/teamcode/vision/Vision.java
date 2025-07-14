package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

import java.util.List;

public class Vision {
    private final Limelight3A limelight;
    private int repetition = VisionConstants.REPETITION;
    private double previousTimestamp = 0.0;
    private double detectionCnt = 0.0;

    public enum State {
        READY, REQUESTED, DETECTED, FAILED
    }

    private enum InnerState {
        WAITING_FOR_DETECTION,
        WAITING_FOR_OBTAINING_ORIENTATION,
        WAITING_FOR_CAPTURING,
        WAITING_FOR_CHECKING_DIFFERENCE,
        COMPLETED
    }

    private State state = State.READY;
    private InnerState innerState = InnerState.COMPLETED;
    private Sample sample = null;
    private boolean isDifferent = false;

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, VisionConstants.LIMELIGHT_NAME);
        limelight.setPollRateHz(VisionConstants.POLL_RATE_HZ);
        limelight.start();
    }

    public void update() {
        TelemetrySystem.addClassData("Vision", "state", state.toString());
        if (state == State.REQUESTED) {
            LLStatus status = limelight.getStatus();

            TelemetrySystem.addClassData("Vision", "name", status.getName());
            TelemetrySystem.addClassData("Vision", "temp", status.getTemp());
            TelemetrySystem.addClassData("Vision", "cpu", status.getCpu());
            TelemetrySystem.addClassData("Vision", "fps", (int) status.getFps());

            LLResult result = limelight.getLatestResult();

            if (result == null) return;

            if (innerState == InnerState.WAITING_FOR_DETECTION) {
                if (!result.isValid()) return;
                if (result.getPipelineIndex() == VisionConstants.DETECTION_PIPELINE_ID) {
                    if (result.getTimestamp() != previousTimestamp) {
                        previousTimestamp = result.getTimestamp();
                        repetition--;

                        Sample s = detectTarget(result);

                        if (s.state == Sample.State.DETECTED) {
                            sample.state = Sample.State.DETECTED;
                            sample.x += s.x;
                            sample.y += s.y;
                            sample.x_angle += s.x_angle;
                            sample.y_angle += s.y_angle;
                            if (sample.corners == null || sample.corners.isEmpty()) {
                                sample.corners = s.corners;
                            } else {
                                for (int i = 0; i < sample.corners.size(); i++) {
                                    List<Double> sampleCorner = sample.corners.get(i);
                                    List<Double> sCorner = s.corners.get(i);
                                    for (int j = 0; j < sampleCorner.size(); j++) {
                                        sampleCorner.set(j, sampleCorner.get(j) + sCorner.get(j));
                                    }
                                }
                            }
                            detectionCnt++;
                        }

                        if(repetition <= 0) {
                            if (sample.state == Sample.State.DETECTED) {
                                sample.x /= detectionCnt;
                                sample.y /= detectionCnt;
                                sample.x_angle /= detectionCnt;
                                sample.y_angle /= detectionCnt;
                                for (int i = 0; i < sample.corners.size(); i++) {
                                    List<Double> sampleCorner = sample.corners.get(i);
                                    for (int j = 0; j < sampleCorner.size(); j++) {
                                        sampleCorner.set(j, sampleCorner.get(j) / detectionCnt);
                                    }
                                }

                                innerState = InnerState.WAITING_FOR_OBTAINING_ORIENTATION;
                                setInputForObtainingOrientation();
                                limelight.pipelineSwitch(VisionConstants.ORIENTATION_PIPELINE_ID);
                            } else {
                                state = State.FAILED;
                                innerState = InnerState.COMPLETED;
                            }
                        }
                    }
                }
            } else if (innerState == InnerState.WAITING_FOR_OBTAINING_ORIENTATION) {
                if (result.getPipelineIndex() == VisionConstants.ORIENTATION_PIPELINE_ID) {
                    Sample s = obtainOrientation(result);
                    sample.state = s.state;
                    sample.angle = s.angle;
                    if (sample.state == Sample.State.DETECTED) {
                        state = State.DETECTED;
                    } else {
                        state = State.FAILED;
                    }
                    innerState = InnerState.COMPLETED;
                }
            } else if (innerState == InnerState.WAITING_FOR_CAPTURING) {
                if(!VisionConstants.VISION_BASES_SAMPLE_PICKUP_CHECK)
                    throw new UnsupportedOperationException("This feature is no longer supported");
                if (result.getPipelineIndex() == VisionConstants.DIFFERENCE_PIPELINE_ID
                    && result.getPythonOutput()[0] == VisionConstants.CAPTURE_CODE) {
                    innerState = InnerState.COMPLETED;
                    state = State.READY;
                }
            } else if (innerState == InnerState.WAITING_FOR_CHECKING_DIFFERENCE) {
                if(!VisionConstants.VISION_BASES_SAMPLE_PICKUP_CHECK)
                    throw new UnsupportedOperationException("This feature is no longer supported");
                if (result.getPipelineIndex() == VisionConstants.DIFFERENCE_PIPELINE_ID
                    && result.getPythonOutput()[0] == VisionConstants.DIFFERENCE_CHECKING_CODE) {
                    innerState = InnerState.COMPLETED;
                    state = State.READY;
                    TelemetrySystem.addClassData("Vision", "diff", result.getPythonOutput()[1]);
                    isDifferent = result.getPythonOutput()[1] > VisionConstants.DIFF_THRESHOLD;
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
            repetition = VisionConstants.REPETITION;
            detectionCnt = 0.0;
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

    private Sample detectTarget(LLResult result) {
        String label = sample.getLabel();

        Sample sample = new Sample();
        sample.color = this.sample.color;

        List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
        double meanConfidence = 0.0;
        double minAbsXDegree = 180;
        for (LLResultTypes.DetectorResult target : detectorResults) {
            String targetLabel = target.getClassName();
            if (targetLabel.equals(label)) {
                meanConfidence += target.getConfidence();
            }
        }
        meanConfidence /= detectorResults.size();
        for (LLResultTypes.DetectorResult target : detectorResults) {
            String targetLabel = target.getClassName();
            if (targetLabel.equals(label)) {
                if (target.getConfidence() >= meanConfidence
                    && Math.abs(target.getTargetXDegrees()) < minAbsXDegree) {
                    minAbsXDegree = Math.abs(target.getTargetXDegrees());
                    sample.x_angle = Math.toRadians(target.getTargetXDegrees());
                    sample.y_angle = Math.toRadians(target.getTargetYDegrees());
                    double h = VisionConstants.LIMELIGHT_HEIGHT;

                    double phi_x = sample.x_angle;
                    double phi_y = Math.toRadians(90)
                            - VisionConstants.LIMELIGHT_ANGLE - sample.y_angle;

                    sample.x = h / Math.tan(phi_y) * Math.sin(phi_x);
                    sample.y = h / Math.tan(phi_y) * Math.cos(phi_x);
                    sample.state = Sample.State.DETECTED;
                    sample.corners = target.getTargetCorners();

                    sample.x += VisionConstants.LIMELIGHT_X_DELTA;
                    sample.y += VisionConstants.LIMELIGHT_Y_DELTA;
                }
            }
        }
        return sample;
    }

    private void setInputForObtainingOrientation() {
        double buffer = VisionConstants.AREA_MARGIN;
        double x = (sample.corners.get(0).get(0));
        double y = (sample.corners.get(0).get(1));
        double w = (sample.corners.get(2).get(0) - x);
        double h = (sample.corners.get(2).get(1) - y);

        x -= VisionConstants.SHIFT;
        y -= VisionConstants.SHIFT;

        x -= buffer;
        y -= buffer;
        w += buffer * 2;
        h += buffer * 2;

        //*
        TelemetrySystem.addClassData("Vision", "x", x);
        TelemetrySystem.addClassData("Vision", "y", y);
        TelemetrySystem.addClassData("Vision", "w", w);
        TelemetrySystem.addClassData("Vision", "h", h);
        //*/
        double[] inputs = {
                sample.getColorIndex(),
                x, y, w, h,
                0,
                VisionConstants.LIMELIGHT_ANGLE,
                VisionConstants.LIMELIGHT_HEIGHT
        };
        limelight.updatePythonInputs(inputs);
    }

    private Sample obtainOrientation(LLResult result) {
        Sample sample = new Sample();

        double[] pythonOutputs = result.getPythonOutput();
        if (pythonOutputs != null && pythonOutputs.length > 0) {
            double flag = pythonOutputs[0];
            if (flag == 1.0) {
                sample.state = Sample.State.DETECTED;
                sample.angle = pythonOutputs[1];

                while (sample.angle <= -90.0) sample.angle += 180.0;
                while (sample.angle > 90.0) sample.angle -= 180.0;

                /*
                sample.angle += VisionConstants.AMAZING_CONSTANT
                        * Math.toDegrees(sample.x_angle) * Math.toDegrees(sample.y_angle);
                 */
                //*
                //*/
            } else {
                TelemetrySystem.addDebugData("FLAG", flag);
                sample.state = Sample.State.FAILED;
            }
            TelemetrySystem.addClassData("Vision", "contour_x", pythonOutputs[2]);
            TelemetrySystem.addClassData("Vision", "contour_y", pythonOutputs[3]);
            TelemetrySystem.addClassData("Vision", "contour_w", pythonOutputs[4]);
            TelemetrySystem.addClassData("Vision", "contour_h", pythonOutputs[5]);

        }
        return sample;
    }

    @Deprecated
    public void capture() {
        if(!VisionConstants.VISION_BASES_SAMPLE_PICKUP_CHECK)
            throw new UnsupportedOperationException("This feature is no longer supported");

        if (state == State.READY) {
            state = State.REQUESTED;
            innerState = InnerState.WAITING_FOR_CAPTURING;
            limelight.pipelineSwitch(VisionConstants.DIFFERENCE_PIPELINE_ID);
            double[] inputs = {VisionConstants.CAPTURE_CODE, 0,0,0,0,0,0,0};
            limelight.updatePythonInputs(inputs);
        }
    }

    @Deprecated
    public void checkDifference() {
        if(!VisionConstants.VISION_BASES_SAMPLE_PICKUP_CHECK)
            throw new UnsupportedOperationException("This feature is no longer supported");

        if (limelight.getStatus().getPipelineIndex() != VisionConstants.DIFFERENCE_PIPELINE_ID) {
            return;
        }
        if (state == State.READY) {
            state = State.REQUESTED;
            innerState = InnerState.WAITING_FOR_CHECKING_DIFFERENCE;
            double[] inputs = {VisionConstants.DIFFERENCE_CHECKING_CODE, 0,0,0,0,0,0,0};
            limelight.updatePythonInputs(inputs);
        }
    }

    @Deprecated
    public boolean isDifferent() {
        if(!VisionConstants.VISION_BASES_SAMPLE_PICKUP_CHECK)
            throw new UnsupportedOperationException("This feature is no longer supported");

        return isDifferent;
    }
}
