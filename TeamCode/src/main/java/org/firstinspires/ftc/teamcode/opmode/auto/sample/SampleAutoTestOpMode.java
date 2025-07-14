package org.firstinspires.ftc.teamcode.opmode.auto.sample;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;

@Config(value = "Auto-Sample")
@Autonomous(group = "Automatic", preselectTeleOp="TeleOpMode")
public class SampleAutoTestOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private SampleStrategy strategy;

    public enum Strategy {
        SCORE_SAMPLE,
        FIRST_SAMPLE,
        SECOND_SAMPLE,
        THIRD_SAMPLE,
    }

    public static Strategy currentStrategy = Strategy.SCORE_SAMPLE;
    public static boolean run = false;

    @Override
    public void init() {
        SmartMotor.init();
        SmartServo.init();
        Schedule.init();

        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        part_list = new Part[] { intake, deposit, drive };

        for (Part part : part_list) {
            part.init(hardwareMap);
        }

        strategy = new SampleStrategy(drive, intake, deposit);

        TelemetrySystem.enableClass("Vision");
        TelemetrySystem.enableClass("Drive");
        TelemetrySystem.setDebugMode(true);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        // Updating parts and features
        for (Part part : part_list) {
            part.update();
        }
        Schedule.update();
        SmartServo.updateAll();
        SmartMotor.updateAll();

        // Update telemetry
        TelemetrySystem.update();

        if (run) {
            switch (currentStrategy) {
                case SCORE_SAMPLE:
                    strategy.startSample();
                    break;
                case FIRST_SAMPLE:
                    strategy.firstSample();
                    break;
                case SECOND_SAMPLE:
                    strategy.secondSample();
                    break;
                case THIRD_SAMPLE:
                    strategy.thirdSample();
                    break;
            }
            run = false; // Reset run flag after executing the strategy
        }
    }

}
