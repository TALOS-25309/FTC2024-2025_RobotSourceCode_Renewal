package org.firstinspires.ftc.teamcode.opmode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(group = "test")
public class AxonEncoderTest extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Servo axon;
    AnalogInput analogInput;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());

        axon = hardwareMap.get(Servo.class, "axon");
        analogInput = hardwareMap.get(AnalogInput.class, "encoder");
    }

    @Override
    public void loop() {
        double position = analogInput.getVoltage() / 3.3 * 360;
        telemetry.addData("Encoder Position", position);

        if(gamepad1.dpad_up) {
            axon.setPosition(1.0);
            telemetry.addData("UPUP", position);
        } else if (gamepad1.dpad_down) {
            axon.setPosition(0.0);
            telemetry.addData("DOWN", position);
        }

        telemetry.update();
    }
}
