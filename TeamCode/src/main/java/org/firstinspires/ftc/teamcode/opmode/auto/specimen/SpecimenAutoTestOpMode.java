package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;

@Config(value = "Auto-Specimen")
@Autonomous(group = "Automatic", preselectTeleOp="TeleOpMode")
public class SpecimenAutoTestOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private SpecimenStrategyV1 strategy;

    public enum Strategy {
        START_SPECIMEN,
        FIRST_SAMPLE,
        SECOND_SAMPLE,
        THIRD_SAMPLE,
        PICKUP_SPECIMEN,
        SCORE_SPECIMEN,
    }

    public static Strategy currentStrategy = Strategy.START_SPECIMEN;
    public static boolean run = false;

    private boolean isPreviousStrategyIsScore = false;

    @Override
    public void init() {
        Global.init(Global.OpMode.AUTO_SPECIMEN, Global.Alliance.RED);

        SmartMotor.init();
        SmartServo.init();
        Schedule.init();

        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        part_list = new Part[] { intake, deposit, drive };

        for (Part part : part_list) {
            part.init(hardwareMap);
        }

        strategy = new SpecimenStrategyV1(drive, intake, deposit);

        TelemetrySystem.enableClass("Vision");
        TelemetrySystem.enableClass("Drive");
        TelemetrySystem.setDebugMode(true);
    }

    @Override
    public void start() {
        for (Part part : part_list) {
            part.start();
        }
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
                case START_SPECIMEN:
                    strategy.startSpecimen();
                    isPreviousStrategyIsScore = false;
                    break;
                case FIRST_SAMPLE:
                    strategy.moveFirstSample();
                    isPreviousStrategyIsScore = false;
                    break;
                case SECOND_SAMPLE:
                    strategy.moveSecondSample();
                    isPreviousStrategyIsScore = false;
                    break;
                case THIRD_SAMPLE:
                    strategy.moveThirdSample();
                    isPreviousStrategyIsScore = false;
                    break;
                case PICKUP_SPECIMEN:
                    if(isPreviousStrategyIsScore) {
                        strategy.pickupSpecimen();
                    } else {
                        strategy.pickupSpecimenAfterMoveSample();
                    }
                    isPreviousStrategyIsScore = false;
                    break;
                case SCORE_SPECIMEN:
                    strategy.scoreSpecimen();
                    isPreviousStrategyIsScore = true;
                    break;
            }
            run = false; // Reset run flag after executing the strategy
        }
    }

}
