package com.visualizer.meepmeepvisualizer;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepVisualizer {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(30, 60, Math.toRadians(180), Math.toRadians(180), 9.76)
                .build();

        Pose2d robotInitialPose = new Pose2d(8, -64, Math.toRadians(90));

        myBot.runAction(myBot.getDrive().actionBuilder(robotInitialPose)
                .splineTo(vec(Constants.samplePushPose), Constants.samplePushPose.heading)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .turn(Constants.observationZoneDirection - Constants.sampleDirection1)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .turn(Constants.sampleDirection2 - Constants.observationZoneDirection)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .turn(Constants.observationZoneDirection - Constants.sampleDirection2)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .turn(Constants.sampleDirection3 - Constants.observationZoneDirection)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
                .waitSeconds(Constants.DELAY_FOR_SAMPLE_DETECTION_CHECK)
                .turn(Constants.observationZoneDirection - Constants.sampleDirection3)
                .waitSeconds(Constants.DELAY_FOR_LINEAR_SLIDE_MOVEMENT)
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