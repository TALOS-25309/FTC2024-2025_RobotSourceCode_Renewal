package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.util.Encoder;

@Autonomous(group = "auto", preselectTeleOp="TeleOpMode")
public class AutoOpMode extends OpMode {

    private Part[] part_list;

    @Override
    public void init() {
        Global.ROBOT_STATE = Global.RobotState.NONE;

        part_list = new Part[] {  };
        for (Part part : part_list) {
            part.init(hardwareMap, telemetry);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

    }

}
