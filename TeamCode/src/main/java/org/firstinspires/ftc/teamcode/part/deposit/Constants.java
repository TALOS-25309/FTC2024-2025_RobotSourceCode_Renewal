package org.firstinspires.ftc.teamcode.part.deposit;

public class Constants {
    private Constants() {} // Prevent instantiation

    // Hardware Setting
    public static final String ARM_SERVO_LEFT_NAME = "deposit_arm_left";
    public static final String ARM_SERVO_RIGHT_NAME = "deposit_arm_right";
    public static final String CLAW_SERVO_NAME = "deposit_claw";
    public static final String LINEAR_SLIDE_MAIN_MOTOR_NAME = "deposit_linear_main";
    public static final String LINEAR_SLIDE_AUX_MOTOR_NAME = "deposit_linear_aux";
    public static final String LINEAR_SLIDE_ENCODER_NAME = "deposit_linear_encoder";

    // Linear slide system values
    public static double LINEAR_SLIDE_MAX_POWER = 0.5;
    public static double LINEAR_SLIDE_P = 0.1;
    public static double LINEAR_SLIDE_I = 0.0;
    public static double LINEAR_SLIDE_D = 0.0;
    public static double LINEAR_SLIDE_RANGE = 1000.0; // Range of the linear slide in encoder ticks
    static final boolean CAN_LINEAR_SLIDE_SYSTEM_BE_MANIPULATED = true;

    // Positions of the servos
    public static double CLAW_OPEN_POSITION = 0.0;
    public static double CLAW_CLOSED_POSITION = 0.5;

    public static double ARM_POSITION_DIFFERENCE = 0.1; // Delta position for arm movement
    public static double ARM_READY_POSITION = 0.3;
    public static double ARM_TRANSFER_POSITION = 0.0;
    public static double ARM_SPECIMEN_PICKUP_POSITION = 1.0;
    public static double ARM_BASKET_SCORING_POSITION = 0.7;
    public static double ARM_SPECIMEN_SCORING_FORWARD_POSITION = 0.8;
    public static double ARM_SPECIMEN_SCORING_BACKWARD_POSITION = 0.2;

    // Positions of the linear slide (in cm)
    public static double LINEAR_SLIDE_READY_POSITION = 0.0;
    public static double LINEAR_SLIDE_TRANSFER_POSITION = 0.0;
    public static double LINEAR_SLIDE_SPECIMEN_PICKUP_POSITION = 10.0;
    public static double LINEAR_SLIDE_LOW_BASKET_SCORING_POSITION = 20.0;
    public static double LINEAR_SLIDE_HIGH_BASKET_SCORING_POSITION = 50.0;
    public static double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_FORWARD_POSITION = 30.0;
    public static double LINEAR_SLIDE_LOW_SPECIMEN_SCORING_BACKWARD_POSITION = 30.0;
    public static double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_FORWARD_POSITION = 40.0;
    public static double LINEAR_SLIDE_HIGH_SPECIMEN_SCORING_BACKWARD_POSITION = 40.0;
    public static double LINEAR_SLIDE_MAX_LENGTH = 50.0; // Maximum length of the linear slide in cm

    // Delays
    public static double TRANSFER_DELAY_FOR_GOTO_TRANSFER_POSITION = 0.3;
    public static double TRANSFER_DELAY_FOR_CLOSE_CLAW = 0.5;
    public static double TRANSFER_DELAY_FOR_GOTO_READY_POSITION = 0.8;

    public static double PICKUP_DELAY_FOR_GOTO_READY_POSITION = 0.3;

    public static double SCORING_BASKET_DELAY_FOR_GOTO_READY_POSITION = 0.3;

    public static double SCORING_SPECIMEN_DELAY_FOR_GOTO_READY_POSITION = 0.3;
}
