package org.firstinspires.ftc.teamcode.global;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Global {
    public enum Alliance { RED, BLUE }
    public static Alliance ALLIANCE = Alliance.RED;
    public static boolean IS_EMERGENCY = false;
    public static boolean DETECTING = false;
    public static boolean ASCENDING = false;
}
