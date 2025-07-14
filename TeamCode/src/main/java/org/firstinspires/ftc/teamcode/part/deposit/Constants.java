package org.firstinspires.ftc.teamcode.part.deposit;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.global.Global;

@Config(value = "DepositConstants")
public class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String ARM_SERVO_LEFT_NAME = "DepositArmL";
    public static final String ARM_SERVO_RIGHT_NAME = "DepositArmR";
    public static final String CLAW_SERVO_NAME = "DepositClaw";
    public static final String LINEAR_SLIDE_MAIN_MOTOR_NAME = "VerticalLinear2";
    public static final String LINEAR_SLIDE_AUX_MOTOR_NAME = "VerticalLinear1";
    public static final String LINEAR_SLIDE_ENCODER_NAME = "VerticalLinear2";

    // Linear slide system values
    public static double LINEAR_SLIDE_MAX_POWER = 1.0;
    public static double LINEAR_SLIDE_PID_P = 0.0002;
    public static double LINEAR_SLIDE_PID_I = 0.0007;
    public static double LINEAR_SLIDE_PID_D = 0.0001;
    public static double LINEAR_SLIDE_RANGE = 63000; // Range of the linear slide in encoder ticks

    // Positions of the servos
    public static double CLAW_OPEN_POSITION = 0.35;
    public static double CLAW_CLOSED_POSITION_FOR_SPECIMEN = 0.593;
    public static double CLAW_CLOSED_POSITION_FOR_SAMPLE = 0.5;
    public static double CLAW_CLOSED_POSITION() {
        if (Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return CLAW_CLOSED_POSITION_FOR_SAMPLE;
        } else {
            return CLAW_CLOSED_POSITION_FOR_SPECIMEN;
        }
    }

    public static double ARM_POSITION_DIFFERENCE = 0.0; // Delta position for arm movement
    public static double ARM_READY_POSITION = 0.07;
    public static double ARM_TRANSFER_POSITION_FOR_SAMPLE = 0.07;
    public static double ARM_TRANSFER_POSITION_FOR_SPECIMEN = 0.09;
    public static double ARM_TRANSFER_POSITION() {
        if (Global.TRANSFER_TYPE == Global.TransferType.SAMPLE) {
            return ARM_TRANSFER_POSITION_FOR_SAMPLE;
        } else {
            return ARM_TRANSFER_POSITION_FOR_SPECIMEN;
        }
    }
    public static double ARM_SPECIMEN_PICKUP_POSITION = 0.87;
    public static double ARM_BASKET_SCORING_POSITION = 0.61;
    public static double ARM_SPECIMEN_SCORING_FORWARD_POSITION = 0.04;
    public static double ARM_SPECIMEN_SCORING_BACKWARD_POSITION = 0.77;
    public static double ARM_DISCARD_POSITION = 0.8;

    // Positions of the linear slide (in cm)
    public static final double LINEAR_SLIDE_READY_POSITION_IN_CM = 0.0;
    public static final double LINEAR_SLIDE_TRANSFER_POSITION_IN_CM = 0.0;
    public static final double LINEAR_SLIDE_SPECIMEN_PICKUP_POSITION_IN_CM = 6.2;
    public static final double LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION_IN_CM = 25.0;
    public static final double LINEAR_SLIDE_HIGH_BASKET_SCORING_POSITION_IN_CM = 69.0;
    public static final double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION_IN_CM = 0.0; // NOT USE
    public static final double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION_IN_CM = 0.0; // NOT USE
    public static final double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION_IN_CM = 29.0;
    public static final double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION_IN_CM = 34.5;
    public static final double LINEAR_SLIDE_ASCENDING_READY_POSITION_IN_CM = 65.0;
    public static final double LINEAR_SLIDE_ASCENDING_CLIMBING_POSITION_IN_CM = 48.0;
    public static final double LINEAR_SLIDE_MAX_LENGTH = 114.3 - 43; // Maximum length of the linear slide in cm

    private static final double LINEAR_SLIDE_ENCODER_VALUE_FRACTION
            = LINEAR_SLIDE_RANGE / LINEAR_SLIDE_MAX_LENGTH;
    static final double LINEAR_SLIDE_READY_POSITION
            = LINEAR_SLIDE_READY_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_TRANSFER_POSITION
            = LINEAR_SLIDE_TRANSFER_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_SPECIMEN_PICKUP_POSITION
            = LINEAR_SLIDE_SPECIMEN_PICKUP_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION
            = LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_HIGH_BASKET_SCORING_POSITION
            = LINEAR_SLIDE_HIGH_BASKET_SCORING_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION
            = LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION
            = LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION
            = LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION
            = LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_ASCENDING_READY_POSITION
            = LINEAR_SLIDE_ASCENDING_READY_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;
    static final double LINEAR_SLIDE_ASCENDING_CLIMBING_POSITION
            = LINEAR_SLIDE_ASCENDING_CLIMBING_POSITION_IN_CM * LINEAR_SLIDE_ENCODER_VALUE_FRACTION;

    // Delays
    public static double TRANSFER_DELAY_FOR_GOTO_TRANSFER_POSITION = 0.1;
    public static double TRANSFER_DELAY_FOR_CLOSE_CLAW = 0.3;
    public static double TRANSFER_DELAY_FOR_GOTO_READY_POSITION = 0.8;

    public static double PICKUP_DELAY_FOR_GOTO_SPECIMEN_SCORING_POSE = 0.5;

    public static double SPECIMEN_SCORING_POSE_DELAY_FOR_MOVE_ARM = 0.5;

    public static double SCORING_BASKET_DELAY_FOR_GOTO_READY_POSITION = 0.3;

    public static double SCORING_SPECIMEN_DELAY_FOR_GOTO_READY_POSITION = 0.3;

    public static double DISCARD_DELAY_FOR_GOTO_READY_POSITION = 0.5;
    public static double DISCARD_DELAY_FOR_OPEN_CLAW = 0.2;

    public static double BASKET_DELAY_FOR_MOVING_ARM = 1.0;
}
