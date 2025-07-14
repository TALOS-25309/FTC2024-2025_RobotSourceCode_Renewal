package com.visualizer.meepmeepvisualizer;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import static com.visualizer.meepmeepvisualizer.Constants.*;

public class MeepMeepVisualizer {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(30, 60, Math.toRadians(180), Math.toRadians(180), 9.76)
                .build();

        Pose2d initialPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        /*Pose2d targetPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X_o,
                POSE_SAMPLE_PICKUP_Y_o,
                Math.toRadians(POSE_SAMPLE_PICKUP_DIRECTION_o)
        );*/

        Pose2d currentPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );
        Pose2d firstSidePose = new Pose2d(
                POSE_SAMPLE_SIDE_X,
                POSE_SAMPLE_SIDE_Y,
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
        Pose2d depositPose = new Pose2d(
                POSE_SAMPLE_PICKUP_X,
                POSE_SAMPLE_PICKUP_Y,
                Math.toRadians(STANDARD_HEADING)
        );

        myBot.runAction(myBot.getDrive().actionBuilder(currentPose)
                .setReversed(true)
                .splineToLinearHeading(firstSidePose, Math.toRadians(STANDARD_HEADING))
                .splineToLinearHeading(secondSidePose, Math.toRadians(STANDARD_HEADING))
                .strafeToConstantHeading(firstSampleStartPose.position)
                .strafeToConstantHeading(firstSampleEndPose.position)
                .splineToLinearHeading(secondSampleStartPose, Math.toRadians(RIGHT))
                .strafeToConstantHeading(secondSampleEndPose.position)
                .splineToLinearHeading(thirdSampleStartPose, Math.toRadians(RIGHT))
                .strafeToConstantHeading(thirdSampleEndPose.position)
                .splineToLinearHeading(specimenReadyPose, Math.toRadians(LEFT))
                .strafeTo(specimenPose.position)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    public static Vector2d vec(Pose2d pose) {
        return new Vector2d(pose.position.x, pose.position.y);
    }
}