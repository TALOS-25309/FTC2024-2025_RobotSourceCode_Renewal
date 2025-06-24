package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.vision.Sample;
import org.firstinspires.ftc.teamcode.vision.Vision;

@Config
@TeleOp(group = "test")
public class VisionTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    Vision vision;
    Sample sample = new Sample();

    public static Sample.SampleColor SAMPLE_COLOR = Sample.SampleColor.YELLOW;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);
        vision = new Vision(hardwareMap);
    }

    @Override
    public void loop() {
        vision.update();

        if (vision.currentState() == Vision.State.READY) {
            vision.request(SAMPLE_COLOR);
        } else if (vision.currentState() != Vision.State.REQUESTED) {
            Sample s = vision.getTargetData();
            if (s != null) {
                if (s.state() == Sample.State.DETECTED) {
                    sample = s;
                }
            }
        }

        TelemetrySystem.addClassData("VisionTest", "Sample Color", sample.color.toString());
        TelemetrySystem.addClassData("VisionTest", "Sample State", sample.state().toString());
        TelemetrySystem.addClassData("VisionTest", "Sample X", sample.getX());
        TelemetrySystem.addClassData("VisionTest", "Sample Y", sample.getY());
        TelemetrySystem.addClassData("VisionTest", "Sample Angle", sample.getAngle());

        TelemetrySystem.update();
    }
}
