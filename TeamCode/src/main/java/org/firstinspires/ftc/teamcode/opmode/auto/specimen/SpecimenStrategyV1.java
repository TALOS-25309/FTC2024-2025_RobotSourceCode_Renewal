package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.opmode.auto.specimen.Constants.*;

import java.util.concurrent.atomic.AtomicReference;

public class SpecimenStrategyV1 {
    private final Drive drive;
    private final Intake intake;
    private final Deposit deposit;
    private double DELTA = 2;
    private double LINEAR_DELTA = 3;

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

    public SpecimenStrategyV1(Drive drive, Intake intake, Deposit deposit) {
        this.drive = drive;
        this.intake = intake;
        this.deposit = deposit;
        Pose2d initialPose = new Pose2d(POSE_INITIAL_X, POSE_INITIAL_Y, Math.toRadians(STANDARD_HEADING));
        this.drive.drive().setPoseEstimate(initialPose);
    }

    public void startSpecimen() {
        run();

        Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d depositPose = new Pose2d(
                POSE_START_SPECIMEN_DEPOSIT_X,
                POSE_START_SPECIMEN_DEPOSIT_Y,
                Math.toRadians(STANDARD_HEADING)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        deposit.command().poseForHighSpecimenScoringForward();
                    }, Schedule.RUN_INSTANTLY);
                })
                .waitSeconds(DELAY_FOR_START_SAMPLE_LINEAR_MOVE)
                .lineToLinearHeading(depositPose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        deposit.command().scoringSpecimen();
                        Schedule.addTask(this::end, DELAY_FOR_START_SAMPLE_END);
                    }, Schedule.RUN_INSTANTLY);
                })
                .build();
        drive.command().followTrajectory(trajectory);
    }

    public void moveFirstSample() {
        Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d observationZonePose = new Pose2d(
                POSE_OBSERVATION_X,
                POSE_OBSERVATION_Y,
                Math.toRadians(POSE_SAMPLE_DIRECTION_1)
        );

        TrajectorySequence firstSampleTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .setReversed(true)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().movePositionXY(LINEAR_SAMPLE_X_1, LINEAR_SAMPLE_Y_1);
                    }, DELAY_FOR_FIRST_SAMPLE_LINEAR_MOVE);
                })
                .splineToLinearHeading(observationZonePose, Math.toRadians(RIGHT))
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        detectionLoop(false, 1);
                        Schedule.addConditionalTask(() -> {
                            end();
                            intake.command().drop();
                        }, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
                    }, Schedule.RUN_INSTANTLY, intake::isLinearSlideStretchPerfectly);
                })
                .build();
        drive.command().followTrajectory(firstSampleTrajectory);
    }

    public void moveSecondSample() {
        Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d observationZonePose = new Pose2d(
                POSE_OBSERVATION_X,
                POSE_OBSERVATION_Y,
                Math.toRadians(POSE_SAMPLE_DIRECTION_2)
        );

        TrajectorySequence secondSampleTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .lineToLinearHeading(observationZonePose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().movePositionXY(LINEAR_SAMPLE_X_2, LINEAR_SAMPLE_Y_2);
                        Schedule.addConditionalTask(() -> {
                            detectionLoop(false, 1);
                            Schedule.addConditionalTask(() -> {
                                intake.command().drop();
                                Schedule.addTask(this::end, DELAY_AFTER_DROP_SECOND_SAMPLE);
                            }, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
                        }, Schedule.RUN_INSTANTLY, intake::isLinearSlideStretchPerfectly);
                    }, DELAY_FOR_SECOND_SAMPLE_LINEAR_MOVE);
                })
                .build();
        drive.command().followTrajectory(secondSampleTrajectory);
    }

    public void moveThirdSample() {
        Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d pickupPose = new Pose2d(
                POSE_LAST_SAMPLE_X,
                POSE_LAST_SAMPLE_Y,
                Math.toRadians(POSE_SAMPLE_DIRECTION_3)
        );

        Pose2d dropPose = new Pose2d(
                POSE_LAST_SAMPLE_X,
                POSE_LAST_SAMPLE_Y,
                Math.toRadians(POSE_SAMPLE_DIRECTION_LAST)
        );

        AtomicReference<Boolean> isEnd = new AtomicReference<>(false);

        TrajectorySequence thirdSampleTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().movePositionXY(LINEAR_SAMPLE_X_3, LINEAR_SAMPLE_Y_3);
                    }, DELAY_FOR_LAST_SAMPLE_LINEAR_MOVE);
                })
                .lineToLinearHeading(pickupPose)
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        detectionLoop(true, 1);
                        Schedule.addConditionalTask(() -> {
                            isEnd.set(true);
                        }, Schedule.RUN_INSTANTLY, () -> intake.state() == IntakeState.READY_FOR_TRANSFER);
                    }, Schedule.RUN_INSTANTLY, intake::isLinearSlideStretchPerfectly);
                })
                .build();

        TrajectorySequence dropTrajectory = drive.drive().trajectorySequenceBuilder(thirdSampleTrajectory.end())
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().movePositionXY(LINEAR_SAMPLE_X_LAST, LINEAR_SAMPLE_Y_LAST);
                    }, DELAY_FOR_DROP_LAST_SAMPLE_LINEAR_MOVE);
                })
                .turn(dropPose.getHeading() - pickupPose.getHeading())
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        intake.command().discard();
                        deposit.command().rest();
                        end();
                    }, Schedule.RUN_INSTANTLY, deposit::isLinearSlideStretchPerfectly);
                })
                .build();

        drive.command().followTrajectory(thirdSampleTrajectory);
        Schedule.addConditionalTask(() -> {
            if (isEnd.get()) {
                drive.command().followTrajectory(dropTrajectory);
            }
        }, Schedule.RUN_INSTANTLY, isEnd::get);
    }

    public void pickupSpecimen() {
        Global.TRANSFER_TYPE = Global.TransferType.SPECIMEN;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d pickupPose = new Pose2d(
                POSE_SPECIMEN_PICKUP_X,
                POSE_SPECIMEN_PICKUP_Y,
                Math.toRadians(POSE_SPECIMEN_PICKUP_DIRECTION)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().setPosition(LINEAR_SPECIMEN_PICKUP_X, LINEAR_SPECIMEN_PICKUP_Y + LINEAR_DELTA, 0);
                        LINEAR_DELTA += 3;
                    }, DELAY_FOR_SPECIMEN_LINEAR_MOVE);
                })
                .splineToLinearHeading(pickupPose, Math.toRadians(POSE_SPECIMEN_PICKUP_DIRECTION))
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        Schedule.addTask(this::pickupLoop, DELAY_PICKUP);
                        Schedule.addConditionalTask(this::end, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
                    }, Schedule.RUN_INSTANTLY, intake::isLinearSlideStretchPerfectly);
                })
                .build();

        drive.command().followTrajectory(trajectory);
    }

    public void pickupSpecimenAfterMoveSample() {
        Global.TRANSFER_TYPE = Global.TransferType.SPECIMEN;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d pickupPose = new Pose2d(
                POSE_SPECIMEN_PICKUP_X,
                POSE_SPECIMEN_PICKUP_Y,
                Math.toRadians(POSE_SPECIMEN_PICKUP_DIRECTION)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        intake.command().setPosition(LINEAR_SPECIMEN_PICKUP_X, LINEAR_SPECIMEN_PICKUP_Y, 0);
                    }, DELAY_FOR_SPECIMEN_LINEAR_MOVE);
                })
                .lineToLinearHeading(pickupPose)
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        Schedule.addTask(this::pickupLoop, DELAY_PICKUP);
                        Schedule.addConditionalTask(this::end, Schedule.RUN_INSTANTLY, intake::isLinearSlideInside);
                    }, Schedule.RUN_INSTANTLY, intake::isLinearSlideStretchPerfectly);
                })
                .build();

        drive.command().followTrajectory(trajectory);
    }

    public void scoreSpecimen() {
        Global.TRANSFER_TYPE = Global.TransferType.SPECIMEN;
        run();

        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d depositPose = new Pose2d(
                POSE_SPECIMEN_DEPOSIT_X + DELTA,
                POSE_SPECIMEN_DEPOSIT_Y,
                Math.toRadians(BACK)
        );
        DELTA += 2;

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    intake.command().transfer();
                    deposit.command().transfer();
                    deposit.command().poseForHighSpecimenScoringBackward();
                })
                .waitSeconds(DELAY_FOR_SCORING_SPECIMEN_MOVE_ROBOT)
                .splineToLinearHeading(depositPose, Math.toRadians(STANDARD_HEADING))
                .addTemporalMarker(() -> {
                    Schedule.addTask(() -> {
                        deposit.command().scoringSpecimen();
                        end();
                    }, DELAY_FOR_SCORING_SPECIMEN);
                })
                .build();

        drive.command().followTrajectory(trajectory);
    }

    public void pickupLoop() {
        intake.command().pickup();
        Schedule.addConditionalTask(() -> {
                if(intake.state() == IntakeState.READY_FOR_PICKUP) {
                    pickupLoop();
                }
            }, Schedule.RUN_INSTANTLY,
            () -> intake.state() == IntakeState.READY_FOR_PICKUP
                    || intake.state() == IntakeState.READY_FOR_TRANSFER
        );
    }

    public void detectionLoop(boolean cautious, int rep) {
        intake.command().automaticTargetForAllianceSample(cautious);
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
}
