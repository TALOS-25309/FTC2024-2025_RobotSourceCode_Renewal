package org.firstinspires.ftc.teamcode.part.intake;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.global.Memory;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.vision.Vision;

public class Intake implements Part {
    Commands commandProcessor;
    Adjustment adjustmentProcessor;
    IntakeState state = IntakeState.READY_FOR_PICKUP;

    SmartServo clawServo;
    SmartServo wristOrientationServo;
    SmartServo wristUpDownServo;
    SmartServo armUpDownServo;
    SmartServo turretServo;

    DcMotor light;

    SmartMotor linearSlideMotor;

    AnalogInput clawAnalogInput;

    Vision vision;

    boolean lightState = false;
    boolean wristOrientationManualControl = false;

    double current_x = 0.0, current_y = 0.0, current_orientation = 0.0;

    @Override
    public void init(HardwareMap hardwareMap) {
        this.commandProcessor = new Commands(this);
        this.adjustmentProcessor = new Adjustment(this);

        // Initialize hardware components
        vision = new Vision(hardwareMap);
        clawServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.CLAW_SERVO_NAME),
                Constants.CLAW_SERVO_NAME
        );
        wristOrientationServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.WRIST_ORIENTATION_SERVO_NAME),
                Constants.WRIST_ORIENTATION_SERVO_NAME
        );
        wristUpDownServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.WRIST_UP_DOWN_SERVO_NAME),
                Constants.WRIST_UP_DOWN_SERVO_NAME
        );
        armUpDownServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_UP_DOWN_SERVO_NAME),
                Constants.ARM_UP_DOWN_SERVO_NAME
        );
        turretServo = new SmartServo(
                hardwareMap.get(Servo.class, Constants.ARM_ROTATION_SERVO_NAME),
                Constants.ARM_ROTATION_SERVO_NAME
        );
        linearSlideMotor = new SmartMotor(
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_MOTOR_NAME),
                hardwareMap.get(DcMotor.class, Constants.LINEAR_SLIDE_ENCODER_NAME),
                Constants.LINEAR_SLIDE_MOTOR_NAME
        );
        clawAnalogInput = hardwareMap.get(AnalogInput.class, Constants.CLAW_ANALOG_INPUT_NAME);
        light = hardwareMap.get(DcMotor.class, Constants.LED_NAME);

        // Set initial positions
        if (Memory.END_STATE == Memory.EndState.PRE_TRANSFER_SAMPLE) {
           clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
           wristUpDownServo.setPosition(Constants.WRIST_TRANSFER_POSITION_FOR_SAMPLE);
           armUpDownServo.setPosition(Constants.ARM_PRE_TRANSFER_POSITION);
           state = IntakeState.READY_FOR_TRANSFER;
        } else {
            clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
            wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
            armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        }
        wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
        if(Global.OPMODE == Global.OpMode.TELE) {
            turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        } else {
            turretServo.setPosition(Constants.TURRET_DROP_POSITION * 0.7 + Constants.TURRET_TRANSFER_POSITION * 0.3);
        }

            // Set motor properties
        linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        linearSlideMotor.setMotorDirection(DcMotor.Direction.REVERSE);
        linearSlideMotor.setEncoderDirection(DcMotor.Direction.FORWARD);
        linearSlideMotor.resetEncoder();
        linearSlideMotor.setPID(
                Constants.LINEAR_SLIDE_PID_P,
                Constants.LINEAR_SLIDE_PID_I,
                Constants.LINEAR_SLIDE_PID_D
        );
        linearSlideMotor.setMotorMaximumPower(Constants.LINEAR_SLIDE_MAX_POWER);
    }

    @Override
    public void start() {
        command().lightOn();
        linearSlideMotor.setPosition(0);
        turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
    }

    @Override
    public void update() {
        adjustmentProcessor.update();
        vision.update();

        /*
        if (linearSlideMotor.getCurrentPosition() < Constants.LINEAR_SLIDE_READY_POSITION_THRESHOLD) {
            linearSlideMotor.setPower(Constants.LINEAR_SLIDE_STABLE_POWER);
        }
        */
        TelemetrySystem.addClassData("Intake", "state", this.state.toString());
        // TelemetrySystem.addClassData("Intake", "X", this.current_x);
        // TelemetrySystem.addClassData("Intake", "Y", this.current_y);
        // TelemetrySystem.addClassData("Intake", "Ori", this.current_orientation);
    }

    @Override
    public void stop() {
        wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
        wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
        armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
    }

    public Commands command() {
        return this.commandProcessor;
    }

    public Adjustment adjustment() {
        return this.adjustmentProcessor;
    }

    public IntakeState state() {
        return state;
    }

    public boolean isLinearSlideInside() {
        return linearSlideMotor.getCurrentPosition() < Constants.LINEAR_SLIDE_READY_POSITION_THRESHOLD;
    }

    public boolean isLinearSlideStretchPerfectly() {
        return Math.abs(linearSlideMotor.getTargetPosition() - linearSlideMotor.getCurrentPosition())
                < Constants.LINEAR_SLIDE_READY_POSITION_THRESHOLD;
    }

    public void activateWristOrientationManualControl() {
        this.wristOrientationManualControl = true;
        this.command().lightOff();
    }
}
