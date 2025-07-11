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

        Pose2d initialPose = new Pose2d(POSE_INITIAL_X, POSE_INITIAL_Y, Math.toRadians(STANDARD_HEADING));
        Pose2d targetPose = new Pose2d(
                POSE_SAMPLE_DEPOSIT_X,
                POSE_SAMPLE_DEPOSIT_Y,
                Math.toRadians(POSE_SAMPLE_DEPOSIT_ORIENTATION)
        );
        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                .strafeToLinearHeading(targetPose.position, targetPose.heading)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    public static Vector2d vec(Pose2d pose) {
        return new Vector2d(pose.position.x, pose.position.y);
    }
}