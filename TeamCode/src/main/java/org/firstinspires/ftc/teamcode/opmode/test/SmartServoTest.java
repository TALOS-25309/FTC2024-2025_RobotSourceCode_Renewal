package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.deposit.Constants;

@Config
@TeleOp(group = "test")
public class SmartServoTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private SmartServo servo1, servo2;

    SmartServo armMainServo; // Left
    SmartServo armAuxServo; // Right

    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);
        armMainServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_SERVO_LEFT_NAME),
                Constants.ARM_SERVO_LEFT_NAME
        );
        armAuxServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_SERVO_RIGHT_NAME),
                Constants.ARM_SERVO_RIGHT_NAME
        );
        armMainServo.setDirection(Servo.Direction.FORWARD);
        armAuxServo.setDirection(Servo.Direction.FORWARD);
        armAuxServo.synchronizeWith(Constants.ARM_SERVO_LEFT_NAME, Constants.ARM_POSITION_DIFFERENCE);

        func1();

    }

    public void func1() {
        armMainServo.setPosition(0.6);
        Schedule.addTask(this::func2, 1);
    }


    public void func2() {
        armMainServo.setPosition(0.4);
        Schedule.addTask(this::func1, 1);
    }

    @Override
    public void loop() {
        Schedule.update();
        SmartMotor.updateAll();
        TelemetrySystem.update();
    }
}
