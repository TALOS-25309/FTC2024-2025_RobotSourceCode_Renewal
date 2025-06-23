package org.firstinspires.ftc.teamcode.vision;

public class VisionConstants {
    private VisionConstants() {} // Prevent instantiation

    // Limelight settings
    public static final String LIMELIGHT_NAME = "limelight";
    public static final double LIMELIGHT_HEIGHT = 30.0; // Height of the limelight in cm
    public static final double LIMELIGHT_ANGLE_IN_DEGREE = 0.0; // Angle of the limelight in degrees
    public static final double LIMELIGHT_ANGLE = Math.toRadians(LIMELIGHT_ANGLE_IN_DEGREE); // DO NOT CHANGE

    public static final int PIPELINE_ID = 0; // Default pipeline ID for the limelight
    public static final int POLL_RATE_HZ = 100; // Polling rate in Hz
}
