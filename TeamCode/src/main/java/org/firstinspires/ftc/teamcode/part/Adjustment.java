package org.firstinspires.ftc.teamcode.part;

/**
 * Abstract class for adjusting part classes.
 * <p>
 * This class defines the structure for adjustments. <br>
 * State is the mode of adjustment, which can be: <br>
 * - {@link State#ADJUST_SERVO}: Adjust the servo positions of the part. <br>
 * - {@link State#PRINTING_ENCODER_VALUE}: Print the encoder values for the part's motors. <br>
 * - {@link State#ADJUST_PID}: Adjust the PID values for the part's motors. <br>
 * <p>
 * We have to define the following methods: <br>
 * - {@link Adjustment#adjustServo()}: Adjust the servo positions of the part. <br>
 * - {@link Adjustment#adjustPID()}: Adjust the PID values for the part's motors. <br>
 * - {@link Adjustment#printEncoderValue()}: Print the encoder values for the part's motors. <br>
 * The basic format of these methods must not change,
 * but you have to implement them for specific cases and hardware.
 */
public abstract class Adjustment {
    public enum State {
        ADJUST_SERVO,
        PRINTING_ENCODER_VALUE,
        PRINTING_ANALOG_INPUT_VALUE,
        ADJUST_PID,
    }

    protected State adjustState = State.ADJUST_SERVO;

    private boolean isActived = false;
    public void activate() {
        isActived = true;
    }

    public void update() {
        setAdjustState();
        if (isActived) {
            switch (adjustState) {
                case ADJUST_SERVO:
                    adjustServo();
                    break;
                case PRINTING_ENCODER_VALUE:
                    printEncoderValue();
                    break;
                case ADJUST_PID:
                    adjustPID();
                    break;
                case PRINTING_ANALOG_INPUT_VALUE:
                    printAnalogInputValue();
                    break;
            }
        }
    }

    protected abstract void setAdjustState();
    protected abstract void adjustServo();
    protected abstract void adjustPID();
    protected abstract void printEncoderValue();
    protected abstract void printAnalogInputValue();
}
