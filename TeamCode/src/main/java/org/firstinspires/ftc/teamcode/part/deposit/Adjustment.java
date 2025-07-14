package org.firstinspires.ftc.teamcode.part.deposit;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.features.TelemetrySystem;

@Config(value="DepositAdjustment")
public class Adjustment extends org.firstinspires.ftc.teamcode.part.Adjustment {
    private Deposit deposit;

    public Adjustment(Deposit deposit) {
        this.deposit = deposit;
    }

    public enum ServoState {
        READY,
        TRANSFER,
        SPECIMEN_PICKUP,
        BASKET_SCORING,
        SPECIMEN_FORWARD_SCORING,
        SPECIMEN_BACKWARD_SCORING,
        OPEN_CLAW,
        CLOSE_CLAW
    }

    public static State ADJUSTMENT_STATE = State.ADJUST_SERVO;
    public static ServoState SERVO_STATE = ServoState.READY;
    public static double MOTOR_TARGET_POSITION_IN_CM = 0.0;

    @Override
    protected void setAdjustState() {
        this.adjustState = ADJUSTMENT_STATE;
    }

    @Override
    protected void adjustServo() {
        deposit.linearSlideMainMotor.stop();
        switch (SERVO_STATE) {
            case READY:
                deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
                break;
            case TRANSFER:
                deposit.armMainServo.setPosition(Constants.ARM_TRANSFER_POSITION());
                break;
            case SPECIMEN_PICKUP:
                deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_PICKUP_POSITION);
                break;
            case BASKET_SCORING:
                deposit.armMainServo.setPosition(Constants.ARM_BASKET_SCORING_POSITION);
                break;
            case SPECIMEN_FORWARD_SCORING:
                deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
                break;
            case SPECIMEN_BACKWARD_SCORING:
                deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
                break;
            case OPEN_CLAW:
                deposit.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
                break;
            case CLOSE_CLAW:
                deposit.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION());
                break;
        }
    }

    @Override
    protected void adjustPID() {
        deposit.linearSlideMainMotor.setPID(
                Constants.LINEAR_SLIDE_PID_P,
                Constants.LINEAR_SLIDE_PID_I,
                Constants.LINEAR_SLIDE_PID_D
        );

        TelemetrySystem.addClassData(
                "DepositAdjustment",
                "Current Encoder Value",
                deposit.linearSlideMainMotor.getCurrentPosition()
        );

        TelemetrySystem.addClassData(
                "DepositAdjustment",
                "Target Value",
                deposit.linearSlideMainMotor.getTargetPosition()
        );

        deposit.linearSlideMainMotor.setMotorMaximumPower(Constants.LINEAR_SLIDE_MAX_POWER);
        deposit.linearSlideMainMotor.activatePID();
        double fraction = Constants.LINEAR_SLIDE_RANGE / Constants.LINEAR_SLIDE_MAX_LENGTH;
        deposit.linearSlideMainMotor.setPosition(MOTOR_TARGET_POSITION_IN_CM * fraction);
    }

    @Override
    protected void printEncoderValue() {
        deposit.linearSlideMainMotor.setPower(0);
        TelemetrySystem.addClassData(
                "DepositAdjustment",
                "Linear Slide Encoder Value",
                deposit.linearSlideMainMotor.getCurrentPosition()
        );
    }

    @Override
    protected void printAnalogInputValue() {

    }
}
