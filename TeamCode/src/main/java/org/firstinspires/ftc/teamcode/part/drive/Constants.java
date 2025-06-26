package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Config(value = "DriveConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String WHEEL_NAME_LEFT_FRONT = "leftFront";
    public static final String WHEEL_NAME_LEFT_REAR = "leftBack";
    public static final String WHEEL_NAME_RIGHT_FRONT = "rightFront";
    public static final String WHEEL_NAME_RIGHT_REAR = "rightBack";

    public static final Boolean WHEEL_REVERSE_LEFT_FRONT = false;
    public static final Boolean WHEEL_REVERSE_LEFT_REAR = true;
    public static final Boolean WHEEL_REVERSE_RIGHT_FRONT = true;
    public static final Boolean WHEEL_REVERSE_RIGHT_REAR = false;

    public static final String ODOMETRY_NAME_LEFT = "leftOdo";
    public static final String ODOMETRY_NAME_RIGHT = "rightOdo";
    public static final String ODOMETRY_NAME_FRONT = "frontOdo";

    public static final Boolean ODOMETRY_REVERSE_LEFT = false;
    public static final Boolean ODOMETRY_REVERSE_RIGHT = true;
    public static final Boolean ODOMETRY_REVERSE_FRONT = false;

    // Motor Speed
    public static double MOTOR_SPEED = 1.0;
    public static double MOTOR_SPEED_SLOW = 0.5;

    // X, Y, Omega Weight (Speed)
    public static double VX_WEIGHT = 1.0;
    public static double VY_WEIGHT = 1.0;
    public static double OMEGA_WEIGHT = 1.0;

    // Localization
    public static boolean USING_LOCALIZATION_BASED_DRIVE = false;
    public static Pose2d AUTO_INITIAL_POSITION = new Pose2d(0, 0, 0);
    public static Pose2d TELE_INITIAL_POSITION = new Pose2d(0, 0, 0);
    public static Pose2d INITIAL_POSITION = AUTO_INITIAL_POSITION;

    // Trajectory Values
    public static Vector2d BASKET_POSITION = new Vector2d(-72, -72);
    public static double BASKET_RADIUS = 6;
    public static Vector2d SPECIMEN_POSITION = new Vector2d(0, 0);

    public static double directionSign = 1.0;

    // Synchronization Function for RoadRunner DriveConstants
    public static void SyncDriveConstantsWithRoadRunner() {
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_NAME_LEFT_FRONT = WHEEL_NAME_LEFT_FRONT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_NAME_LEFT_REAR = WHEEL_NAME_LEFT_REAR;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_NAME_RIGHT_FRONT = WHEEL_NAME_RIGHT_FRONT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_NAME_RIGHT_REAR = WHEEL_NAME_RIGHT_REAR;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_REVERSE_LEFT_FRONT = WHEEL_REVERSE_LEFT_FRONT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_REVERSE_LEFT_REAR = WHEEL_REVERSE_LEFT_REAR;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_REVERSE_RIGHT_FRONT = WHEEL_REVERSE_RIGHT_FRONT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.WHEEL_REVERSE_RIGHT_REAR = WHEEL_REVERSE_RIGHT_REAR;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_NAME_LEFT = ODOMETRY_NAME_LEFT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_NAME_RIGHT = ODOMETRY_NAME_RIGHT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_NAME_FRONT = ODOMETRY_NAME_FRONT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_REVERSE_LEFT = ODOMETRY_REVERSE_LEFT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_REVERSE_RIGHT = ODOMETRY_REVERSE_RIGHT;
        org.firstinspires.ftc.teamcode.drive.DriveConstants.ODOMETRY_REVERSE_FRONT = ODOMETRY_REVERSE_FRONT;
    }
}