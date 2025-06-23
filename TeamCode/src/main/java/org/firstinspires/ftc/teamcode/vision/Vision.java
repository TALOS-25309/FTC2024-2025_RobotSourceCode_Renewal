package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.analysis.function.Constant;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

import java.util.List;

public class Vision {
    private final Limelight3A limelight;

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, VisionConstants.LIMELIGHT_NAME);
        limelight.pipelineSwitch(VisionConstants.PIPELINE_ID);
        limelight.setPollRateHz(VisionConstants.POLL_RATE_HZ);
        limelight.start();
    }

    public void update() {
        LLStatus status = limelight.getStatus();
        TelemetrySystem.addClassData("Vision", "name", status.getName());
        TelemetrySystem.addClassData("Vision", "temp", status.getTemp());
        TelemetrySystem.addClassData("Vision", "cpu", status.getCpu());
        TelemetrySystem.addClassData("Vision", "fps", (int) status.getFps());
        TelemetrySystem.addClassData("Vision", "pipelineIndex", status.getPipelineIndex());
        TelemetrySystem.addClassData("Vision", "pipelineType", status.getPipelineType());
    }

    public void getTargetData(Sample sample) {
        LLResult result = limelight.getLatestResult();
        sample.state = Sample.State.FAILED;
        if (result != null && result.isValid()) {
            String label = sample.getLabel();
            List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
            double maxConfidence = 0.0;
            for (LLResultTypes.DetectorResult target : detectorResults) {
                String targetLabel = target.getClassName();
                if (targetLabel.equals(label)) {
                    if (target.getConfidence() > maxConfidence) {
                        maxConfidence = target.getConfidence();
                        double x_angle = Math.toRadians(target.getTargetXDegrees());
                        double y_angle = Math.toRadians(target.getTargetYDegrees());
                        sample.x = VisionConstants.LIMELIGHT_HEIGHT * Math.tan(x_angle + VisionConstants.LIMELIGHT_ANGLE);
                        sample.y = VisionConstants.LIMELIGHT_HEIGHT * Math.tan(y_angle);
                        sample.state = Sample.State.DETECTED;
                    }
                }
            }
        } else {
            TelemetrySystem.addClassData("Vision", "Target Data", "No valid result");
        }
    }
}
