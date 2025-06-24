package org.firstinspires.ftc.teamcode.part.intake;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.vision.Sample;
import org.firstinspires.ftc.teamcode.vision.Vision;

public class Commands {
    private final Intake intake;

    public Commands(Intake intake) {
        this.intake = intake;
    }

    public void openClaw() {
        Schedule.addTask(() -> {
            intake.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void closeClaw() {
        Schedule.addTask(() -> {
            intake.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void ready() {
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.command().openClaw();
        }, Schedule.RUN_INSTANTLY);
    }

    private void automaticReady(Sample.SampleColor color) {
        // Automatically set hand position and orientation
        Schedule.addTask(() -> {
           intake.vision.request(color);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTaskAsync(() -> {
            while(intake.vision.currentState() != Vision.State.DETECTED
                    && intake.vision.currentState() != Vision.State.FAILED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Sample sample = intake.vision.getTargetData();
            if (sample.state() == Sample.State.DETECTED) {
                double x = sample.getX();
                double y = sample.getY();
                double omega = (sample.getAngle() + 90) / 180;
                Schedule.addTask(() -> {
                    intake.command().movePositionXY(x,y);
                    intake.command().rotateOrientation(omega);
                }, Schedule.RUN_INSTANTLY);
                Schedule.addTask(() -> {
                    intake.command().ready();
                }, Schedule.RUN_INSTANTLY);
            }
        }, Schedule.RUN_INSTANTLY);
    }

    public void automaticReadyForAllianceSample() {
        if (Global.ALLIANCE == Global.Alliance.RED) {
            automaticReady(Sample.SampleColor.RED);
        } else if (Global.ALLIANCE == Global.Alliance.BLUE) {
            automaticReady(Sample.SampleColor.BLUE);
        } else {
            automaticReady(Sample.SampleColor.YELLOW);
        }
    }

    public void automaticReadyForYellowSample() {
        automaticReady(Sample.SampleColor.YELLOW);
    }

    public void pickup() {
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_PICKUP_POSITION);
            intake.armUpDownServo.setPosition(Constants.ARM_PICKUP_POSITION);
            intake.command().closeClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.command().closeClaw();
        }, Constants.PICKUP_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
            intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        }, Constants.PICKUP_DELAY_FOR_MOVE_UP);
    }

    public void transfer() {
        Schedule.addTask(() -> {
            intake.linearSlideMotor.setPosition(0.0); // Move linear slide to the bottom
            intake.linearSlideMotor.activatePID();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.command().openClaw();
        }, Constants.TRANSFER_DELAY_FOR_OPEN_CLAW);

        Schedule.addTask(() -> {
            intake.linearSlideMotor.setPower(Constants.LINEAR_SLIDE_STABLE_POWER);
        }, Constants.TRANSFER_DELAY_FOR_LINEAR_SLIDE_STABILIZATION);
    }

    public void movePositionXY(double x, double y) {
        double theta = Math.asin(x / Constants.ARM_LENGTH) + Constants.TURRET_RANGE / 2.0;
        double turretPosition = theta / Constants.TURRET_RANGE;
        double targetLinearLength =
                y - Constants.ARM_LENGTH * Math.cos(theta - Constants.TURRET_RANGE / 2.0);
        double linearMotorPosition =
                targetLinearLength / Constants.LINEAR_SLIDE_MAX_LENGTH * Constants.LINEAR_SLIDE_RANGE;
        Schedule.addTask(() -> {
            intake.linearSlideMotor.setPosition(linearMotorPosition);
            intake.turretServo.setPosition(turretPosition);
            intake.linearSlideMotor.activatePID();
        }, Schedule.RUN_INSTANTLY);
    }

    public void rotateOrientation(double orientation) {
        Schedule.addTask(() -> {
            intake.wristOrientationServo.setPosition(
                    orientation *
                            (Constants.WRIST_ORIENTATION_RIGHT_LIMIT
                                    - Constants.WRIST_ORIENTATION_LEFT_LIMIT)
                            + Constants.WRIST_ORIENTATION_LEFT_LIMIT
            );
        }, Schedule.RUN_INSTANTLY);
    }
}
