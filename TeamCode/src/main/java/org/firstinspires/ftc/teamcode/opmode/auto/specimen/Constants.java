package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@Config("AutoConstants-Specimen")
public class Constants {
    public static double standardHeading = 90;

    public static double POSE_INITIAL_X = 8;
    public static double POSE_INITIAL_Y = -64;
    public static double POSE_SAMPLE_PICKUP_X = 0;
    public static double POSE_SAMPLE_PICKUP_Y = -28;
    public static double POSE_SPECIMEN_READY_X = 32;
    public static double POSE_SPECIMEN_READY_Y = -60;
    public static double POSE_SPECIMEN_X = 32;
    public static double POSE_SPECIMEN_Y = -64;

    public static double PUSH_POSE_DIRECTION_SAMPLE_1 = 60;
    public static double PUSH_POSE_DIRECTION_SAMPLE_2 = 45;
    public static double PUSH_POSE_DIRECTION_SAMPLE_3 = 30;
    public static double PUSH_POSE_DIRECTION_OBSERVATION_ZONE = -45;

    public static double PUSH_DISTANCE_SAMPLE_1 = 30;
    public static double PUSH_DISTANCE_SAMPLE_2 = 30;
    public static double PUSH_DISTANCE_SAMPLE_3 = 30;
    public static double PUSH_DISTANCE_OBSERVATION_ZONE = 30;

    public static double PUSH_POSE_SAMPLE_PUSH = 30;
    public static double PUSH_POSE_SAMPLE_PUSH_X = 38;
    public static double PUSH_POSE_SAMPLE_PUSH_Y = -48;

    public static double STANDARD_VELOCITY = 40;
    public static double SPECIMEN_VELOCITY = 10;

    // Delays for various operations
    public static double DELAY_FOR_SAMPLE_DETECTION_CHECK = 1.0;
    public static double DELAY_FOR_LINEAR_SLIDE_MOVEMENT = 1.0;
    public static double DELAY_FOR_END_SCORE_SPECIMEN_AND_PICK_UP_SAMPLE = 1.0;
    public static double DELAY_FOR_GET_SPECIMEN = 1.0;
}