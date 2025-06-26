package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

@Config
@TeleOp(group = "test")
public class SmartMotorTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private SmartMotor motor;

    public static double P = 0.05;
    public static double I = 0.1;
    public static double D = 0.02;
    public static int TARGET_POSITION = 0;
    public static double MINMAX_LIMIT = 0.5;

    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        motor = new SmartMotor(hardwareMap.get(DcMotor.class, "motor"), "motor");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMotorDirection(DcMotor.Direction.FORWARD);
        motor.resetEncoder();
        motor.setPID(P, I, D);
        motor.setPosition(TARGET_POSITION);
        motor.setMotorMaximumPower(MINMAX_LIMIT);
    }

    @Override
    public void loop() {
        motor.setPID(P, I, D);
        motor.setPosition(TARGET_POSITION);
        motor.setMotorMaximumPower(MINMAX_LIMIT);

        Schedule.update();
        SmartMotor.updateAll();
        TelemetrySystem.update();
    }
}
