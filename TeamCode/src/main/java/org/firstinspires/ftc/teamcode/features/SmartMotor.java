package org.firstinspires.ftc.teamcode.features;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Vector;

public class SmartMotor {
    private final DcMotor motor;
    private final DcMotor encoder;
    private final String name;
    private static final Vector<SmartMotor> smartMotors = new Vector<>();

    private double targetPosition = 0.0;
    private double previousTargetPosition = 0.0;
    private double motorMaximumPower = 1.0;

    private final PID pidController = new PID(0.1, 0, 0);

    private boolean isSynchronized = false;
    private final Vector<SmartMotor> synchronizedMotors = new Vector<>();

    private boolean isPIDActivated = false;

    public SmartMotor(DcMotor motor, String name) {
        this.motor = motor;
        this.encoder = motor;
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.setMotorDirection(DcMotor.Direction.FORWARD);
        this.setEncoderDirection(DcMotor.Direction.FORWARD);
        this.resetEncoder();
        this.name = name;
        smartMotors.add(this);
    }

    public SmartMotor(DcMotor motor, DcMotor encoder, String name) {
        this.motor = motor;
        this.encoder = encoder;
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.setMotorDirection(DcMotor.Direction.FORWARD);
        this.setEncoderDirection(DcMotor.Direction.FORWARD);
        this.resetEncoder();
        this.name = name;
        smartMotors.add(this);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        this.motor.setZeroPowerBehavior(behavior);
    }

    public void setMotorDirection(DcMotor.Direction direction) {
        this.motor.setDirection(direction);
    }

    public void setEncoderDirection(DcMotor.Direction direction) {
        this.encoder.setDirection(direction);
    }

    public void resetEncoder() {
        this.encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPID(double p, double i, double d) {
        this.pidController.updatePID(p, i, d);
    }

    public void setMotorMaximumPower(double power) {
        this.motorMaximumPower = power;
    }

    public void synchronizeWith(String motorName) {
        this.isSynchronized = true;
        SmartMotor parentMotor = getMotorByName(motorName);
        if (parentMotor == null) {
            throw new IllegalArgumentException("Motor with name " + motorName + " not found.");
        }
        parentMotor.synchronizedMotors.add(this);
    }

    public void setPosition(double position) {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot set position while synchronized.");
        }
        this.targetPosition = position;
        if (this.previousTargetPosition != this.targetPosition) {
            this.pidController.resetIntegral();
            this.previousTargetPosition = targetPosition;
        }
    }

    public double getCurrentPosition() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot get current position while synchronized.");
        }
        return this.encoder.getCurrentPosition();
    }

    public double getTargetPosition() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot get target position while synchronized.");
        }
        return this.targetPosition;
    }

    public void activatePID() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot activate PID while synchronized.");
        }
        this.isPIDActivated = true;
    }

    public void lockAsCurrentPower() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot lock current power while synchronized.");
        }
        this.targetPosition = this.encoder.getCurrentPosition();
        this.previousTargetPosition = this.targetPosition;
        this.pidController.resetIntegral();
        this.isPIDActivated = false;
    }

    public void setPower(double power) {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot set power while synchronized.");
        }
        this.motor.setPower(power);
        for (SmartMotor synchronizedMotor : synchronizedMotors) {
            synchronizedMotor.motor.setPower(power);
        }
        this.targetPosition = this.encoder.getCurrentPosition();
        this.previousTargetPosition = this.targetPosition;
        this.pidController.resetIntegral();
        this.isPIDActivated = false;
    }

    public void stop() {
        if (isSynchronized) {
            throw new IllegalStateException("Cannot stop while synchronized.");
        }
        this.motor.setPower(0);
        for (SmartMotor synchronizedMotor : synchronizedMotors) {
            synchronizedMotor.motor.setPower(0);
        }
        this.targetPosition = this.motor.getCurrentPosition();
        this.previousTargetPosition = this.targetPosition;
        this.pidController.resetIntegral();
    }

    public DcMotor motor() {
        return motor;
    }

    public void update() {
        if (isPIDActivated) {
            double currentPosition = this.encoder.getCurrentPosition();
            double error = currentPosition - targetPosition;
            double pidOutput = this.pidController.update(error, -motorMaximumPower, motorMaximumPower);
            this.motor.setPower(pidOutput);
            for (SmartMotor synchronizedMotor : synchronizedMotors) {
                synchronizedMotor.motor.setPower(pidOutput);
            }
        }
    }

    public static SmartMotor getMotorByName(String name) {
        for (SmartMotor smartMotor : smartMotors) {
            if (smartMotor.name.equals(name)) {
                return smartMotor;
            }
        }
        return null; // or throw an exception if preferred
    }

    public static void updateAll() {
        for (SmartMotor smartMotor : smartMotors) {
            smartMotor.update();
        }
    }
}
