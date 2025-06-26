package org.firstinspires.ftc.teamcode.features;

public abstract class Adjustment {
    public enum State {
        ADJUST_SERVO,
        PRINTING_ENCODER_VALUE,
        ADJUST_PID
    }

    public static State ADJUSTMENT_SATE = State.ADJUST_SERVO;

    private boolean isActived = false;
    public void activate() {
        isActived = true;
    }

    public void update() {
        if (!isActived) {
            switch (ADJUSTMENT_SATE) {
                case ADJUST_SERVO:
                    adjustServo();
                    break;
                case PRINTING_ENCODER_VALUE:
                    printEncoderValue();
                    break;
                case ADJUST_PID:
                    adjustPID();
                    break;
            }
        }
    }

    protected abstract void adjustServo();
    protected abstract void adjustPID();
    protected abstract void printEncoderValue();
}
