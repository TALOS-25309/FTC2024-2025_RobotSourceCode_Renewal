package org.firstinspires.ftc.teamcode.opmode.auto.sample;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.opmode.auto.specimen.Constants;
import org.firstinspires.ftc.teamcode.opmode.auto.specimen.SpecimenStrategy;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;

@Autonomous(group = "Automatic", preselectTeleOp="TeleOpMode")
public class SampleAutoOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private SampleStrategy strategy;

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
    }

    public void procedure() {
        strategy.startSample();
        Schedule.addConditionalTask(() -> {
            strategy.firstSample();
            Schedule.addConditionalTask(() -> {
                strategy.secondSample();
                Schedule.addConditionalTask(() -> {
                    strategy.thirdSample();
                }, Schedule.RUN_INSTANTLY, () -> strategy.isEnd());
            }, Schedule.RUN_INSTANTLY, () -> strategy.isEnd());
        }, Schedule.RUN_INSTANTLY, () -> strategy.isEnd());
    }

    @Override
    public void start() {
        procedure();
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
    }

}
