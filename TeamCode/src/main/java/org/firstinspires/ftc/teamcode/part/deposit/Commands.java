package org.firstinspires.ftc.teamcode.part.deposit;

import org.firstinspires.ftc.teamcode.features.Schedule;

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
     * Closes the claw of the deposit part.
     * This method is not effective to the deposit state.
     */
    public void closeClaw() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Trasnfers the sample from the intake part to the deposit part.
     * This method changes the deposit state to {@link DepositState#TRANSFER_SAMPLE}
     * and it will be {@link DepositState#LOAD_SAMPLE} after the transfer is done.
     */
    public void transfer() {
        deposit.state = DepositState.TRANSFER_SAMPLE;

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_TRANSFER_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_TRANSFER_POSITION);
        }, Constants.TRANSFER_DELAY_FOR_GOTO_TRANSFER_POSITION);

        Schedule.addTask(() -> {
            Constants.CLAW_CLOSED_POSITION = Constants.CLAW_CLOSED_POSITION_FOR_SAMPLE;
            deposit.command().closeClaw();
        }, Constants.TRANSFER_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            deposit.state = DepositState.LOAD_SAMPLE;

            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.TRANSFER_DELAY_FOR_GOTO_READY_POSITION);
    }

    /**
     * Prepares the deposit part to pick up a specimen.
     * This method changes the deposit state to {@link DepositState#READY_TO_PICKUP}.
     */
    public void poseForSpecimenPickup() {
        deposit.state = DepositState.READY_TO_PICKUP;

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
     * This method changes the deposit state to {@link DepositState#READY_TO_PICKUP}.
     */
    public void pickupSpecimen() {
        deposit.state = DepositState.LOAD_SPECIMEN;

        Schedule.addTask(() -> {
            Constants.CLAW_CLOSED_POSITION = Constants.CLAW_CLOSED_POSITION_FOR_SPECIMEN;
            deposit.command().closeClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.command().poseForHighSpecimenScoringForward();
        }, Constants.PICKUP_DELAY_FOR_GOTO_SPECIMEN_SCORING_POSE);
    }

    /**
     * Prepares the discard sample.
     * This method changes the deposit state to {@link DepositState#READY_TO_DISCARD}.
     */
    public void poseForDiscard() {
        deposit.state = DepositState.READY_TO_DISCARD;

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_DISCARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    /**
     * Prepares the discard sample.
     * This method changes the deposit state to {@link DepositState#READY_TO_DISCARD}.
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
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_BASKET}.
     */
    public void poseForLowBasketScoring() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_TO_DEPOSIT_BASKET;

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
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_BASKET}.
     */
    public void poseForHighBasketScoring() {
        Schedule.addConditionalTask(() -> {
            deposit.state = DepositState.READY_TO_DEPOSIT_BASKET;

            Schedule.addTask(() -> {
                deposit.armMainServo.setPosition(Constants.ARM_BASKET_SCORING_POSITION);
            }, Schedule.RUN_INSTANTLY);

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
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_SPECIMEN}.
     */
    public void poseForLowSpecimenScoringForward() {
        deposit.state = DepositState.READY_TO_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
    }

    /**
     * Prepares the deposit part to score a specimen backward for low beam.
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_SPECIMEN}.
     */
    public void poseForLowSpecimenScoringBackward() {
        deposit.state = DepositState.READY_TO_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
    }

    /**
     * Prepares the deposit part to score a specimen forward for high beam.
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_SPECIMEN}.
     */
    public void poseForHighSpecimenScoringForward() {
        deposit.state = DepositState.READY_TO_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
    }

    /**
     * Prepares the deposit part to score a specimen backward for high beam.
     * This method changes the deposit state to {@link DepositState#READY_TO_DEPOSIT_SPECIMEN}.
     */
    public void poseForHighSpecimenScoringBackward() {
        deposit.state = DepositState.READY_TO_DEPOSIT_SPECIMEN;

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Constants.SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM);
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
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
        }, Constants.SCORING_SPECIMEN_DELAY_FOR_GOTO_READY_POSITION);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.SCORING_SPECIMEN_DELAY_FOR_GOTO_READY_POSITION);
    }
}
