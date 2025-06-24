package org.firstinspires.ftc.teamcode.part.deposit;

import org.firstinspires.ftc.teamcode.features.Schedule;

public class Commands {
    private final Deposit deposit;

    public Commands(Deposit deposit) {
        this.deposit = deposit;
    }

    public void openClaw() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_OPEN_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void closeClaw() {
        Schedule.addTask(() -> {
            deposit.clawServo.setPosition(Constants.CLAW_CLOSED_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void ready() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.command().openClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void transfer() {
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
            deposit.command().closeClaw();
        }, Constants.TRANSFER_DELAY_FOR_CLOSE_CLAW);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_READY_POSITION);
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_READY_POSITION);
        }, Constants.TRANSFER_DELAY_FOR_GOTO_READY_POSITION);
    }

    public void poseForSpecimenPickup() {
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

    public void pickupSpecimen() {
        Schedule.addTask(() -> {
            deposit.command().closeClaw();
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_BASKET_SCORING_POSITION);
        }, Constants.PICKUP_DELAY_FOR_GOTO_READY_POSITION);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION);
        }, Constants.PICKUP_DELAY_FOR_GOTO_READY_POSITION);
    }

    public void poseForLowBasketScoring() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void poseForHighBasketScoring() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void scoringBasket() {
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

    public void poseForLowSpecimenScoringForward() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void poseForLowSpecimenScoringBackward() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void poseForHighSpecimenScoringForward() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void poseForHighSpecimenScoringBackward() {
        Schedule.addTask(() -> {
            deposit.armMainServo.setPosition(Constants.ARM_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);

        Schedule.addTask(() -> {
            deposit.linearSlideMainMotor.setPosition(Constants.LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION);
        }, Schedule.RUN_INSTANTLY);
    }

    public void scoringSpecimen() {
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
