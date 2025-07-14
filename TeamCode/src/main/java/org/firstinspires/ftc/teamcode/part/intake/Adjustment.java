package org.firstinspires.ftc.teamcode.part.intake;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

@Config(value = "IntakeAdjustment")
public class Adjustment extends org.firstinspires.ftc.teamcode.part.Adjustment {
    private final Intake intake;
    public Adjustment(Intake intake) {
        this.intake = intake;
    }

    public static boolean PID_ACTIVATED = false;
    public static boolean SERVO_ACTIVATED = false;

    public enum ServoState {
        READY,
        PICKUP,
        TRANSFER,
        CLAW_OPEN,
        CLAW_CLOSE,
        CLAW_CLOSE_MAXIMUM,
        WRIST_ORIENTATION_LEFT,
        WRIST_ORIENTATION_RIGHT,
        TURRET_LEFT,
        TURRET_RIGHT,
        DROP,
        CAUTIOUS_PICKUP_READY
    }
    public static ServoState SERVO_STATE = ServoState.READY;
    public static double MOTOR_TARGET_POSITION_IN_CM = 0.0;

    @Override
    protected void setAdjustState() {
        PIDActivated = PID_ACTIVATED;
        ServoActivated = SERVO_ACTIVATED;
    }

    @Override
    protected void adjustServo() {
        intake.clawServo.servo().getController().pwmEnable();
        intake.armUpDownServo.servo().getController().pwmEnable();
        intake.wristUpDownServo.servo().getController().pwmEnable();
        intake.wristOrientationServo.servo().getController().pwmEnable();
        intake.turretServo.servo().getController().pwmEnable();

        switch (SERVO_STATE) {
            case READY:
                intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
                intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
                break;
            case PICKUP:
                intake.wristUpDownServo.setPosition(Constants.WRIST_PICKUP_POSITION());
                intake.armUpDownServo.setPosition(Constants.ARM_PICKUP_POSITION());
                break;
            case TRANSFER:
                intake.wristUpDownServo.setPosition(Constants.WRIST_TRANSFER_POSITION());
                intake.armUpDownServo.setPosition(Constants.ARM_TRANSFER_POSITION());
                intake.turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
                intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
                break;
            case DROP:
                intake.wristUpDownServo.setPosition(Constants.WRIST_DROP_POSITION);
                intake.turretServo.setPosition(Constants.TURRET_DROP_POSITION);
                intake.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
                break;
            case CLAW_OPEN:
                intake.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
                break;
            case CLAW_CLOSE:
                intake.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
                break;
            case CLAW_CLOSE_MAXIMUM:
                intake.clawServo.setPosition(Constants.CLAW_CLOSED_MAXIMUM_POSITION);
                break;
            case WRIST_ORIENTATION_LEFT:
                intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_LEFT_LIMIT);
                break;
            case WRIST_ORIENTATION_RIGHT:
                intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_RIGHT_LIMIT);
                break;
            case TURRET_LEFT:
                intake.turretServo.setPosition(Constants.TURRET_LEFT_LIMIT);
                break;
            case TURRET_RIGHT:
                intake.turretServo.setPosition(Constants.TURRET_RIGHT_LIMIT);
                break;
            case CAUTIOUS_PICKUP_READY:
                intake.armUpDownServo.setPosition(Constants.ARM_CAUTIOUS_PICKUP_READY_POSITION);
                break;
        }
    }

    @Override
    protected void adjustPID() {
        intake.linearSlideMotor.setPID(
                Constants.LINEAR_SLIDE_PID_P,
                Constants.LINEAR_SLIDE_PID_I,
                Constants.LINEAR_SLIDE_PID_D
        );

        TelemetrySystem.addClassData(
                "IntakeAdjustment",
                "Current Encoder Value",
                intake.linearSlideMotor.getCurrentPosition()
        );

        TelemetrySystem.addClassData(
                "IntakeAdjustment",
                "Target Value",
                intake.linearSlideMotor.getTargetPosition()
        );

        intake.linearSlideMotor.setMotorMaximumPower(Constants.LINEAR_SLIDE_MAX_POWER);
        intake.linearSlideMotor.activatePID();
        double fraction = Constants.LINEAR_SLIDE_RANGE / Constants.LINEAR_SLIDE_MAX_LENGTH;
        intake.linearSlideMotor.setPosition(MOTOR_TARGET_POSITION_IN_CM * fraction);
    }

    @Override
    protected void releaseServo() {
        intake.clawServo.servo().getController().pwmDisable();
        intake.armUpDownServo.servo().getController().pwmDisable();
        intake.wristUpDownServo.servo().getController().pwmDisable();
        intake.wristOrientationServo.servo().getController().pwmDisable();
        intake.turretServo.servo().getController().pwmDisable();
    }

    @Override
    protected void releasePID() {
        intake.linearSlideMotor.stop();
    }

    @Override
    protected void printEncoderValue() {
        TelemetrySystem.addClassData(
                "IntakeAdjustment",
                "Linear Slide Encoder Value",
                intake.linearSlideMotor.getCurrentPosition()
        );
        TelemetrySystem.addClassData(
                "IntakeAdjustment",
                "Servo Encoder Value",
                intake.clawAnalogInput.getVoltage()
        );

    }
}
