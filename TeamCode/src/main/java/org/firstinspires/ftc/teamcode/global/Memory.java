package org.firstinspires.ftc.teamcode.global;

import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.deposit.DepositState;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;

public class Memory {
    public enum EndState {
        READY,
        PRE_TRANSFER_SAMPLE,
        TRANSFER_SAMPLE,
        TRANSFER_SPECIMEN
    }

    public static EndState END_STATE = EndState.READY;

    public static void startWithEndState(Intake intake, Deposit deposit) {
        switch (END_STATE) {
            case TRANSFER_SAMPLE:
                Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
                deposit.command().poseForHighBasketScoring();
                break;
            case TRANSFER_SPECIMEN:
                Global.TRANSFER_TYPE = Global.TransferType.SPECIMEN;
                deposit.command().poseForHighSpecimenScoringBackward();
                break;
            default:
                break;
        }
    }

    public static void saveEndState(Intake intake, Deposit deposit) {
        if (intake.state() == IntakeState.READY_FOR_TRANSFER) {
            if (Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
                END_STATE = EndState.PRE_TRANSFER_SAMPLE;
            }
        } else if (deposit.state() == DepositState.LOAD_SAMPLE
            || deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
            END_STATE = EndState.TRANSFER_SAMPLE;
        } else if (deposit.state() == DepositState.LOAD_SPECIMEN
            || deposit.state() == DepositState.READY_FOR_DEPOSIT_SPECIMEN) {
            END_STATE = EndState.TRANSFER_SPECIMEN;
        } else {
            END_STATE = EndState.READY;
        }
    }
}
