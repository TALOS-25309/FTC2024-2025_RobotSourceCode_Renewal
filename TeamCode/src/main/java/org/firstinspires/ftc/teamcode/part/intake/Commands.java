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

    public void closeClawMaximum() {
        Schedule.addTask(() -> {
            intake.clawServo.setPosition(Constants.CLAW_CLOSED_MAXIMUM_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the ready state.
     * This will open the claw and set the wrist and arm to the ready position.
     * State will be set to {@link IntakeState#READY_FOR_PICKUP}.
     */
    public void ready() {
        intake.state = IntakeState.READY_FOR_PICKUP;

        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.command().openClaw();
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.command().rotateOrientation(intake.current_orientation);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the automatic ready state.
     * This will automatically detect the sample and set the hand position and orientation.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_FOR_PICKUP} when the sample is detected.
     *
     * @param color The color of the sample to detect.
     */
    private void automaticTarget(Sample.SampleColor color, boolean cautious) {
        if (Global.DETECTING) {
            return;
        }

        Global.DETECTING = true;

        Schedule.addTask(() -> {
            intake.command().ready();
        }, Schedule.RUN_INSTANTLY);

        intake.state = IntakeState.AUTO_DETECTING;

        // Automatically set hand position and orientation
        Schedule.addTask(() -> {
            intake.vision.request(color);
        }, Constants.DETECTION_DELAY);

        Schedule.addConditionalTask(() -> {
            intake.state = IntakeState.READY_FOR_PICKUP;
            Sample sample = intake.vision.getTargetData();
            TelemetrySystem.addClassData("Intake", "Sample State", sample.state().toString());
            if (sample.state() == Sample.State.DETECTED) {
                double x = sample.getX();
                double y = sample.getY();
                double omega = sample.getAngle();

                TelemetrySystem.addClassData("Intake", "Sample Color", sample.color.toString());
                TelemetrySystem.addClassData("Intake", "Sample X", sample.getX());
                TelemetrySystem.addClassData("Intake", "Sample Y", sample.getY());
                TelemetrySystem.addClassData("Intake", "Sample Angle", sample.getAngle());

                double currentTurretFraction =
                        (intake.turretServo.getPosition() - Constants.TURRET_LEFT_LIMIT)
                        / (Constants.TURRET_RIGHT_LIMIT - Constants.TURRET_LEFT_LIMIT);
                double currentTurretAngle = currentTurretFraction * Constants.TURRET_RANGE
                        - Constants.TURRET_RANGE / 2.0;

                currentTurretAngle = -currentTurretAngle;

                double finalX = x * Math.cos(currentTurretAngle) - y * Math.sin(currentTurretAngle);
                double finalY = x * Math.sin(currentTurretAngle) + y * Math.cos(currentTurretAngle);

                double finalOmega = omega - Math.toDegrees(currentTurretAngle) + 90;

                if(cautious) {
                    Schedule.addTask(() -> {
                        intake.armUpDownServo.setPosition(Constants.ARM_CAUTIOUS_PICKUP_READY_POSITION);
                        intake.wristUpDownServo.setPosition(Constants.WRIST_PICKUP_POSITION());
                    }, Schedule.RUN_INSTANTLY);
                }

                Schedule.addTask(() -> {
                    intake.command().setPositionDelta(
                            finalX, finalY, finalOmega - intake.current_orientation
                    );
                }, Schedule.RUN_INSTANTLY);

                Schedule.addTask(() -> {
                    intake.command().pickup(cautious);
                }, Constants.MIN_DETECTION_DELAY_FOR_PICKUP);
            } else {
                Global.DETECTING = false;
            }
        }, Constants.DETECTION_DELAY, () -> intake.vision.currentState() != Vision.State.REQUESTED);
    }

    /**
     * Set the intake to the automatic ready state for the alliance sample.
     * This will automatically detect the sample based on the alliance color.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_FOR_PICKUP} when the sample is detected.
     */
    public void automaticTargetForAllianceSample() {
        automaticTargetForAllianceSample(false);
    }
    public void automaticTargetForAllianceSample(boolean cautious) {
        if (Global.ALLIANCE == Global.Alliance.RED) {
            automaticTarget(Sample.SampleColor.RED, cautious);
        } else if (Global.ALLIANCE == Global.Alliance.BLUE) {
            automaticTarget(Sample.SampleColor.BLUE, cautious);
        } else {
            automaticTarget(Sample.SampleColor.YELLOW, cautious);
        }
    }

    /**
     * Set the intake to the automatic ready state for the yellow sample.
     * This will automatically detect the sample based on the yellow color.
     * State will be set to {@link IntakeState#AUTO_DETECTING}
     * and then to {@link IntakeState#READY_FOR_PICKUP} when the sample is detected.
     */
    public void automaticTargetForYellowSample() {
        automaticTargetForYellowSample(false);
    }
    public void automaticTargetForYellowSample(boolean cautious) {
        automaticTarget(Sample.SampleColor.YELLOW, cautious);
    }

    /**
     * Set the intake to the pickup state.
     * This will move the wrist and arm to the pickup position,
     * close the claw, and then move the wrist and arm to the ready position.
     * State will be set to {@link IntakeState#PICKED_UP}.
     */
    public void pickup() {
        pickup(false);
    }
    public void pickup(boolean cautious) {
        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_PICKUP_POSITION());
            intake.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_PICKUP_POSITION());
        }, Constants.PICKUP_DELAY_FOR_MOVE_DOWN);

        Schedule.addTask(() -> {
            intake.command().closeClaw();
        }, Constants.PICKUP_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            intake.state = IntakeState.PICKED_UP;
            if(!cautious) {
                intake.wristUpDownServo.setPosition(Constants.WRIST_READY_POSITION);
                intake.wristOrientationServo.setPosition(Constants.WRIST_ORIENTATION_TRANSFER_POSITION);
                intake.armUpDownServo.setPosition(Constants.ARM_READY_POSITION);
            }
            intake.command().closeClawMaximum();
        }, Constants.PICKUP_DELAY_FOR_MOVE_UP);

        Schedule.addTask(() -> {
            if (intake.clawAnalogInput.getVoltage() > Constants.ANALOG_INPUT_PICKUP_THRESHOLD) {
                intake.command().closeClaw();
                intake.command().readyForTransfer();
            } else {
                intake.command().discard();
            }
            Global.DETECTING = false;
        }, Constants.PICKUP_DELAY_FOR_CHECKING_PICKUP);
    }

    /**
     * Set the intake to the discard state.
     * This will move the wrist and arm to the ready position,
     * open the claw, and then set the state to {@link IntakeState#READY_FOR_PICKUP}.
     */
    public void discard() {
        intake.state = IntakeState.READY_FOR_PICKUP;

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
            intake.wristUpDownServo.setPosition(Constants.WRIST_TRANSFER_POSITION());
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_PRE_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.current_x = 0.0;
            intake.turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            intake.command().rotateOrientation(0);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the transfer state.
     * This will move the wrist and arm to the transfer position,
     * open the claw, and then move the linear slide to the bottom.
     * State will be set to {@link IntakeState#TRANSFER}
     * and then to {@link IntakeState#REST} after the transfer is complete.
     */
    public void transfer() {
        intake.state = IntakeState.TRANSFER;

        Schedule.addTask(() -> {
            intake.armUpDownServo.setPosition(Constants.ARM_TRANSFER_POSITION());
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

    public void drop() {
        intake.state = IntakeState.DROP_SAMPLE;

        Schedule.addTask(() -> {
            intake.wristUpDownServo.setPosition(Constants.WRIST_DROP_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            intake.turretServo.setPosition(Constants.TURRET_DROP_POSITION);
        }, Constants.DROP_DELAY_FOR_MOVE_RIGHT);

        Schedule.addTask(() -> {
            intake.state = IntakeState.REST;
            intake.command().openClaw();
        }, Constants.DROP_DELAY_FOR_OPEN_CLAW);

        Schedule.addTask(() -> {
            intake.turretServo.setPosition(Constants.TURRET_TRANSFER_POSITION);
        }, Constants.DROP_DELAY_FOR_MOVE_CENTER);

        Schedule.addTask(() -> {
            intake.command().ready();
        }, Constants.DROP_DELAY_FOR_READY);
    }

    public void setPosition(double x, double y, double angle) {
        while (angle < 0) {
            angle += 180;
        }
        while (angle > 180) {
            angle -= 180;
        }

        double phi = Math.toRadians(angle);
        double l = Constants.CLAW_DISTANCE;
        double L = Constants.ARM_LENGTH;
        double alpha = Math.toRadians(Constants.ANGLE_OFFSET);

        double t = (x - l * Math.sin(phi)) / L;
        if (t < -1) {
            x = -L + l * Math.sin(phi);
            t = -1;
        } else if (t > 1) {
            x = L + l * Math.sin(phi);
            t = 1;
        }
        double theta = Math.asin(t);

        double turretAngle = theta + alpha;

        double turretFraction = (turretAngle + Constants.TURRET_RANGE / 2.0) / Constants.TURRET_RANGE;
        if (turretFraction > 1.0) {
            turretFraction = 1.0;
            turretAngle = Constants.TURRET_RANGE / 2.0;
            theta = turretAngle - alpha;
            t = Math.sin(theta);
            x = l * Math.sin(phi) + L * t;
        } else if (turretFraction < 0.0) {
            turretFraction = 0.0;
            turretAngle = -Constants.TURRET_RANGE / 2.0;
            theta = turretAngle - alpha;
            t = Math.sin(theta);
            x = l * Math.sin(phi) + L * t;
        }

        double wristAngle = -phi + turretAngle;
        while (wristAngle < 0) {
            wristAngle += Math.PI;
        }
        while (wristAngle > Math.PI) {
            wristAngle -= Math.PI;
        }
        double wristFraction = wristAngle / Math.PI;


        wristFraction += 0.5;
        if(wristFraction > 1.0) wristFraction -= 1.0;
        double turretPosition = Constants.TURRET_LEFT_LIMIT
                + turretFraction
                * (Constants.TURRET_RIGHT_LIMIT - Constants.TURRET_LEFT_LIMIT);
        double wristPosition = Constants.WRIST_ORIENTATION_LEFT_LIMIT
                + wristFraction
                * (Constants.WRIST_ORIENTATION_RIGHT_LIMIT - Constants.WRIST_ORIENTATION_LEFT_LIMIT);

        double linearLength = y - L * Math.cos(theta) - l * Math.cos(phi);
        if (linearLength < 0) {
            linearLength = 0;
            y = L * Math.cos(theta) + l * Math.cos(phi);
        } else if (linearLength > Constants.LINEAR_SLIDE_MAX_LENGTH) {
            linearLength = Constants.LINEAR_SLIDE_MAX_LENGTH;
            y = L * Math.cos(theta) + l * Math.cos(phi) + Constants.LINEAR_SLIDE_MAX_LENGTH;
        }
        double linearPosition =
                linearLength / Constants.LINEAR_SLIDE_MAX_LENGTH * Constants.LINEAR_SLIDE_RANGE;

        intake.linearSlideMotor.setPosition(linearPosition);
        intake.turretServo.setPosition(turretPosition);
        intake.wristOrientationServo.setPosition(wristPosition);
        intake.linearSlideMotor.activatePID();

        this.intake.current_x = x;
        this.intake.current_y = y;
        this.intake.current_orientation = angle;
    }

    public void movePositionXY(double x, double y) {
        intake.command().setPosition(x, y, intake.current_orientation);
    }

    public void movePositiondXdY(double dX, double dY) {
        intake.current_x += dX;
        intake.current_y += dY;
        movePositionXY(intake.current_x, intake.current_y);
    }

    public void rotateOrientation(double orientation) {
        intake.command().setPosition(intake.current_x, intake.current_y, orientation);
    }

    public void rotateDeltaOrientation(double deltaOrientation) {
        intake.current_orientation += deltaOrientation;
        rotateOrientation(intake.current_orientation);
    }

    public void setPositionDelta(double deltaX, double deltaY, double deltaOrientation) {
        intake.current_x += deltaX;
        intake.current_y += deltaY;
        intake.current_orientation += deltaOrientation;
        setPosition(intake.current_x, intake.current_y, intake.current_orientation);
    }
}
