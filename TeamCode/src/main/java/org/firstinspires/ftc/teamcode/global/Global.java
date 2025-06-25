package org.firstinspires.ftc.teamcode.global;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Global {
    public enum Alliance { RED, BLUE }
    public static Alliance ALLIANCE = Alliance.RED;
    public static boolean IS_EMERGENCY = false;

    public enum HighOrLow { HIGH, LOW }
    public static HighOrLow HIGH_OR_LOW = HighOrLow.HIGH;
}
