package com.visualizer.meepmeepvisualizer;

import com.acmerobotics.roadrunner.Pose2d;

public class Constants {
    public static final double STANDARD_HEADING = 90;
    public static final double LEFT = 180;
    public static final double RIGHT = 0;
    public static final double BACK = 270;

    public static final double TILE_MM_SIZE = 599; // Width of the field in mm
    public static final double TILE_SIZE = 24; // Tile size of road runner unit
    public static final double ROBOT_X_OFFSET = (TILE_SIZE * 160.0) / TILE_MM_SIZE;
    public static final double ROBOT_Y_OFFSET = (TILE_SIZE * 172.5) / TILE_MM_SIZE;
    public static final double SUBMERSIBLE_Y_POSITION = 1245 * TILE_SIZE / TILE_MM_SIZE - (TILE_SIZE * 3);


    public static final double UNIT_CM = 10 * TILE_SIZE / TILE_MM_SIZE;

    public static double POSE_INITIAL_X = ROBOT_X_OFFSET;
    public static double POSE_INITIAL_Y = -3 * TILE_SIZE + ROBOT_Y_OFFSET;
    public static double POSE_SAMPLE_PICKUP_X = 0;
    public static double POSE_SAMPLE_PICKUP_Y = SUBMERSIBLE_Y_POSITION - ROBOT_Y_OFFSET;
    public static double POSE_SPECIMEN_READY_X = 32;
    public static double POSE_SPECIMEN_READY_Y = -60;
    public static double POSE_SPECIMEN_X = 32;
    public static double POSE_SPECIMEN_Y = -3 * TILE_SIZE + ROBOT_Y_OFFSET;

    public static double POSE_LAST_SAMPLE_X = 38;
    public static double POSE_LAST_SAMPLE_Y = -36;

    public static double POSE_OBSERVATION_X = 48;
    public static double POSE_OBSERVATION_Y = -58;
    public static double SAMPLE_DIRECTION_1 = STANDARD_HEADING;
    public static double SAMPLE_DIRECTION_2 = 70;
    public static double SAMPLE_DIRECTION_3 = 25;
    public static double SAMPLE_DIRECTION_LAST = -50;
    public static double SAMPLE_DISTANCE_1 = 65; // MAX
    public static double SAMPLE_DISTANCE_2 = 55;
    public static double SAMPLE_DISTANCE_3 = 60;
    public static double SAMPLE_DISTANCE_LAST = 100; // MAX

    public static double STANDARD_VELOCITY = 60;
    public static double SPECIMEN_VELOCITY = 10;

    public static double SAMPLE_MOVE_ROBOT_POSITION_OFFSET = 5;

    public static double POSE_SAMPLE_SIDE_X = 1.5 * TILE_SIZE;
    public static double POSE_SAMPLE_START_Y = -0.5 * TILE_SIZE;
    public static double POSE_SAMPLE_END_Y = -2 * TILE_SIZE;
    public static double POSE_SAMPLE_1_X = 1.9 * TILE_SIZE;
    public static double POSE_SAMPLE_2_X = 2.3 * TILE_SIZE;
    public static double POSE_SAMPLE_3_X = 3 * TILE_SIZE - 1.2 * ROBOT_X_OFFSET;

    // Delays for various operations
    public static double DELAY_FOR_SAMPLE_DETECTION_CHECK = 1.0;
    public static double DELAY_FOR_LINEAR_SLIDE_MOVEMENT = 0.7;
    public static double DELAY_FOR_END_SCORE_SPECIMEN_AND_PICK_UP_SAMPLE = 1.0;
    public static double DELAY_FOR_GET_SPECIMEN = 1.0;
    public static double DELAY_FOR_DROP_SAMPLE = 0.5;
}