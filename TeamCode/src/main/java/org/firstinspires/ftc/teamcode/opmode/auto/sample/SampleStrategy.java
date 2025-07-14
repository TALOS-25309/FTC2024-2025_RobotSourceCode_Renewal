package org.firstinspires.ftc.teamcode.opmode.auto.sample;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.deposit.DepositState;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.concurrent.atomic.AtomicReference;

import static org.firstinspires.ftc.teamcode.opmode.auto.sample.Constants.*;

public class SampleStrategy {
    private final Drive drive;
    private final Intake intake;
    private final Deposit deposit;

    private boolean isRun = false;

    private void run() {
        isRun = true;
    }

    private void end() {
        isRun = false;
    }

    public boolean isEnd() {
        return !isRun;
    }

    public SampleStrategy(Drive drive, Intake intake, Deposit deposit) {
        this.drive = drive;
        this.intake = intake;
        this.deposit = deposit;
        Pose2d initialPose = new Pose2d(POSE_INITIAL_X, POSE_INITIAL_Y, Math.toRadians(STANDARD_HEADING));
        this.drive.drive().setPoseEstimate(initialPose);
    }

    public void startSample() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d sampleDepositPose = new Pose2d(
                POSE_SAMPLE_DEPOSIT_X,
                POSE_SAMPLE_DEPOSIT_Y,
                Math.toRadians(POSE_SAMPLE_DEPOSIT_ORIENTATION)
        );
        TrajectorySequence depositTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    deposit.command().poseForHighBasketScoring();
                })
                .lineToLinearHeading(sampleDepositPose)
                .waitSeconds(DELAY_FOR_START_BASKET_LINEAR_MOVE)
                .addTemporalMarker(() -> {
                    end();
                    deposit.command().scoringBasket();
                })
                .build();
        drive.command().followTrajectory(depositTrajectory);
    }

    public void firstSample() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePickupPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X_1,
                POSE_SAMPLE_PICKUP_Y_1,
                Math.toRadians(POSE_SAMPLE_PICKUP_DIRECTION_1)
        );
        TrajectorySequence pickupTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY(LINEAR_SAMPLE_PICKUP_X_1, LINEAR_SAMPLE_PICKUP_Y_1);
                })
                .lineToLinearHeading(samplePickupPose)
                .addTemporalMarker(() -> {
                    detectionLoop(false,3);
                    transferAndScoring();
                })
                .build();
        drive.command().followTrajectory(pickupTrajectory);
    }

    public void secondSample() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePickupPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X_2,
                POSE_SAMPLE_PICKUP_Y_2,
                Math.toRadians(POSE_SAMPLE_PICKUP_DIRECTION_2)
        );
        TrajectorySequence pickupTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY(LINEAR_SAMPLE_PICKUP_X_2, LINEAR_SAMPLE_PICKUP_Y_2);
                })
                .lineToLinearHeading(samplePickupPose)
                .addTemporalMarker(() -> {
                    detectionLoop(false,3);
                    transferAndScoring();
                })
                .build();
        drive.command().followTrajectory(pickupTrajectory);
    }

    public void thirdSample() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePickupPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X_3,
                POSE_SAMPLE_PICKUP_Y_3,
                Math.toRadians(POSE_SAMPLE_PICKUP_DIRECTION_3)
        );
        Pose2d sampleDepositPose = new Pose2d(
                POSE_SAMPLE_DEPOSIT_X,
                POSE_SAMPLE_DEPOSIT_Y,
                Math.toRadians(POSE_SAMPLE_DEPOSIT_ORIENTATION)
        );

        AtomicReference<Boolean> scoring = new AtomicReference<>(false);

        TrajectorySequence pickupTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY(LINEAR_SAMPLE_PICKUP_X_3, LINEAR_SAMPLE_PICKUP_Y_3);
                })
                .lineToLinearHeading(samplePickupPose)
                .addTemporalMarker(() -> {
                    detectionLoop(true,1);
                    Schedule.addConditionalTask(() -> {
                        scoring.set(true);
                    }, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
                })
                .build();

        TrajectorySequence scoringTrajectory = drive.drive().trajectorySequenceBuilder(pickupTrajectory.end())
                .addTemporalMarker(this::transferAndScoring)
                .lineToLinearHeading(sampleDepositPose)
                .build();

        drive.command().followTrajectory(pickupTrajectory);
        Schedule.addConditionalTask(() -> {
            drive.command().followTrajectory(scoringTrajectory);
        }, Schedule.RUN_INSTANTLY, scoring::get);
    }

    private void detectionLoop(boolean cautious, int rep) {
        intake.command().automaticTargetForYellowSample(cautious);
        Schedule.addConditionalTask(() -> {
            if(intake.state() != IntakeState.READY_FOR_TRANSFER) {
                if(rep <= 1) {
                    intake.command().readyForTransfer();
                } else {
                    detectionLoop(cautious, rep - 1);
                }
            }
        }, Schedule.RUN_INSTANTLY,
                () -> !Global.DETECTING
        );
    }

    private void transferAndScoring() {
        Schedule.addConditionalTask(() -> {
            intake.command().transfer();
            deposit.command().transfer();
            Schedule.addConditionalTask(() -> {
                deposit.command().poseForHighBasketScoring();
                Schedule.addTask(() -> {
                    end();
                    deposit.command().scoringBasket();
                }, DELAY_FOR_BASKET_LINEAR_MOVE);
            }, Schedule.RUN_INSTANTLY, () -> deposit.state() == DepositState.LOAD_SAMPLE);
        }, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
    }
}
