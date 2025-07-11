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

    public static double POSE_INITIAL_X = -ROBOT_X_OFFSET;
    public static double POSE_INITIAL_Y = -3 * TILE_SIZE + ROBOT_Y_OFFSET;

    public static double POSE_SAMPLE_DEPOSIT_X = -2.2 * TILE_SIZE;
    public static double POSE_SAMPLE_DEPOSIT_Y = -2.2 * TILE_SIZE;
    public static double POSE_SAMPLE_DEPOSIT_ORIENTATION = 45;
}