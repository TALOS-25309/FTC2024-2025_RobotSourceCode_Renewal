package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.util.Encoder;

@Autonomous(name = "EasyAuto")
public class AutoOpMode extends OpMode {

    private Part[] part_list;

    Encoder leftEncoder, rightEncoder;

    @Override
    public void init() {
        Global.ROBOT_STATE = Global.RobotState.NONE;

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftRear"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftFront"));

        part_list = new Part[] {  };

        for (Part part : part_list) {
            part.init(hardwareMap, telemetry);
        }

        leftEncoder.setDirection(Encoder.Direction.FORWARD);
        rightEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    int step = 1;

    int target = 8200;

    @Override
    public void start() {

    }

    @Override
    public void loop() {

    }

}
