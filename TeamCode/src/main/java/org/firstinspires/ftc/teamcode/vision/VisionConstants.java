package org.firstinspires.ftc.teamcode.vision;

public class VisionConstants {
    private VisionConstants() {} // Prevent instantiation

    // Limelight settings
    public static final String LIMELIGHT_NAME = "limelight";
    public static final double LIMELIGHT_HEIGHT = 22.0; // Height of the limelight in cm
    public static final double LIMELIGHT_ANGLE_IN_DEGREE = 80.0; // Angle of the limelight in degrees
    public static final double LIMELIGHT_ANGLE = Math.toRadians(LIMELIGHT_ANGLE_IN_DEGREE); // DO NOT CHANGE

    public static final int DETECTION_PIPELINE_ID = 0; // Pipeline ID for detection based on ML
    public static final int ORIENTATION_PIPELINE_ID = 1; // Pipeline ID for color detection and obtaining orientation
    public static final int POLL_RATE_HZ = 100; // Polling rate in Hz

    public static int AREA_MARGIN = 30;
}
