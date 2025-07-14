package org.firstinspires.ftc.teamcode.global;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Global {
    public enum OpMode {AUTO_SPECIMEN, AUTO_SAMPLE, TELE}
    public static OpMode OPMODE = OpMode.AUTO_SAMPLE;
    public enum Alliance { RED, BLUE }
    public static Alliance ALLIANCE = Alliance.RED;
    public static boolean IS_EMERGENCY = false;
    public static boolean DETECTING = false;
    public static boolean ASCENDING = false;
    public static boolean PERMIT_PICKUP = true;
    public enum TransferType {SAMPLE, SPECIMEN}
    public static TransferType TRANSFER_TYPE = TransferType.SAMPLE;
    public static boolean ENCODER_RESET = true;

    private static OpMode PREVIOUS_OPMODE = OpMode.TELE;

    public static void init(OpMode opmode, Alliance alliance){
        Global.OPMODE = opmode;
        Global.ALLIANCE = alliance;
        IS_EMERGENCY = false;
        DETECTING = false;
        ASCENDING = false;
        PERMIT_PICKUP = true;

        ENCODER_RESET = true;
        if (OPMODE == OpMode.TELE) {
           if(PREVIOUS_OPMODE == OpMode.AUTO_SAMPLE || PREVIOUS_OPMODE == OpMode.AUTO_SPECIMEN) {
               ENCODER_RESET = false;
           }
        }
        PREVIOUS_OPMODE = OPMODE;

        if(opmode == OpMode.AUTO_SPECIMEN) {
            TRANSFER_TYPE = TransferType.SPECIMEN;
        } else {
            TRANSFER_TYPE = TransferType.SAMPLE;
        }
    }
}
