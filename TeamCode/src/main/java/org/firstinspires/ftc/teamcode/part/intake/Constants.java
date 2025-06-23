package org.firstinspires.ftc.teamcode.part.intake;

import com.acmerobotics.dashboard.config.Config;

@Config(value = "IntakeConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    static final String CLAW_SERVO_NAME = "intake_claw";
    static final String WRIST_ORIENTATION_SERVO_NAME = "intake_wrist_ori";
    static final String WRIST_UP_DOWN_SERVO_NAME = "intake_wrist";
    static final String ARM_UP_DOWN_SERVO_NAME = "intake_arm";
    static final String ARM_ROTATION_SERVO_NAME = "intake_turret";
    static final String LINEAR_SLIDE_MOTOR_NAME = "intake_linear";
    static final String LINEAR_SLIDE_ENCODER_NAME = "intake_linear_encoder";

    // Intake system measurements (in cm, degree)
    static final double LINEAR_SLIDE_MAX_LENGTH = 50.0; // Maximum length of the linear slide in cm
    static final double ARM_LENGTH = 20.0; // Length of the arm in cm
    static final double TURRET_RANGE_IN_DEGREE = 180.0; // Range of the turret in degrees

    static final double TURRET_RANGE = Math.toRadians(TURRET_RANGE_IN_DEGREE); // DO NOT CHANGE

    // Linear slide system values
    static double LINEAR_SLIDE_MAX_POWER = 0.5;
    static double LINEAR_SLIDE_P = 0.1;
    static double LINEAR_SLIDE_I = 0.0;
    static double LINEAR_SLIDE_D = 0.0;
    static double LINEAR_SLIDE_RANGE = 1000.0; // Range of the linear slide in encoder ticks
    static double LINEAR_SLIDE_STABLE_POWER = -0.1; // Power to stabilize the linear slide when it is inside the robot
    static final boolean CAN_LINEAR_SLIDE_SYSTEM_BE_MANIPULATED = true;

    // Positions of the servos
    static double CLAW_OPEN_POSITION = 0.0;
    static double CLAW_CLOSED_POSITION = 0.5;

    static double WRIST_ORIENTATION_LEFT_LIMIT = 0.0;
    static double WRIST_ORIENTATION_RIGHT_LIMIT = 1.0;
    static double WRIST_ORIENTATION_TRANSFER_POSITION = 0.5;

    static double WRIST_READY_POSITION = 0.0;
    static double WRIST_PICKUP_POSITION = 0.5;
    static double WRIST_TRANSFER_POSITION = 1.0;

    static double ARM_READY_POSITION = 0.0;
    static double ARM_PICKUP_POSITION = 0.5;
    static double ARM_TRANSFER_POSITION = 1.0;

    static double TURRET_LEFT_LIMIT = 0.0;
    static double TURRET_RIGHT_LIMIT = 1.0;
    static double TURRET_TRANSFER_POSITION = 0.5;

    // Delays
    static double PICKUP_DELAY_FOR_CLOSE_CLAW = 0.5;
    static double PICKUP_DELAY_FOR_MOVE_UP = 0.5;

    static double TRANSFER_DELAY_FOR_OPEN_CLAW = 0.5;
    static double TRANSFER_DELAY_FOR_LINEAR_SLIDE_STABILIZATION = 0.5;
}
