package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

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
        Pose2d initialPose = new Pose2d(Constants.POSE_INITIAL_X, Constants.POSE_INITIAL_Y, Math.toRadians(Constants.standardHeading));
        this.drive.drive().setPoseEstimate(initialPose);
    }

    public void scoreSpecimenAndPickupSample() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePickUpPose = new Pose2d(
                Constants.POSE_SAMPLE_PICKUP_X,
                Constants.POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(Constants.standardHeading)
        );
        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addDisplacementMarker(() -> {
                    //deposit.command().poseForHighSpecimenScoringForward();
                })
                .lineToLinearHeading(samplePickUpPose)
                .addDisplacementMarker(() -> {
                    //deposit.command().scoringSpecimen();
                })
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, 30);
                })
                .waitSeconds(Constants.DELAY_FOR_END_SCORE_SPECIMEN_AND_PICK_UP_SAMPLE)
                .setConstraints(
                        SampleMecanumDrive.getVelocityConstraint(
                                Constants.STANDARD_VELOCITY,
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
            if(intake.state() == IntakeState.READY_TO_PICKUP) {
                intake.command().compactReady();
                isPickupSuccessful = false;
            } else {
                isPickupSuccessful = true;
            }        }, Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK, () -> intake.state() == IntakeState.READY_FOR_TRANSFER
                || intake.state() == IntakeState.READY_TO_PICKUP
        );
    }

    public void getSpecimen() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d specimenReadyPose = new Pose2d(
                Constants.POSE_SPECIMEN_READY_X,
                Constants.POSE_SPECIMEN_READY_Y,
                Math.toRadians(Constants.standardHeading)
        );
        Pose2d specimenPose = new Pose2d(
                Constants.POSE_SPECIMEN_X,
                Constants.POSE_SPECIMEN_Y,
                Math.toRadians(Constants.standardHeading)
        );
        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addDisplacementMarker(() -> {
                    //deposit.command().poseForSpecimenPickup();
                })
                .lineToLinearHeading(specimenReadyPose)
                .strafeTo(specimenPose.vec(),
                    SampleMecanumDrive.getVelocityConstraint(
                        Constants.SPECIMEN_VELOCITY,
                        DriveConstants.MAX_ANG_VEL,
                        DriveConstants.TRACK_WIDTH
                    ),
                    SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .addDisplacementMarker(() -> {
                    //deposit.command().pickupSpecimen();
                    if(isPickupSuccessful) {
                        intake.command().drop();
                    }
                })
                .waitSeconds(Constants.DELAY_FOR_GET_SPECIMEN)
                .setConstraints(
                        SampleMecanumDrive.getVelocityConstraint(
                                Constants.STANDARD_VELOCITY,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH
                        ),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        drive.command().followTrajectory(trajectory);
    }

    public void pushThreeSampleToObservationZone() {
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d samplePushPose = new Pose2d(
                Constants.PUSH_POSE_SAMPLE_PUSH_X,
                Constants.PUSH_POSE_SAMPLE_PUSH_Y,
                Math.toRadians(Constants.PUSH_POSE_DIRECTION_SAMPLE_1)
        );
        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                // Move to the initial position for pushing samples
                .lineToLinearHeading(samplePushPose)

                // Sample 1
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_SAMPLE_1);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().automaticTargetForAllianceSample();
                })
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, 0);
                })
                .turn(Math.toRadians(
                        Constants.PUSH_POSE_DIRECTION_OBSERVATION_ZONE
                                - Constants.PUSH_POSE_DIRECTION_SAMPLE_1
                        )
                )
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_OBSERVATION_ZONE);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().compactReady();
                })

                // Sample 2
                .turn(Math.toRadians(
                        Constants.PUSH_POSE_DIRECTION_SAMPLE_2
                                - Constants.PUSH_POSE_DIRECTION_OBSERVATION_ZONE
                        )
                )
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_SAMPLE_2);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().automaticTargetForAllianceSample();
                })
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, 0);
                })
                .turn(Math.toRadians(
                        Constants.PUSH_POSE_DIRECTION_OBSERVATION_ZONE
                                - Constants.PUSH_POSE_DIRECTION_SAMPLE_2
                        )
                )
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_OBSERVATION_ZONE);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().compactReady();
                })

                // Sample 3
                .turn(Math.toRadians(
                        Constants.PUSH_POSE_DIRECTION_SAMPLE_3
                                - Constants.PUSH_POSE_DIRECTION_OBSERVATION_ZONE
                        )
                )
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_SAMPLE_3);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().automaticTargetForAllianceSample();
                })
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, 0);
                })
                .turn(Math.toRadians(
                        Constants.PUSH_POSE_DIRECTION_OBSERVATION_ZONE
                                - Constants.PUSH_POSE_DIRECTION_SAMPLE_3
                        )
                )
                .addDisplacementMarker(() -> {
                    //intake.command().movePositionXY(0, Constants.PUSH_DISTANCE_OBSERVATION_ZONE);
                })
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .addDisplacementMarker(() -> {
                    //intake.command().compactReady();
                })
                .setConstraints(
                        SampleMecanumDrive.getVelocityConstraint(
                                Constants.STANDARD_VELOCITY,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH
                        ),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        drive.command().followTrajectory(trajectory);
    }
}
