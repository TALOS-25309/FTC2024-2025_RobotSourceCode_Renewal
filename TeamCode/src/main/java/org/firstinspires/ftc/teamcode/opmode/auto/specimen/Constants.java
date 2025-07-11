package org.firstinspires.ftc.teamcode.opmode.auto.specimen;
import com.acmerobotics.dashboard.config.Config;
import static org.firstinspires.ftc.teamcode.opmode.auto.GlobalConstants.*;

@Config("AutoConstants-Specimen")
public class Constants {
    public static final double STANDARD_HEADING = 90;
    public static final double LEFT = 180;
    public static final double RIGHT = 0;
    public static final double BACK = 270;

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

    // Delays for various operations
    public static double DELAY_FOR_SAMPLE_DETECTION_CHECK = 1.0;
    public static double DELAY_FOR_LINEAR_SLIDE_MOVEMENT = 0.7;
    public static double DELAY_FOR_END_SCORE_SPECIMEN_AND_PICK_UP_SAMPLE = 1.0;
    public static double DELAY_FOR_GET_SPECIMEN = 1.0;
    public static double DELAY_FOR_DROP_SAMPLE = 0.5;
}