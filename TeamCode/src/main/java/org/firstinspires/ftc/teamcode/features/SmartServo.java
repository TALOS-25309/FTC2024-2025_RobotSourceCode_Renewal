package org.firstinspires.ftc.teamcode.features;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.Vector;

public class SmartServo {
    private final Servo servo;
    private double position;

    private double targetPosition;
    private double previousPosition;
    private long startTime;
    private long duration;

    private final String name;
    private static final Vector<SmartServo> smartServos = new Vector<SmartServo>();

    private boolean isSynchronized = false;
    private double syncPositionOffset = 0.0; // Difference in position for synchronized servos
    private final Vector<SmartServo> synchronizedServos = new Vector<SmartServo>();

    public SmartServo(Servo servo, String name) {
        this.servo = servo;
        this.position = 0.5;
        this.targetPosition = 0.5;
        this.previousPosition = 0.5;
        this.startTime = 0;
        this.duration = 0;

        this.name = name;
        smartServos.add(this);
    }

    public void setDirection(Servo.Direction direction) {
        this.servo.setDirection(direction);
    }

    public void synchronizeWith(String name, double syncPositionOffset) {
        SmartServo parent = getServoByName(name);
        if (parent != null) {
            this.isSynchronized = true;
            parent.synchronizedServos.add(this);
            this.syncPositionOffset = syncPositionOffset;
        } else {
            throw new IllegalArgumentException("No SmartServo found with name: " + name);
        }
    }

    public void setPosition(double position) {
        if (position < 0.0 || position > 1.0) {
            throw new IllegalArgumentException("Position must be between 0.0 and 1.0");
        }

        if (isSynchronized) {
            throw new IllegalStateException("Cannot set position of synchronized servo. Use the parent servo instead.");
        }
        this.targetPosition = position;
        this.previousPosition = position;
        this.position = position;

        for (SmartServo smartServo : synchronizedServos) {
            if (position + smartServo.syncPositionOffset > 1.0) {
                position = 1.0 - smartServo.syncPositionOffset;
            } else if (position + smartServo.syncPositionOffset < 0.0) {
                position = 0.0 - smartServo.syncPositionOffset;
            }
        }

        servo.setPosition(position);
        for (SmartServo smartServo : synchronizedServos) {
            smartServo.position = position + smartServo.syncPositionOffset;
            smartServo.servo.setPosition(smartServo.position);
        }
    }

    public void setPosition(double position, double duration) {
        if (position < 0.0 || position > 1.0) {
            throw new IllegalArgumentException("Position must be between 0.0 and 1.0");
        }
        if (isSynchronized) {
            throw new IllegalStateException("Cannot set position of synchronized servo. Use the parent servo instead.");
        }
        this.targetPosition = position;
        this.previousPosition = this.position;
        this.startTime = System.nanoTime();
        this.duration = (long) (duration * 1e9);
    }

    public double getPosition() {
        return position;
    }

    public double getTargetPosition() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot get target position of synchronized servo. Use the parent servo instead.");
        }
        return targetPosition;
    }

    public Servo servo() {
        return servo;
    }

    public void update() {
        if (duration == 0) {
            return;
        }

        double progress = (System.nanoTime() - startTime) / (double) duration;

        if (progress < 0) progress = 0;
        else if (progress > 1) progress = 1;

        progress = 1 - (1-progress) * (1-progress) * (1-progress);
        if (progress >= 1) {
            position = targetPosition;
            duration = 0;
        } else {
            position = (1 - progress) * this.previousPosition + progress * targetPosition;
        }

        for (SmartServo smartServo : synchronizedServos) {
            if (position + smartServo.syncPositionOffset > 1.0) {
                position = 1.0 - smartServo.syncPositionOffset;
            } else if (position + smartServo.syncPositionOffset < 0.0) {
                position = 0.0 - smartServo.syncPositionOffset;
            }
        }

        servo.setPosition(position);
        for (SmartServo smartServo : synchronizedServos) {
            smartServo.position = position + smartServo.syncPositionOffset;
            smartServo.servo.setPosition(smartServo.position);
        }
    }

    public static SmartServo getServoByName(String name) {
        for (SmartServo smartServo : smartServos) {
            if (smartServo.name.equals(name)) {
                return smartServo;
            }
        }
        return null;
    }

    public static void updateAll() {
        for (SmartServo smartServo : smartServos) {
            smartServo.update();
        }
    }
}
