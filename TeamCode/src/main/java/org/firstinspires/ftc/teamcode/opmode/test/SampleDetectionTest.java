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
    Sample sample = new Sample();

    public static Sample.SampleColor SAMPLE_COLOR = Sample.SampleColor.YELLOW;

    @Override
    public void init() {
        SmartMotor.init();
        SmartServo.init();
        Schedule.init();

        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        intake.init(hardwareMap);
    }

    @Override
    public void start() {
        workflow();
    }

    public void workflow() {
        intake.command().ready();
        Schedule.addTask(() -> {
            intake.command().automaticTargetForYellowSample();
        }, 4.0);
        Schedule.addTask(() -> {
            intake.command().pickup();
        }, 8.0);
        Schedule.addTask(() -> {
            intake.command().readyForTransfer();
        }, 9.0);
        Schedule.addTask(this::workflow,10);
    }

    @Override
    public void loop() {
        intake.update();

        Schedule.update();
        SmartServo.updateAll();
        SmartMotor.updateAll();
        TelemetrySystem.update();
    }
}
