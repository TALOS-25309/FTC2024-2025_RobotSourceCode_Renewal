package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.dashboard.config.Config;

@Config
public class VisionConstants {
    private VisionConstants() {} // Prevent instantiation

    // Limelight settings
    public static final String LIMELIGHT_NAME = "limelight";
    //public static final double LIMELIGHT_HEIGHT = 28.4 - 1.9; // Height of the limelight in cm
    public static final double LIMELIGHT_HEIGHT = 14.2 - 1.9; // Height of the limelight in cm
    public static final double LIMELIGHT_ANGLE_IN_DEGREE = 60.0; // Angle of the limelight in degrees
    public static final double LIMELIGHT_ANGLE = Math.toRadians(LIMELIGHT_ANGLE_IN_DEGREE); // DO NOT CHANGE

    //public static final double LIMELIGHT_X_DELTA = -9.5;
    public static final double LIMELIGHT_X_DELTA = 0;
    //public static final double LIMELIGHT_Y_DELTA = -1.0;
    public static final double LIMELIGHT_Y_DELTA = 0;

    public static final int DETECTION_PIPELINE_ID = 0; // Pipeline ID for detection based on ML
    public static final int ORIENTATION_PIPELINE_ID = 1; // Pipeline ID for color detection and obtaining orientation
    public static final int POLL_RATE_HZ = 100; // Polling rate in Hz

    public static double AMAZING_CONSTANT = -0.15;

    public static int AREA_MARGIN = 3;
}
