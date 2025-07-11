package org.firstinspires.ftc.teamcode.opmode.auto.sample;
import static org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants.*;

import com.acmerobotics.dashboard.config.Config;

@Config("AutoConstants-Sample")
public class Constants {
    public static final double STANDARD_HEADING = 90;
    public static final double LEFT = 180;
    public static final double RIGHT = 0;
    public static final double BACK = 270;

    public static double POSE_INITIAL_X = -2 * TILE_SIZE + ROBOT_X_OFFSET + UNIT_CM;
    public static double POSE_INITIAL_Y = -3 * TILE_SIZE + ROBOT_Y_OFFSET;

    public static double POSE_SAMPLE_DEPOSIT_X = -2.4 * TILE_SIZE;
    public static double POSE_SAMPLE_DEPOSIT_Y = -2.4 * TILE_SIZE;
    public static double POSE_SAMPLE_DEPOSIT_ORIENTATION = 45;

    public static final double BASKET_ROBOT_DISTANCE = 20.36467;

    public static double POSE_SAMPLE_PICKUP_DIRECTION_1 = 70.26984;
    public static double POSE_SAMPLE_PICKUP_DIRECTION_2 = 81.41295;
    public static double POSE_SAMPLE_PICKUP_DIRECTION_3 = 130.44356;

    public static double POSE_SAMPLE_PICKUP_X_1 = -60.14386;
    public static double POSE_SAMPLE_PICKUP_Y_1 = -54.97208;

    public static double POSE_SAMPLE_PICKUP_X_2 = -62.60025;
    public static double POSE_SAMPLE_PICKUP_Y_2 = -53.82515;

    public static double POSE_SAMPLE_PICKUP_X_3 = -55.79253;
    public static double POSE_SAMPLE_PICKUP_Y_3 = -45.68016;

    public static double LINEAR_SAMPLE_PICKUP_X_1 = -5;
    public static double LINEAR_SAMPLE_PICKUP_Y_1 = 50;
    public static double LINEAR_SAMPLE_PICKUP_X_2 = -5;
    public static double LINEAR_SAMPLE_PICKUP_Y_2 = 45;
    public static double LINEAR_SAMPLE_PICKUP_X_3 = -5;
    public static double LINEAR_SAMPLE_PICKUP_Y_3 = 35;

    public static double DELAY_FOR_BASKET_LINEAR_MOVE = 1.5;
    public static double DELAY_FOR_START_BASKET_LINEAR_MOVE = 0.3;
}