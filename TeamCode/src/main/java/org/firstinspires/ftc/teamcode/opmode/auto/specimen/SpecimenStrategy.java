package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.opmode.auto.specimen.Constants.*;
import static org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants.*;

import java.util.concurrent.atomic.AtomicInteger;

public class SpecimenStrategy {
    private final Drive drive;
    private final Intake intake;
    private final Deposit deposit;

    public boolean isPickupSuccessful = false;
    public boolean isPickingUpSample = false;

    public SpecimenStrategy(Drive drive, Intake intake, Deposit deposit) {
        this.drive = drive;
        this.intake = intake;
        this.deposit = deposit;
        Pose2d initialPose = new Pose2d(POSE_INITIAL_X, POSE_INITIAL_Y, Math.toRadians(STANDARD_HEADING));
        this.drive.drive().setPoseEstimate(initialPose);
    }

    public void scoreSpecimenAndPickupSample() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePickUpPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    deposit.command().poseForHighSpecimenScoringForward();
                })
                .lineToLinearHeading(samplePickUpPose)
                .addTemporalMarker(() -> {
                    deposit.command().scoringSpecimen();
                })
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY(0, 0);
                })
                .waitSeconds(DELAY_FOR_END_SCORE_SPECIMEN_AND_PICK_UP_SAMPLE)
                .setConstraints(
                        SampleMecanumDrive.getVelocityConstraint(
                                STANDARD_VELOCITY,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH
                        ),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        drive.command().followTrajectory(trajectory);
    }

    public void detectSampleAndPickUp() {
        isPickingUpSample = true;
        intake.command().automaticTargetForAllianceSample();
        Schedule.addConditionalTask(() -> {
            isPickingUpSample = false;
            if(intake.state() == IntakeState.READY_FOR_PICKUP) {
                intake.command().compactReady();
                isPickupSuccessful = false;
            } else {
                isPickupSuccessful = true;
            }        }, DELAY_FOR_SAMPLE_DETECTION_CHECK, () -> intake.state() == IntakeState.READY_FOR_TRANSFER
                || intake.state() == IntakeState.READY_FOR_PICKUP
        );
    }

    public void getSpecimen() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d specimenReadyPose = new Pose2d(
                POSE_SPECIMEN_READY_X,
                POSE_SPECIMEN_READY_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d specimenPose = new Pose2d(
                POSE_SPECIMEN_X,
                POSE_SPECIMEN_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addDisplacementMarker(() -> {
                    deposit.command().poseForSpecimenPickup();
                })
                .lineToLinearHeading(specimenReadyPose)
                .strafeTo(specimenPose.vec(),
                    SampleMecanumDrive.getVelocityConstraint(
                        SPECIMEN_VELOCITY,
                        DriveConstants.MAX_ANG_VEL,
                        DriveConstants.TRACK_WIDTH
                    ),
                    SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .addDisplacementMarker(() -> {
                    deposit.command().pickupSpecimen();
                    if(isPickupSuccessful) {
                        intake.command().drop();
                    }
                })
                .waitSeconds(DELAY_FOR_GET_SPECIMEN)
                .setConstraints(
                        SampleMecanumDrive.getVelocityConstraint(
                                STANDARD_VELOCITY,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH
                        ),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        drive.command().followTrajectory(trajectory);
    }

    public void moveThreeSampleToObservationZone() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d observationZonePose = new Pose2d(
                POSE_OBSERVATION_X,
                POSE_OBSERVATION_Y,
                Math.toRadians(SAMPLE_DIRECTION_1)
        );
        Pose2d lastSamplePose = new Pose2d(
                POSE_LAST_SAMPLE_X,
                POSE_LAST_SAMPLE_Y,
                Math.toRadians(SAMPLE_DIRECTION_3)
        );

        AtomicInteger level = new AtomicInteger(1);

        Trajectory startTrajectory = drive.drive().trajectoryBuilder(currentPose, true)
                .splineToLinearHeading(observationZonePose, Math.toRadians(RIGHT))
                .build();

        TrajectorySequence firstSampleTrajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTrajectory(startTrajectory)
                .waitSeconds(startTrajectory.duration())
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY(0, SAMPLE_DISTANCE_1);
                })
                .waitSeconds(DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addTemporalMarker(() -> {
                    detectionLoop(false, 3);
                    Schedule.addConditionalTask(level::getAndIncrement,
                            Schedule.RUN_INSTANTLY,
                            () -> intake.state() == IntakeState.READY_FOR_TRANSFER
                    );
                })
                .build();

        TrajectorySequence secondSampleTrajectory = drive.drive().trajectorySequenceBuilder(firstSampleTrajectory.end())
                .turn(Math.toRadians(SAMPLE_DIRECTION_2 - SAMPLE_DIRECTION_1))
                .addTemporalMarker(() -> {
                    intake.command().drop();
                })
                .waitSeconds(DELAY_FOR_DROP_SAMPLE)
                .addTemporalMarker(() -> {
                    intake.command().ready();
                    intake.command().movePositionXY( 0, SAMPLE_DISTANCE_2);
                })
                .forward(SAMPLE_MOVE_ROBOT_POSITION_OFFSET)
                .waitSeconds(DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addTemporalMarker(() -> {
                    detectionLoop(false, 3);
                    Schedule.addConditionalTask(level::getAndIncrement,
                            Schedule.RUN_INSTANTLY,
                            () -> intake.state() == IntakeState.READY_FOR_TRANSFER
                    );
                })
                .build();

        TrajectorySequence thirdSampleTrajectory = drive.drive().trajectorySequenceBuilder(secondSampleTrajectory.end())
                .back(SAMPLE_MOVE_ROBOT_POSITION_OFFSET)
                .turn(Math.toRadians(SAMPLE_DIRECTION_3 - SAMPLE_DIRECTION_2))
                .addTemporalMarker(() -> {
                    intake.command().drop();
                })
                .waitSeconds(DELAY_FOR_DROP_SAMPLE)
                .addTemporalMarker(() -> {
                    intake.command().ready();
                    intake.command().movePositionXY( 0, SAMPLE_DISTANCE_3);
                })
                .lineToLinearHeading(lastSamplePose)
                .waitSeconds(DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addTemporalMarker(() -> {
                    detectionLoop(true, 1);
                    Schedule.addConditionalTask(level::getAndIncrement,
                            Schedule.RUN_INSTANTLY,
                            () -> intake.state() == IntakeState.READY_FOR_TRANSFER
                    );
                })
                .build();

        TrajectorySequence endTrajectory = drive.drive().trajectorySequenceBuilder(thirdSampleTrajectory.end())
                .waitSeconds(DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addTemporalMarker(() -> {
                    intake.command().movePositionXY( 0, SAMPLE_DISTANCE_LAST);
                })
                .turn(Math.toRadians(SAMPLE_DIRECTION_LAST - SAMPLE_DIRECTION_3))
                .addTemporalMarker(() -> {
                    intake.command().discard();
                })
                .build();

        Schedule.addConditionalTask(() -> {
            drive.command().followTrajectory(firstSampleTrajectory);
        }, Schedule.RUN_INSTANTLY, () -> level.get() == 1);
        Schedule.addConditionalTask(() -> {
            drive.command().followTrajectory(secondSampleTrajectory);
        }, Schedule.RUN_INSTANTLY, () -> level.get() == 2);
        Schedule.addConditionalTask(() -> {
            drive.command().followTrajectory(thirdSampleTrajectory);
        }, Schedule.RUN_INSTANTLY, () -> level.get() == 3);
        Schedule.addConditionalTask(() -> {
            drive.command().followTrajectory(endTrajectory);
        }, Schedule.RUN_INSTANTLY, () -> level.get() == 4);
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
