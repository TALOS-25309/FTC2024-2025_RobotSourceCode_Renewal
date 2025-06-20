package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Config(value = "DriveConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    static String WHEEL_NAME_LEFT_FRONT = "leftFront";
    static String WHEEL_NAME_LEFT_REAR = "leftBack";
    static String WHEEL_NAME_RIGHT_FRONT = "rightFront";
    static String WHEEL_NAME_RIGHT_REAR = "rightBack";

    static Boolean WHEEL_REVERSE_LEFT_FRONT = false;
    static Boolean WHEEL_REVERSE_LEFT_REAR = true;
    static Boolean WHEEL_REVERSE_RIGHT_FRONT = true;
    static Boolean WHEEL_REVERSE_RIGHT_REAR = false;

    static String ODOMETRY_NAME_LEFT = "leftOdo";
    static String ODOMETRY_NAME_RIGHT = "rightOdo";
    static String ODOMETRY_NAME_FRONT = "frontOdo";

    static Boolean ODOMETRY_REVERSE_LEFT = false;
    static Boolean ODOMETRY_REVERSE_RIGHT = true;
    static Boolean ODOMETRY_REVERSE_FRONT = false;

    // Motor Speed
    static double MOTOR_SPEED = 1.0;
    static double MOTOR_SPEED_SLOW = 0.5;

    // X, Y, Omega Weight (Speed)
    static double VX_WEIGHT = 1.0;
    static double VY_WEIGHT = 1.0;
    static double OMEGA_WEIGHT = 1.0;

    // Localization
    static boolean USING_LOCALIZATION_BASED_DRIVE = false;
    static Pose2d AUTO_INITIAL_POSITION = new Pose2d(0, 0, 0);
    static Pose2d TELE_INITIAL_POSITION = new Pose2d(0, 0, 0);
    static Pose2d INITIAL_POSITION = AUTO_INITIAL_POSITION;

    // Trajectory Values
    static Vector2d BASKET_POSITION = new Vector2d(-72, -72);
    static double BASKET_RADIUS = 6;
    static Vector2d SPECIMEN_POSITION = new Vector2d(0, 0);

    static double directionSign = 1.0;

    // Synchronization Function for RoadRunner DriveConstants
    static void SyncDriveConstantsWithRoadRunner() {
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