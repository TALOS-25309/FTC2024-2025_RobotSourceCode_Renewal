package org.firstinspires.ftc.teamcode.part.deposit;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;

public class Commands {
    private final Deposit deposit;

    public Commands(Deposit deposit) {
        this.deposit = deposit;
    }

    /**
     * Opens the claw of the deposit part.
     * This method is not effective to the deposit state.
     */
    public void openClaw() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Set the intake to the ready state.
     * This will open the claw and set the wrist and arm to the ready position.
     * State will be set to {@link DepositState#REST}.
     */
    public void rest() {
        deposit.state = DepositState.REST;
        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Closes the claw of the deposit part.
     * This method is not effective to the deposit state.
     */
    public void closeClaw() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION());
        }, Schedule.RUN_INSTANTLY);
    }
    
    public void closeClawForSpecimen() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION_FOR_SPECIMEN);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Trasnfers the sample from the intake part to the deposit part.
     * This method changes the deposit state to {@link DepositState#TRANSFER}
     * and it will be {@link DepositState#LOAD_SAMPLE} after the transfer is done.
     */
    public void transfer() {
        deposit.state = DepositState.TRANSFER;

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_TRANSFER_POSITION());
        }, Constants.TRANSFER_DELAY_FOR_GOTO_TRANSFER_POSITION);

        Schedule.addTask(() -> {
            deposit.command().closeClaw();
        }, Constants.TRANSFER_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            if(Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
                deposit.state = DepositState.LOAD_SAMPLE;
            } else if(Global.TRANSFER_TYPE == Global.TransferType.SPECIMEN) {
                deposit.state = DepositState.LOAD_SPECIMEN;
            }

            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.TRANSFER_DELAY_FOR_GOTO_READY_POSITION);
    }

    /**
     * Prepares the deposit part to pick up a specimen.
     * This method changes the deposit state to {@link DepositState#READY_FOR_PICKUP}.
     */
    public void poseForSpecimenPickup() {
        deposit.state = DepositState.READY_FOR_PICKUP;

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_PICKUP_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_SPECIMEN_PICKUP_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Picks up a specimen using the deposit part and go to the ready position.
     * This method changes the deposit state to {@link DepositState#READY_FOR_PICKUP}.
     */
    public void pickupSpecimen() {
        deposit.state = DepositState.LOAD_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.command().closeClawForSpecimen();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
           deposit.command().poseForHighSpecimenScoringForward();
        }, Constants.PICKUP_DELAY_FOR_GOTO_SPECIMEN_SCORING_POSE);
    }

    /**
     * Prepares the discard sample.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DISCARD}.
     */
    public void poseForDiscard() {
        deposit.state = DepositState.READY_FOR_DISCARD;

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_DISCARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Prepares the discard sample.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DISCARD}.
     */
    public void discard() {
        Schedule.addTask(() -> {
            deposit.command().poseForDiscard();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Constants.DISCARD_DELAY_FOR_OPEN_CLAW);

        Schedule.addTask(() -> {
            deposit.state = DepositState.REST;
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
        }, Constants.DISCARD_DELAY_FOR_GOTO_READY_POSITION);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.DISCARD_DELAY_FOR_GOTO_READY_POSITION);
    }

    /**
     * Prepares the deposit part to score a low basket.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_BASKET}.
     */
    public void poseForLowBasketScoring() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_FOR_DEPOSIT_BASKET;

            Schedule.addTask(() -> {
                deposit.armMainServo.setPosition(Constants.ARM_BASKET_SCORING_POSITION);
            }, Schedule.RUN_INSTANTLY);

            Schedule.addTask(() -> {
                deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION);
            }, Schedule.RUN_INSTANTLY);
        }, Schedule.RUN_INSTANTLY, () -> deposit.state == DepositState.LOAD_SAMPLE);
    }

    /**
     * Prepares the deposit part to score a high basket.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_BASKET}.
     */
    public void poseForHighBasketScoring() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_FOR_DEPOSIT_BASKET;

            Schedule.addTask(() -> {
                deposit.armMainServo.setPosition(Constants.ARM_BASKET_SCORING_POSITION);
            }, Constants.BASKET_DELAY_FOR_MOVING_ARM);

            Schedule.addTask(() -> {
                deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_BASKET_SCORING_POSITION);
            }, Schedule.RUN_INSTANTLY);
        }, Schedule.RUN_INSTANTLY, () -> deposit.state == DepositState.LOAD_SAMPLE);
    }

    /**
     * Scores a basket in the deposit part and returns to the resting state.
     * This method changes the deposit state to {@link DepositState#REST}.
     */
    public void scoringBasket() {
        deposit.state = DepositState.REST;

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
        }, Constants.SCORING_BASKET_DELAY_FOR_GOTO_READY_POSITION);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.SCORING_BASKET_DELAY_FOR_GOTO_READY_POSITION);
    }

    /**
     * Prepares the deposit part to score a specimen forward for high beam.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_SPECIMEN}.
     */
    @Deprecated
    public void poseForLowSpecimenScoringForward() {
        if (true) {
            throw new UnsupportedOperationException("This feature is no longer supported");
        }
        deposit.state = DepositState.READY_FOR_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
    }

    /**
     * Prepares the deposit part to score a specimen backward for low beam.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_SPECIMEN}.
     */
    @Deprecated
    public void poseForLowSpecimenScoringBackward() {
        if (true) {
            throw new UnsupportedOperationException("This feature is no longer supported");
        }
        deposit.state = DepositState.READY_FOR_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
    }

    /**
     * Prepares the deposit part to score a specimen forward for high beam.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_SPECIMEN}.
     */
    public void poseForHighSpecimenScoringForward() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_FOR_DEPOSIT_SPECIMEN;

            Schedule.addTask(() -> {
                deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION);
            }, Schedule.RUN_INSTANTLY);

            Schedule.addTask(() -> {
                deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
            }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
        }, Schedule.RUN_INSTANTLY, () -> deposit.state == DepositState.LOAD_SPECIMEN);
    }

    /**
     * Prepares the deposit part to score a specimen backward for high beam.
     * This method changes the deposit state to {@link DepositState#READY_FOR_DEPOSIT_SPECIMEN}.
     */
    public void poseForHighSpecimenScoringBackward() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_FOR_DEPOSIT_SPECIMEN;

            Schedule.addTask(() -> {
                deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION);
            }, Schedule.RUN_INSTANTLY);

            Schedule.addTask(() -> {
                deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
            }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
        }, Schedule.RUN_INSTANTLY, () -> deposit.state == DepositState.LOAD_SPECIMEN);
    }

    /**
     * Scores a specimen in the deposit part and returns to the resting state.
     * This method changes the deposit state to {@link DepositState#REST}.
     */
    public void scoringSpecimen() {
        deposit.state = DepositState.REST;

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.command().poseForSpecimenPickup();
        }, Constants.SCORING_SPECIMEN_DELAY_FOR_GOTO_READY_POSITION);
    }

    public void ascendingReady() {
        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_ASCENDING_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void ascend() {
        deposit.state = DepositState.ASCENDING;
        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_ASCENDING_CLIMBING_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }
}
