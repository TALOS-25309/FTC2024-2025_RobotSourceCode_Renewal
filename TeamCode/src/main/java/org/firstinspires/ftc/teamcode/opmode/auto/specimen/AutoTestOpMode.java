package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartGamepad;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;

@Config
@Autonomous(group = "Automatic", preselectTeleOp="TeleOpMode")
public class AutoTestOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private SpecimenStrategy strategy;

    public enum Strategy {
        SCORE_SPECIMEN_AND_PICKUP_SAMPLE,
        DETECT_SAMPLE_AND_PICKUP,
        GET_SPECIMEN,
        PUSH_SAMPLES,
    }

    public static Strategy currentStrategy = Strategy.SCORE_SPECIMEN_AND_PICKUP_SAMPLE;
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

        strategy = new SpecimenStrategy(drive, intake, deposit);
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
                case SCORE_SPECIMEN_AND_PICKUP_SAMPLE:
                    strategy.scoreSpecimenAndPickupSample();
                    break;
                case DETECT_SAMPLE_AND_PICKUP:
                    strategy.detectSampleAndPickUp();
                    break;
                case GET_SPECIMEN:
                    strategy.getSpecimen();
                    break;
                case PUSH_SAMPLES:
                    strategy.pushThreeSampleToObservationZone();
                    break;
            }
            run = false; // Reset run flag after executing the strategy
        }
    }

}
