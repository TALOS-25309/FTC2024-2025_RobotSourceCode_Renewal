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
    public enum TransferType {SAMPLE, SPECIMEN}
    public static TransferType TRANSFER_TYPE = TransferType.SAMPLE;
}
