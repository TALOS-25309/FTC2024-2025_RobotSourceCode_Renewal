package org.firstinspires.ftc.teamcode.opmode.test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.features.SmartGamepad;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.drive.Drive;

@Config
@TeleOp(group = "test")
public class DriveTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private Drive drivePart;
    private SmartGamepad gamepad;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);
        drivePart = new Drive();
        drivePart.init(hardwareMap);
        gamepad = new SmartGamepad(gamepad1);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        drivePart.command().drive(
            gamepad.triggerLeftStickX().getValue(),
            gamepad.triggerLeftStickY().getValue(),
            gamepad.triggerRightTrigger().getValue() - gamepad.triggerLeftTrigger().getValue()
        );

        gamepad.update();
        drivePart.update();
    }

    @Override
    public void stop() {
        drivePart.stop();
    }
}
