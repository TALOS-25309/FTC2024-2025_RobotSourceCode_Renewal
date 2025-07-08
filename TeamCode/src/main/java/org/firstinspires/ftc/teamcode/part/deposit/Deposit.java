package org.firstinspires.ftc.teamcode.part.deposit;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.part.Part;

public class Deposit implements Part {
    Commands commandProcessor;
    Adjustment adjustmentProcessor;

    DepositState state = DepositState.REST;

    SmartServo clawServo;
    SmartServo armMainServo; // Left
    SmartServo armAuxServo; // Right

    SmartMotor linearSlideMainMotor; // Inner
    SmartMotor linearSlideAuxMotor; // Outer

    @Override
    public void init(HardwareMap hardwareMap) {
        this.commandProcessor = new Commands(this);
        this.adjustmentProcessor = new Adjustment(this);

        // Initialize hardware components
        clawServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.CLAW_SERVO_NAME),
                Constants.CLAW_SERVO_NAME
        );
        armMainServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_SERVO_LEFT_NAME),
                Constants.ARM_SERVO_LEFT_NAME
        );
        armAuxServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_SERVO_RIGHT_NAME),
                Constants.ARM_SERVO_RIGHT_NAME
        );
        linearSlideMainMotor = new SmartMotor(
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_MAIN_MOTOR_NAME),
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_ENCODER_NAME),
                Constants.LINEAR_SLIDE_MAIN_MOTOR_NAME
        );
        linearSlideAuxMotor = new SmartMotor(
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_AUX_MOTOR_NAME),
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_ENCODER_NAME),
                Constants.LINEAR_SLIDE_AUX_MOTOR_NAME
        );

        // Set initial positions and properties for servos
        clawServo.setDirection(Servo.Direction.FORWARD);
        clawServo.setPosition(Constants.CLAW_OPEN_POSITION);

        armMainServo.setDirection(Servo.Direction.FORWARD);
        armAuxServo.setDirection(Servo.Direction.FORWARD);
        armMainServo.setPosition(Constants.ARM_READY_POSITION);
        armAuxServo.setPosition(Constants.ARM_READY_POSITION);

        armAuxServo.synchronizeWith(Constants.ARM_SERVO_LEFT_NAME, Constants.ARM_POSITION_DIFFERENCE);

        // Set motor properties
        linearSlideMainMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        linearSlideMainMotor.setMotorDirection(DcMotor.Direction.REVERSE);
        linearSlideMainMotor.setEncoderDirection(DcMotor.Direction.REVERSE);
        linearSlideMainMotor.resetEncoder();
        linearSlideMainMotor.setPID(
                Constants.LINEAR_SLIDE_PID_P,
                Constants.LINEAR_SLIDE_PID_I,
                Constants.LINEAR_SLIDE_PID_D
        );
        linearSlideMainMotor.setMotorMaximumPower(Constants.LINEAR_SLIDE_MAX_POWER);
        linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        linearSlideMainMotor.activatePID();

        linearSlideAuxMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        linearSlideAuxMotor.setMotorDirection(DcMotor.Direction.FORWARD);
        linearSlideAuxMotor.synchronizeWith(Constants.LINEAR_SLIDE_MAIN_MOTOR_NAME);
    }

    @Override
    public void update() {
        adjustmentProcessor.update();
    }

    @Override
    public void stop() {
        linearSlideMainMotor.setPower(0);
        armMainServo.setPosition(Constants.ARM_READY_POSITION);
        clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
    }

    public Commands command() {
        return commandProcessor;
    }

    public Adjustment adjustment() {
        return adjustmentProcessor;
    }

    public DepositState state() {
        return state;
    }
}
