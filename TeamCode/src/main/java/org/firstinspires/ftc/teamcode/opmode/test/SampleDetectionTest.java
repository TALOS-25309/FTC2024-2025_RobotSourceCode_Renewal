package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.vision.Sample;
import org.firstinspires.ftc.teamcode.vision.Vision;

@Config
@TeleOp(group = "test")
public class SampleDetectionTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private final Intake intake = new Intake();
    Vision vision;
    Sample sample = new Sample();

    public static Sample.SampleColor SAMPLE_COLOR = Sample.SampleColor.YELLOW;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        vision = new Vision(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    public void start() {
        workflow();
    }

    public void workflow() {
        intake.command().ready();
        Schedule.addTask(() -> {
            detect();
        }, 2.5);
        Schedule.addTask(() -> {
            intake.command().pickup();
        }, 5);
        Schedule.addTask(() -> {
            intake.command().transfer();
        }, 7.5);
        Schedule.addTask(this::workflow,10);
    }

    public void detect() {
        if(sample.state() == Sample.State.DETECTED) {
            intake.command().movePositionXY(sample.getX(), sample.getY());
            intake.command().rotateOrientation(sample.getAngle());
        }
    }

    @Override
    public void loop() {
        intake.update();
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

        Schedule.update();
        SmartServo.updateAll();
        SmartMotor.updateAll();
        TelemetrySystem.update();
    }
}
