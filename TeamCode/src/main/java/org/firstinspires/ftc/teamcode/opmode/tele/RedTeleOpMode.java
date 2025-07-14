package org.firstinspires.ftc.teamcode.opmode.tele;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.global.Global;

@TeleOp(name = "[RED] TeleOpMode", group = "Telemetry")
public class RedTeleOpMode extends TeleOpMode {
    @Override
    protected void globalInit() {
        Global.init(Global.OpMode.TELE, Global.Alliance.RED);
    }
}
