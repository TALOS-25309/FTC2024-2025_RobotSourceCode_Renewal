package org.firstinspires.ftc.teamcode.opmode.auto.specimen;
import com.acmerobotics.dashboard.config.Config;
import static org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants.*;

@Config("AutoConstants-Specimen")
public class Constants {
    public static final double STANDARD_HEADING = 90;
    public static final double LEFT = 180;
    public static final double RIGHT = 0;
    public static final double BACK = 270;

    public static double POSE_INITIAL_X = ROBOT_X_OFFSET + UNIT_CM;
    public static double POSE_INITIAL_Y = -3 * TILE_SIZE + ROBOT_Y_OFFSET;

    public static double POSE_START_SPECIMEN_DEPOSIT_X = 0;
    public static double POSE_START_SPECIMEN_DEPOSIT_Y = SUBMERSIBLE_Y_POSITION - ROBOT_Y_OFFSET;

    public static double POSE_SPECIMEN_DEPOSIT_X = 0;
    public static double POSE_SPECIMEN_DEPOSIT_Y = -32.5;

    public static double POSE_LAST_SAMPLE_X = 38;
    public static double POSE_LAST_SAMPLE_Y = -40;

    public static double POSE_OBSERVATION_X = 48;
    public static double POSE_OBSERVATION_Y = -58;
    public static double POSE_SAMPLE_DIRECTION_1 = 90;
    public static double POSE_SAMPLE_DIRECTION_2 = 70;
    public static double POSE_SAMPLE_DIRECTION_3 = 25;
    public static double POSE_SAMPLE_DIRECTION_LAST = -50;
    public static double LINEAR_SAMPLE_X_1 = -2;
    public static double LINEAR_SAMPLE_X_2 = 0;
    public static double LINEAR_SAMPLE_X_3 = -2;
    public static double LINEAR_SAMPLE_X_LAST = 0;
    public static double LINEAR_SAMPLE_Y_1 = 60;
    public static double LINEAR_SAMPLE_Y_2 = 68;
    public static double LINEAR_SAMPLE_Y_3 = 57;
    public static double LINEAR_SAMPLE_Y_LAST = 60;

    public static double POSE_SPECIMEN_PICKUP_X = 15;
    public static double POSE_SPECIMEN_PICKUP_Y = -42.5;
    public static double POSE_SPECIMEN_PICKUP_DIRECTION = -45;
    public static double LINEAR_SPECIMEN_PICKUP_X = 0;
    public static double LINEAR_SPECIMEN_PICKUP_Y = 60;

    // Delays for various operations
    public static double DELAY_FOR_START_SAMPLE_LINEAR_MOVE = 0.25;
    public static double DELAY_FOR_START_SAMPLE_END = 0.2;
    public static double DELAY_FOR_FIRST_SAMPLE_LINEAR_MOVE = 1.0;
    public static double DELAY_FOR_SECOND_SAMPLE_LINEAR_MOVE = 0.5;
    public static double DELAY_FOR_LAST_SAMPLE_LINEAR_MOVE = 0.1;
    public static double DELAY_FOR_DROP_LAST_SAMPLE_LINEAR_MOVE = 0.5;
    public static double DELAY_FOR_SPECIMEN_LINEAR_MOVE = 0.3;
    public static double DELAY_FOR_SCORING_SPECIMEN = 0.5;
    public static double DELAY_FOR_SCORING_SPECIMEN_MOVE_ROBOT = 1.0;
    public static double DELAY_AFTER_DROP_SECOND_SAMPLE = 1.0;
    public static double DELAY_PICKUP = 0.5;
}