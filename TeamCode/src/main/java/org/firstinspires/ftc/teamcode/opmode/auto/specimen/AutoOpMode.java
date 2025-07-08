package org.firstinspires.ftc.teamcode.opmode.auto.specimen;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartGamepad;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;

@Autonomous(group = "Automatic", preselectTeleOp="TeleOpMode")
public class AutoOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private SpecimenStrategy strategy;

    private boolean alreadyPushedSamples = false;

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

        SampleMecanumDrive.getVelocityConstraint(
                Constants.SPECIMEN_VELOCITY,
                DriveConstants.MAX_ANG_VEL,
                DriveConstants.TRACK_WIDTH
        );
    }

    public void procedure() {
        Schedule.addTask(() -> {
            // 1. Specimen을 걸고
            strategy.scoreSpecimenAndPickupSample();
            Schedule.addConditionalTask(() -> {
                // 2. Sample Pickup을 시도
                strategy.detectSampleAndPickUp();
                Schedule.addConditionalTask(() -> {
                    if(strategy.isPickupSuccessful || alreadyPushedSamples) {
                        // 3. Specimen 가지러 가기
                        strategy.getSpecimen();
                    } else {
                        // 4. Sample 3개 옮기기
                        strategy.pushThreeSampleToObservationZone();
                        alreadyPushedSamples = true;
                    }
                    Schedule.addConditionalTask(
                            // 5. 반복
                            this::procedure,
                            Schedule.RUN_INSTANTLY,
                            () -> !drive.isBusy()
                    );
                }, Schedule.RUN_INSTANTLY, () -> !strategy.isPickingUpSample);
            }, Schedule.RUN_INSTANTLY, () -> !drive.isBusy());
        }, Schedule.RUN_INSTANTLY);
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
