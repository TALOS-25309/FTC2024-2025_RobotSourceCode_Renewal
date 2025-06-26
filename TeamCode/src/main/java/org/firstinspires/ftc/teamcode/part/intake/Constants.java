package org.firstinspires.ftc.teamcode.part.intake;

import com.acmerobotics.dashboard.config.Config;

@Config(value = "IntakeConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String CLAW_SERVO_NAME = "intake_claw";
    public static final String WRIST_ORIENTATION_SERVO_NAME = "intake_wrist_ori";
    public static final String WRIST_UP_DOWN_SERVO_NAME = "intake_wrist";
    public static final String ARM_UP_DOWN_SERVO_NAME = "intake_arm";
    public static final String ARM_ROTATION_SERVO_NAME = "intake_turret";
    public static final String LINEAR_SLIDE_MOTOR_NAME = "intake_linear";
    public static final String LINEAR_SLIDE_ENCODER_NAME = "intake_linear_encoder";

    // Intake system measurements (in cm, degree)
    public static final double LINEAR_SLIDE_MAX_LENGTH = 50.0; // Maximum length of the linear slide in cm
    public static final double ARM_LENGTH = 20.0; // Length of the arm in cm
    public static final double TURRET_RANGE_IN_DEGREE = 180.0; // Range of the turret in degrees

    static final double TURRET_RANGE = Math.toRadians(TURRET_RANGE_IN_DEGREE); // DO NOT CHANGE

    // Linear slide system values
    public static double LINEAR_SLIDE_MAX_POWER = 0.5;
    public static double LINEAR_SLIDE_PID_P = 0.1;
    public static double LINEAR_SLIDE_PID_I = 0.0;
    public static double LINEAR_SLIDE_PID_D = 0.0;
    public static double LINEAR_SLIDE_RANGE = 1000.0; // Range of the linear slide in encoder ticks
    // Power to stabilize the linear slide when it is inside the robot
    public static double LINEAR_SLIDE_STABLE_POWER = -0.1;

    // Positions of the servos
    public static double CLAW_OPEN_POSITION = 0.0;
    public static double CLAW_CLOSED_POSITION = 0.5;

    public static double WRIST_ORIENTATION_LEFT_LIMIT = 0.0;
    public static double WRIST_ORIENTATION_RIGHT_LIMIT = 1.0;
    public static double WRIST_ORIENTATION_TRANSFER_POSITION = 0.5;

    public static double WRIST_READY_POSITION = 0.0;
    public static double WRIST_PICKUP_POSITION = 0.5;
    public static double WRIST_TRANSFER_POSITION = 1.0;

    public static double ARM_READY_POSITION = 0.0;
    public static double ARM_PICKUP_POSITION = 0.5;
    public static double ARM_TRANSFER_POSITION = 1.0;

    public static double TURRET_LEFT_LIMIT = 0.0;
    public static double TURRET_RIGHT_LIMIT = 1.0;
    public static double TURRET_TRANSFER_POSITION = 0.5;

    // Delays
    public static double PICKUP_DELAY_FOR_CLOSE_CLAW = 0.5;
    public static double PICKUP_DELAY_FOR_MOVE_UP = 0.5;

    public static double TRANSFER_DELAY_FOR_OPEN_CLAW = 0.5;
    public static double TRANSFER_DELAY_FOR_LINEAR_SLIDE_STABILIZATION = 0.5;
}
