package org.firstinspires.ftc.teamcode.opmode.tele;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.global.Global;

@TeleOp(name = "[BLUE] TeleOpMode", group = "Telemetry")
public class BlueTeleOpMode extends TeleOpMode {
    @Override
    protected void globalInit() {
        Global.init(Global.OpMode.TELE, Global.Alliance.BLUE);
    }
}
