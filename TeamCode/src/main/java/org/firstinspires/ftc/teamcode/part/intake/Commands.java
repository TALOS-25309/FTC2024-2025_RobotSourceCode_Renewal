package org.firstinspires.ftc.teamcode.part.intake;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.vision.Sample;
import org.firstinspires.ftc.teamcode.vision.Vision;

public class Commands {
    private final Intake intake;

    public Commands(Intake intake) {
        this.intake = intake;
    }

    /**
     * Open the claw of the intake.
     * This will set the claw servo to the open position.
     * This method is not effective for the intake state.
     */
    public void openClaw() {
        Schedule.addTask(() -> {
            intake.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Close the claw of the intake.
     * This will set the claw servo to the closed position.
     * This method is not effective for the intake state.
     */
    public void closeClaw() {
        Schedule.addTask(() -> {
            intake.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the ready state.
     * This will open the claw and set the wrist and arm to the ready position.
     * State will be set to {@link IntakeState#READY_TO_PICKUP}.
     */
    public void ready() {
        intake.state = IntakeState.READY_TO_PICKUP;

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

    /**
     * Set the intake to the automatic ready state.
     * This will automatically detect the sample and set the hand position and orientation.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_TO_PICKUP} when the sample is detected.
     *
     * @param color The color of the sample to detect.
     */
    private void automaticReady(Sample.SampleColor color) {
        intake.state = IntakeState.AUTO_DETECTING;

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

            intake.state = IntakeState.READY_TO_PICKUP;
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

    /**
     * Set the intake to the automatic ready state for the alliance sample.
     * This will automatically detect the sample based on the alliance color.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_TO_PICKUP} when the sample is detected.
     */
    public void automaticReadyForAllianceSample() {
        if (Global.ALLIANCE == Global.Alliance.RED) {
            automaticReady(Sample.SampleColor.RED);
        } else if (Global.ALLIANCE == Global.Alliance.BLUE) {
            automaticReady(Sample.SampleColor.BLUE);
        } else {
            automaticReady(Sample.SampleColor.YELLOW);
        }
    }

    /**
     * Set the intake to the automatic ready state for the yellow sample.
     * This will automatically detect the sample based on the yellow color.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_TO_PICKUP} when the sample is detected.
     */
    public void automaticReadyForYellowSample() {
        automaticReady(Sample.SampleColor.YELLOW);
    }

    /**
     * Set the intake to the pickup state.
     * This will move the wrist and arm to the pickup position,
     * close the claw, and then move the wrist and arm to the ready position.
     * State will be set to {@link IntakeState#PICKED_UP}.
     */
    public void pickup() {
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_PICKUP_POSITION);
            intake.armUpDownServo.setPosition(Constants.ARM_PICKUP_POSITION);
            intake.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.command().closeClaw();
        }, Constants.PICKUP_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            intake.state = IntakeState.PICKED_UP;

            intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
            intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        }, Constants.PICKUP_DELAY_FOR_MOVE_UP);
    }

    /**
     * Set the intake to the discard state.
     * This will move the wrist and arm to the ready position,
     * open the claw, and then set the state to {@link IntakeState#READY_TO_PICKUP}.
     */
    public void discard() {
        intake.state = IntakeState.READY_TO_PICKUP;

        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
            intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
            intake.command().openClaw();
        }, Schedule.RUN_INSTANTLY);
    }

    public void compactReady() {
        Schedule.addTask(() -> {
            intake.current_y = 0.0;
            intake.linearSlideMotor.setPosition(0.0); // Move linear slide to the bottom
            intake.linearSlideMotor.activatePID();
            intake.command().ready();
        }, Schedule.RUN_INSTANTLY);
    }

    public void readyForTransfer() {
        intake.state = IntakeState.READY_FOR_TRANSFER;
        Schedule.addTask(() -> {
            intake.current_y = 0.0;
            intake.linearSlideMotor.setPosition(0.0); // Move linear slide to the bottom
            intake.linearSlideMotor.activatePID();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.current_orientation =
                    (Constants.WRIST_ORIENTATION_TRANSFER_POSITION
                            - Constants.WRIST_ORIENTATION_LEFT_LIMIT)
                            / (Constants.WRIST_ORIENTATION_RIGHT_LIMIT
                            - Constants.WRIST_ORIENTATION_LEFT_LIMIT);
            intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.current_x = 0.0;
            intake.turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the transfer state.
     * This will move the wrist and arm to the transfer position,
     * open the claw, and then move the linear slide to the bottom.
     * State will be set to {@link IntakeState#TRANSFER_SAMPLE}
     * and then to {@link IntakeState#REST} after the transfer is complete.
     */
    public void transfer() {
        intake.state = IntakeState.TRANSFER_SAMPLE;

        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.state = IntakeState.REST;
            intake.command().openClaw();
        }, Constants.TRANSFER_DELAY_FOR_OPEN_CLAW);

        Schedule.addTask(() -> {
            intake.command().ready();
        }, Constants.TRANSFER_DELAY_FOR_READY);

        Schedule.addTask(() -> {
            intake.linearSlideMotor.setPower(Constants.LINEAR_SLIDE_STABLE_POWER);
        }, Schedule.RUN_INSTANTLY);
    }

    public void movePositionXY(double x, double y) {
        TelemetrySystem.addClassData("Intake", "X", x);
        TelemetrySystem.addClassData("Intake", "Y", y);

        if(x > Constants.ARM_LENGTH) x = Constants.ARM_LENGTH;
        else if (x < -Constants.ARM_LENGTH) x = -Constants.ARM_LENGTH;

        double theta = Math.asin(x / Constants.ARM_LENGTH) + Constants.TURRET_RANGE / 2.0;
        double turretPosition = theta / Constants.TURRET_RANGE;
        double targetLinearLength =
                y - Constants.ARM_LENGTH * Math.cos(theta - Constants.TURRET_RANGE / 2.0);
        double linearMotorPosition =
                targetLinearLength / Constants.LINEAR_SLIDE_MAX_LENGTH * Constants.LINEAR_SLIDE_RANGE;

        // Constraints
        if (turretPosition < Constants.TURRET_LEFT_LIMIT) {
            turretPosition = Constants.TURRET_LEFT_LIMIT;
            theta = Constants.TURRET_LEFT_LIMIT * Constants.TURRET_RANGE;
        } else if (turretPosition > Constants.TURRET_RIGHT_LIMIT) {
            turretPosition = Constants.TURRET_RIGHT_LIMIT;
            theta = Constants.TURRET_RIGHT_LIMIT * Constants.TURRET_RANGE;
        }
        if (linearMotorPosition < 0) {
            linearMotorPosition = 0;
            targetLinearLength = 0;
        } else if (linearMotorPosition > Constants.LINEAR_SLIDE_RANGE) {
            linearMotorPosition = Constants.LINEAR_SLIDE_RANGE;
            targetLinearLength = Constants.LINEAR_SLIDE_MAX_LENGTH;
        }

        intake.current_x = Constants.ARM_LENGTH * Math.sin(theta - Constants.TURRET_RANGE / 2.0);
        intake.current_y = targetLinearLength
                + Constants.ARM_LENGTH * Math.cos(theta - Constants.TURRET_RANGE / 2.0);

        double finalLinearMotorPosition = linearMotorPosition;
        double finalTurretPosition = turretPosition;

        intake.linearSlideMotor.setPosition(finalLinearMotorPosition);
        intake.turretServo.setPosition(finalTurretPosition);
        intake.linearSlideMotor.activatePID();
    }

    public void movePositiondXdY(double dX, double dY) {
        intake.current_x += dX * 0.5;
        intake.current_y += dY * 0.5;
        movePositionXY(intake.current_x, intake.current_y);
    }

    public void rotateOrientation(double orientation) {
        TelemetrySystem.addClassData("Intake", "Angle", orientation);

        double turretTheta = intake.turretServo.getPosition() * Constants.TURRET_RANGE_IN_DEGREE
                - Constants.TURRET_RANGE_IN_DEGREE / 2.0;

        double angle = orientation - turretTheta;
        angle += 180;
        while (angle > 180.0) angle -= 180;
        while (angle < 0.0) angle += 180;

        double finalOrientation =  1 - angle / 180.0;

        if (finalOrientation > 1.0) finalOrientation = 1.0;
        if (finalOrientation < 0.0) finalOrientation = 0.0;

        double pos = finalOrientation *
                (Constants.WRIST_ORIENTATION_RIGHT_LIMIT
                        - Constants.WRIST_ORIENTATION_LEFT_LIMIT)
                + Constants.WRIST_ORIENTATION_LEFT_LIMIT;
        // Constraints
        if (pos < Constants.WRIST_ORIENTATION_LEFT_LIMIT) {
            pos = Constants.WRIST_ORIENTATION_LEFT_LIMIT;
        } else if (pos > Constants.WRIST_ORIENTATION_RIGHT_LIMIT) {
            pos = Constants.WRIST_ORIENTATION_RIGHT_LIMIT;
        }
        intake.wristOrientationServo.setPosition(pos);
    }

    public void rotateDeltaOrientation(double deltaOrientation) {
        intake.current_orientation += deltaOrientation;
        // TODO : Limit the orientation to the range
        rotateOrientation(intake.current_orientation);
    }
}
