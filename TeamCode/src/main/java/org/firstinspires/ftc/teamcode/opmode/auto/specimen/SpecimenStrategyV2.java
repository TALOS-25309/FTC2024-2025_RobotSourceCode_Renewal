package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants.*;
import static org.firstinspires.ftc.teamcode.opmode.auto.specimen.Constants.*;

import java.util.concurrent.atomic.AtomicInteger;

public class SpecimenStrategyV2 {
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

    public SpecimenStrategyV2(Drive drive, Intake intake, Deposit deposit) {
        this.drive = drive;
        this.intake = intake;
        this.deposit = deposit;
        Pose2d initialPose = new Pose2d(POSE_INITIAL_X, POSE_INITIAL_Y, Math.toRadians(STANDARD_HEADING));
        this.drive.drive().setPoseEstimate(initialPose);
    }

    public void startSpecimen() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d depositPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    deposit.command().poseForHighSpecimenScoringForward();
                })
                .lineToLinearHeading(depositPose)
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        end();
                        deposit.command().scoringSpecimen();
                    }, Schedule.RUN_INSTANTLY, deposit::isLinearSlideStretchPerfectly);
                })
                .build();

        drive.drive().followTrajectorySequence(trajectory);
    }

    public void moveSamples() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d firstSidePose = new Pose2d(
                POSE_SAMPLE_SIDE_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d secondSidePose = new Pose2d(
                POSE_SAMPLE_SIDE_X,
                POSE_SAMPLE_START_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d firstSampleStartPose = new Pose2d(
                POSE_SAMPLE_1_X,
                POSE_SAMPLE_START_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d firstSampleEndPose = new Pose2d(
                POSE_SAMPLE_1_X,
                POSE_SAMPLE_END_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d secondSampleStartPose = new Pose2d(
                POSE_SAMPLE_2_X,
                POSE_SAMPLE_START_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d secondSampleEndPose = new Pose2d(
                POSE_SAMPLE_2_X,
                POSE_SAMPLE_END_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d thirdSampleStartPose = new Pose2d(
                POSE_SAMPLE_3_X,
                POSE_SAMPLE_START_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d thirdSampleEndPose = new Pose2d(
                POSE_SAMPLE_3_X,
                POSE_SAMPLE_END_Y,
                Math.toRadians(STANDARD_HEADING)
        );
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
                .setReversed(true)
                .splineToLinearHeading(firstSidePose, Math.toRadians(STANDARD_HEADING))
                .splineToLinearHeading(secondSidePose, Math.toRadians(STANDARD_HEADING))
                .splineToLinearHeading(firstSampleStartPose, Math.toRadians(BACK))
                .splineToLinearHeading(firstSampleEndPose, Math.toRadians(BACK))
                .splineToLinearHeading(firstSampleStartPose, Math.toRadians(STANDARD_HEADING))
                .splineToLinearHeading(secondSampleStartPose, Math.toRadians(BACK))
                .splineToLinearHeading(secondSampleEndPose, Math.toRadians(BACK))
                .splineToLinearHeading(secondSampleStartPose, Math.toRadians(STANDARD_HEADING))
                .splineToLinearHeading(thirdSampleStartPose, Math.toRadians(BACK))
                .splineToLinearHeading(thirdSampleEndPose, Math.toRadians(BACK))
                .splineToLinearHeading(specimenReadyPose, Math.toRadians(BACK))
                .strafeTo(specimenPose.vec(),
                        SampleMecanumDrive.getVelocityConstraint(
                                SPECIMEN_VELOCITY,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH
                        ),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .addTemporalMarker(this::end)
                .build();
        drive.drive().followTrajectorySequence(trajectory);
    }

    public void scoreSpecimen() {
        run();
        Pose2d currentPose = drive.drive().getPoseEstimate();
        Pose2d depositPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );

        TrajectorySequence trajectory = drive.drive().trajectorySequenceBuilder(currentPose)
                .addTemporalMarker(() -> {
                    deposit.command().poseForHighSpecimenScoringForward();
                })
                .splineToLinearHeading(depositPose, Math.toRadians(STANDARD_HEADING))
                .addTemporalMarker(() -> {
                    Schedule.addConditionalTask(() -> {
                        end();
                        deposit.command().scoringSpecimen();
                    }, Schedule.RUN_INSTANTLY, deposit::isLinearSlideStretchPerfectly);
                })
                .build();
        drive.drive().followTrajectorySequence(trajectory);
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
