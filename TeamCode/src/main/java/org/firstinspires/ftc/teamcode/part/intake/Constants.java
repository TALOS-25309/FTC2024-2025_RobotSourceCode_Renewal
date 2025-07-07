package org.firstinspires.ftc.teamcode.part.intake;

import com.acmerobotics.dashboard.config.Config;

@Config(value = "IntakeConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String CLAW_SERVO_NAME = "IntakeClaw";
    public static final String WRIST_ORIENTATION_SERVO_NAME = "IntakeAngle";
    public static final String WRIST_UP_DOWN_SERVO_NAME = "IntakeWrist";
    public static final String ARM_UP_DOWN_SERVO_NAME = "IntakeArm";
    public static final String ARM_ROTATION_SERVO_NAME = "IntakeTurret";
    public static final String LINEAR_SLIDE_MOTOR_NAME = "HorizontalLinear";
    public static final String LINEAR_SLIDE_ENCODER_NAME = "HorizontalLinear";

    // Intake system measurements (in cm, degree)
    public static final double LINEAR_SLIDE_MAX_LENGTH = 52.8; // Maximum length of the linear slide in cm
    public static double ARM_LENGTH = 13.5; // Length of the arm in cm
    public static double TURRET_RANGE_IN_DEGREE = 124.0; // Range of the turret in degrees

    static final double TURRET_RANGE = Math.toRadians(TURRET_RANGE_IN_DEGREE); // DO NOT CHANGE

    // Linear slide system values
    public static double LINEAR_SLIDE_MAX_POWER = 0.8;
    public static double LINEAR_SLIDE_PID_P = 0.0002;
    public static double LINEAR_SLIDE_PID_I = 0.001;
    public static double LINEAR_SLIDE_PID_D = 0.000;
    public static double LINEAR_SLIDE_RANGE = 44359; // Range of the linear slide in encoder ticks
    // Power to stabilize the linear slide when it is inside the robot
    public static double LINEAR_SLIDE_STABLE_POWER = -0.1;

    // Positions of the servos
    public static double CLAW_OPEN_POSITION = 0.7;
    public static double CLAW_CLOSED_POSITION = 0.46;

    public static double WRIST_ORIENTATION_LEFT_LIMIT = 0.155;
    public static double WRIST_ORIENTATION_RIGHT_LIMIT = 0.825;
    public static double WRIST_ORIENTATION_TRANSFER_POSITION = 0.49;

    public static double WRIST_READY_POSITION = 0.34;
    public static double WRIST_PICKUP_POSITION = 0.16;
    public static double WRIST_TRANSFER_POSITION = 0.83;
    public static double WRIST_DROP_POSITION = 0.5;

    public static double ARM_READY_POSITION = 0.75;
    public static double ARM_PRE_TRANSFER_POSITION = 0.8;
    public static double ARM_PICKUP_POSITION = 0.94;
    public static double ARM_TRANSFER_POSITION = 0.46;

    public static double TURRET_LEFT_LIMIT = 0.14;
    public static double TURRET_RIGHT_LIMIT = 0.88;
    public static double TURRET_TRANSFER_POSITION = 0.51;
    public static double TURRET_DROP_POSITION = 1.0;

    // Delays
    public static double AUTO_PICKUP_DELAY_FOR_PICK_UP = 0.5;

    public static double PICKUP_DELAY_FOR_MOVE_DOWN = 0.1;
    public static double PICKUP_DELAY_FOR_CLOSE_CLAW = 0.3;
    public static double PICKUP_DELAY_FOR_MOVE_UP = 0.7;
    public static double PICKUP_DELAY_FOR_CHECKING_DIFFERENCE = 1.0;

    public static double TRANSFER_DELAY_FOR_OPEN_CLAW = 1.0;
    public static double TRANSFER_DELAY_FOR_READY = 1.5;

    public static double DETECTION_DELAY = 0.5;

    public static double DROP_DELAY_FOR_MOVE_RIGHT = 0.1;
    public static double DROP_DELAY_FOR_OPEN_CLAW = 0.3;
    public static double DROP_DELAY_FOR_READY = 0.5;
}
