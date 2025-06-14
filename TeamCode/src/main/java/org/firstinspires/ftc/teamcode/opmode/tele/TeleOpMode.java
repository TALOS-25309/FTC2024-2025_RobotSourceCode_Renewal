package org.firstinspires.ftc.teamcode.opmode.tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartGamepad;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.Part;

@TeleOp(name = "TeleOp")
public class TeleOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private SmartGamepad smartGamepad1, smartGamepad2;

    @Override
    public void init() {
        Global.ROBOT_STATE = Global.RobotState.NONE;

        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());

        smartGamepad1 = new SmartGamepad(gamepad1);
        smartGamepad2 = new SmartGamepad(gamepad2);

        part_list = new Part[] { };

        for (Part part : part_list) {
            part.init(hardwareMap, telemetry);
        }
    }

    @Override
    public void loop() {
        for (Part part : part_list) {
            part.update();
        }
        Schedule.update();

        checkEmergency();

        if(!Global.IS_EMERGENCY) {
            controlGamepad1();
            controlGamepad2();
        }

        smartGamepad1.update();
        smartGamepad2.update();

        telemetry.addData("WARNING",Global.PLAYER2_WARNING);

        if (Global.PLAYER1_WARNING) {
            smartGamepad1.gamepad().rumble(100);
            Global.PLAYER1_WARNING = false;
        }
        if (Global.PLAYER2_WARNING) {
            smartGamepad2.gamepad().rumble(100);
            Global.PLAYER2_WARNING = false;
        }

        SmartServo.updateAll();

        telemetry.addData("STATE",Global.ROBOT_STATE);
        telemetry.update();
    }

    @Override
    public void stop() {
        for (Part part : part_list) {
            part.stop();
        }
    }

    public void controlGamepad1() {
        // Standard Drive
        double x = Math.abs(smartGamepad1.gamepad().left_stick_x) > Math.abs(smartGamepad1.gamepad().right_stick_x)
                ? smartGamepad1.gamepad().left_stick_x
                : smartGamepad1.gamepad().right_stick_x;
        double y = Math.abs(smartGamepad1.gamepad().left_stick_y) > Math.abs(smartGamepad1.gamepad().right_stick_y)
                ? smartGamepad1.gamepad().left_stick_y
                : smartGamepad1.gamepad().right_stick_y;
        double rot = smartGamepad1.gamepad().left_trigger - smartGamepad1.gamepad().right_trigger;
    }

    public void controlGamepad2() {

    }

    public void checkEmergency() {
        if (smartGamepad1.gamepad().left_bumper && smartGamepad1.gamepad().right_bumper) {
            Global.IS_EMERGENCY = true;
            for (Part part : part_list) {
                part.stop();
            }

            smartGamepad1.gamepad().rumble(500);
            smartGamepad2.gamepad().rumble(500);

        } else {
            Global.IS_EMERGENCY = false;
        }
    }
}
