package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Config
class DriveConstants {
    private DriveConstants() {} // Prevent instantiation

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
}