package org.firstinspires.ftc.teamcode.part.intake;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.global.Global;

@Config(value = "IntakeConstants")
class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String CLAW_SERVO_NAME = "IntakeClaw";
    public static final String CLAW_ANALOG_INPUT_NAME = "IntakeClawEncoder";
    public static final String WRIST_ORIENTATION_SERVO_NAME = "IntakeAngle";
    public static final String WRIST_UP_DOWN_SERVO_NAME = "IntakeWrist";
    public static final String ARM_UP_DOWN_SERVO_NAME = "IntakeArm";
    public static final String ARM_ROTATION_SERVO_NAME = "IntakeTurret";
    public static final String LINEAR_SLIDE_MOTOR_NAME = "HorizontalLinear";
    public static final String LINEAR_SLIDE_ENCODER_NAME = "HorizontalLinear";

    // Intake system measurements (in cm, degree)
    public static final double LINEAR_SLIDE_MAX_LENGTH = 52.8; // Maximum length of the linear slide in cm
    public static double ARM_LENGTH = 12.39; // Length of the arm in cm
    public static double CLAW_DISTANCE = 0.69; // Distance offset of the claw in cm
    public static double ANGLE_OFFSET = 5.2; // Angle offset in degrees for the turret
    public static double TURRET_RANGE_IN_DEGREE = 101.87; // Range of the turret in degrees (167 * RANGE)

    static final double TURRET_RANGE = Math.toRadians(TURRET_RANGE_IN_DEGREE); // DO NOT CHANGE

    // Linear slide system values
    public static double LINEAR_SLIDE_MAX_POWER = 1.0;
    public static double LINEAR_SLIDE_PID_P = 0.0002;
    public static double LINEAR_SLIDE_PID_I = 0.001;
    public static double LINEAR_SLIDE_PID_D = 0.000;
    public static double LINEAR_SLIDE_RANGE = 44359; // Range of the linear slide in encoder ticks
    // Power to stabilize the linear slide when it is inside the robot
    public static double LINEAR_SLIDE_STABLE_POWER = -0.1;

    // Positions of the servos
    public static double CLAW_OPEN_POSITION = 0.35;
    public static double CLAW_CLOSED_POSITION = 0.535;
    public static double CLAW_CLOSED_MAXIMUM_POSITION = 0.61;

    public static double WRIST_ORIENTATION_LEFT_LIMIT = 0.185;
    public static double WRIST_ORIENTATION_RIGHT_LIMIT = 0.855;
    public static double WRIST_ORIENTATION_TRANSFER_POSITION = 0.52;

    public static double WRIST_READY_POSITION = 0.45;
    public static double WRIST_PICKUP_POSITION_FOR_SAMPLE = 0.32;
    public static double WRIST_PICKUP_POSITION_FOR_SPECIMEN = 0.42;
    public static double WRIST_TRANSFER_POSITION_FOR_SAMPLE = 0.97;
    public static double WRIST_TRANSFER_POSITION_FOR_SPECIMEN = 0.86;
    public static double WRIST_DROP_POSITION = 0.65;
    public static double WRIST_PICKUP_POSITION() {
        if(Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return WRIST_PICKUP_POSITION_FOR_SAMPLE;
        } else {
            return WRIST_PICKUP_POSITION_FOR_SPECIMEN;
        }
    }
    public static double WRIST_TRANSFER_POSITION() {
        if(Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return WRIST_TRANSFER_POSITION_FOR_SAMPLE;
        } else {
            return WRIST_TRANSFER_POSITION_FOR_SPECIMEN;
        }
    }

    public static double ARM_READY_POSITION = 0.73;
    public static double ARM_PRE_TRANSFER_POSITION = 0.8;
    public static double ARM_PICKUP_POSITION_FOR_SAMPLE = 0.935;
    public static double ARM_PICKUP_POSITION_FOR_SPECIMEN = 0.9;
    public static double ARM_TRANSFER_POSITION_FOR_SAMPLE = 0.44;
    public static double ARM_TRANSFER_POSITION_FOR_SPECIMEN = 0.4;
    public static double ARM_CAUTIOUS_PICKUP_READY_POSITION = 0.83;
    public static double ARM_PICKUP_POSITION() {
        if(Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return ARM_PICKUP_POSITION_FOR_SAMPLE;
        } else {
            return ARM_PICKUP_POSITION_FOR_SPECIMEN;
        }
    }
    public static double ARM_TRANSFER_POSITION() {
        if(Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return ARM_TRANSFER_POSITION_FOR_SAMPLE;
        } else {
            return ARM_TRANSFER_POSITION_FOR_SPECIMEN;
        }
    }

    public static double TURRET_LEFT_LIMIT = 0.05;
    public static double TURRET_RIGHT_LIMIT = 0.66;
    public static double TURRET_TRANSFER_POSITION = 0.355;
    public static double TURRET_DROP_POSITION = 1.0;

    // Analog input values
    public static final double ANALOG_INPUT_MAX = 3.3;
    public static double ANALOG_INPUT_PICKUP_THRESHOLD = 1.45;

    // Delays
    public static double MIN_DETECTION_DELAY_FOR_PICKUP = 0.5;

    public static double PICKUP_DELAY_FOR_MOVE_DOWN = 0.1;
    public static double PICKUP_DELAY_FOR_CLOSE_CLAW = 0.4;
    public static double PICKUP_DELAY_FOR_MOVE_UP = 0.6;
    public static double PICKUP_DELAY_FOR_CHECKING_PICKUP = 0.8;

    public static double TRANSFER_DELAY_FOR_OPEN_CLAW = 0.6;
    public static double TRANSFER_DELAY_FOR_READY = 1.0;

    public static double DETECTION_DELAY = 0.1;

    public static double DROP_DELAY_FOR_MOVE_RIGHT = 0.1;
    public static double DROP_DELAY_FOR_OPEN_CLAW = 0.5;
    public static double DROP_DELAY_FOR_MOVE_CENTER = 0.8;
    public static double DROP_DELAY_FOR_READY = 1.0;

    // Others
    public static double LINEAR_SLIDE_READY_POSITION_THRESHOLD = 100;
}
