package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.features.PID;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

@Config
@TeleOp(group = "test")
public class PIDTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private PID pid;
    private DcMotor motor;

    public static double P = 0.05;
    public static double I = 0.1;
    public static double D = 0.02;
    public static int TARGET_POSITION = 0;
    public static double MINMAX_LIMIT = 0.5;

    private static int previousTargetPosition = 0;

    public void clock() {
        TARGET_POSITION += 30;
        Schedule.addTask(this::clock, 1.0);
    }

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);
        pid = new PID(P, I, D);
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotor.Direction.FORWARD);
        //motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void start() {
        Schedule.addTask(this::clock, 1.0);
    }

    @Override
    public void loop() {
        pid.updatePID(P, I, D);

        double currentPosition = motor.getCurrentPosition();
        double error = TARGET_POSITION - currentPosition;
        double power = pid.update(error, -MINMAX_LIMIT, MINMAX_LIMIT);
        motor.setPower(power);

        TelemetrySystem.addClassData("PIDTest","Target Position", TARGET_POSITION);
        TelemetrySystem.addClassData("PIDTest","Current Position", currentPosition);
        TelemetrySystem.addClassData("PIDTest","Error", error);
        TelemetrySystem.addClassData("PIDTest","Integral", pid.getIntegral());

        if (previousTargetPosition != TARGET_POSITION) {
            pid.resetIntegral();
            previousTargetPosition = TARGET_POSITION;
        }

        Schedule.update();
        SmartServo.updateAll();
        TelemetrySystem.update();
    }

    @Override
    public void stop() {

    }
}
