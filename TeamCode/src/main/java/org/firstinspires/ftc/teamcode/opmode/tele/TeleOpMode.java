package org.firstinspires.ftc.teamcode.opmode.tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.features.Schedule;
import org.firstinspires.ftc.teamcode.features.SmartGamepad;
import org.firstinspires.ftc.teamcode.features.SmartMotor;
import org.firstinspires.ftc.teamcode.features.SmartServo;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.global.Global;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.deposit.Deposit;
import org.firstinspires.ftc.teamcode.part.deposit.DepositState;
import org.firstinspires.ftc.teamcode.part.drive.Drive;
import org.firstinspires.ftc.teamcode.part.intake.Intake;
import org.firstinspires.ftc.teamcode.part.intake.IntakeState;

@Disabled
@TeleOp(group = "Telemetry")
public class TeleOpMode extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private SmartGamepad smartGamepad1, smartGamepad2;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private boolean lastDetecting = Global.DETECTING;

    @Override
    public void init() {
        SmartMotor.init();
        SmartServo.init();
        Schedule.init();

        telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());
        TelemetrySystem.init(telemetry);

        smartGamepad1 = new SmartGamepad(gamepad1);
        smartGamepad2 = new SmartGamepad(gamepad2);

        part_list = new Part[] { intake, deposit, drive };

        for (Part part : part_list) {
            part.init(hardwareMap);
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

        // Checking driver inputs
        checkEmergency();
        if(!Global.IS_EMERGENCY) {
            controlGamepad1();
            controlGamepad2();
        } else {
            for (Part part : part_list) {
                part.stop();
            }
        }

        smartGamepad1.update();
        smartGamepad2.update();

        if (Global.DETECTING != this.lastDetecting) {
            smartGamepad1.rumble(0.1);
            this.lastDetecting = Global.DETECTING;
        }

        TelemetrySystem.update();
    }

    @Override
    public void stop() {
        for (Part part : part_list) {
            part.stop();
        }
        Schedule.stop();
    }

    public void controlGamepad1() {
        // Controlling Transfer & Drop
        if (smartGamepad1.buttonDPadDown().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_TRANSFER
                && deposit.state() == DepositState.REST) {
                intake.command().transfer();
                deposit.command().transfer();
            } else if (intake.state() == IntakeState.PICKED_UP) {
                intake.command().readyForTransfer();
            } else {
                intake.command().compactReady();
            }
        }

        TelemetrySystem.addClassData("Intake", "state",intake.state().toString());

        // Controlling Intake Part
        if (smartGamepad1.buttonDPadUp().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_PICKUP){
                intake.command().pickup();
            } else if (intake.state() == IntakeState.PICKED_UP) {
                intake.command().discard();
            }
        }
        if (smartGamepad1.buttonDPadLeft().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_PICKUP) {
                intake.command().automaticTargetForYellowSample();
            }
        }
        if (smartGamepad1.buttonDPadRight().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_PICKUP) {
                intake.command().automaticTargetForAllianceSample();
            }
        }
        if (intake.state() == IntakeState.READY_FOR_PICKUP
            || intake.state() == IntakeState.PICKED_UP) {
            int left = smartGamepad1.buttonLeftBumper().isHeld() ? 1 : 0;
            int right = smartGamepad1.buttonRightBumper().isHeld() ? 1 : 0;
            intake.command().setPositionDelta(
                smartGamepad1.triggerLeftStickX().getValue(),
                -smartGamepad1.triggerLeftStickY().getValue(),
                right-left
            );
        }

        // Controlling Deposit Part
        if (smartGamepad1.buttonTriangle().isPressed()) {
            if (deposit.state() == DepositState.REST) {
                deposit.command().poseForSpecimenPickup();
            }
        }
        if (smartGamepad1.buttonCircle().isPressed()) {
            if (deposit.state() == DepositState.LOAD_SAMPLE
            || deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
                deposit.command().poseForHighBasketScoring();
            }
            else if (deposit.state() == DepositState.LOAD_SPECIMEN
            || deposit.state() == DepositState.READY_FOR_DEPOSIT_SPECIMEN) {
                deposit.command().poseForHighSpecimenScoringForward();
            }
        }
        if (smartGamepad1.buttonSquare().isPressed()) {
            if (deposit.state() == DepositState.LOAD_SAMPLE
                    || deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
                deposit.command().poseForLowBasketScoring();
            }
            else if (deposit.state() == DepositState.LOAD_SPECIMEN
                    || deposit.state() == DepositState.READY_FOR_DEPOSIT_SPECIMEN) {
                deposit.command().poseForLowSpecimenScoringForward();
            }
        }
        if (smartGamepad1.buttonCross().isPressed()) {
            deposit.command().poseForDiscard();
        }
    }

    public void controlGamepad2() {
        // Controlling Drive Part
        if (Math.abs(smartGamepad2.triggerLeftStickX().getValue()) > 0.0
        || Math.abs(smartGamepad2.triggerLeftStickY().getValue()) > 0.0){
            drive.command().drive(
                    smartGamepad2.triggerLeftStickX().getValue(),
                    -smartGamepad2.triggerLeftStickY().getValue(),
                    (smartGamepad2.triggerRightTrigger().getValue()
                            - smartGamepad2.triggerLeftTrigger().getValue()) * 0.4
            );
        } else {
            drive.command().drive(
                    smartGamepad2.triggerRightStickX().getValue() * 0.3,
                    -smartGamepad2.triggerRightStickY().getValue() * 0.3,
                    (smartGamepad2.triggerRightTrigger().getValue()
                            - smartGamepad2.triggerLeftTrigger().getValue()) * 0.4
            );
        }

        // Controlling Intake & Deposit Parts
        if (smartGamepad2.buttonTriangle().isPressed()) {
            if(intake.state() == IntakeState.READY_FOR_TRANSFER) {
                intake.command().drop();
            }
            if (deposit.state() == DepositState.READY_FOR_PICKUP) {
                deposit.command().pickupSpecimen();
            } else if (deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
                deposit.command().scoringBasket();
            } else if (deposit.state() == DepositState.READY_FOR_DEPOSIT_SPECIMEN) {
                deposit.command().scoringSpecimen();
            } else if (deposit.state() == DepositState.READY_FOR_DISCARD) {
                deposit.command().discard();
            }
        }
        if (smartGamepad2.buttonCross().isPressed()) {
            deposit.command().discard();
        }
    }

    public void checkEmergency() {
        if (smartGamepad1.buttonLeftBumper().isHeld()
        && smartGamepad1.buttonRightBumper().isHeld()
        && smartGamepad1.triggerLeftTrigger().isHeld()
        && smartGamepad1.triggerRightTrigger().isHeld()) {
            Global.IS_EMERGENCY = true;
            smartGamepad1.rumble(0.5);
            smartGamepad2.rumble(0.5);
        } else if (smartGamepad1.buttonA().isPressed()) {
            Global.IS_EMERGENCY = false;
            smartGamepad1.rumble(0.1);
            smartGamepad2.rumble(0.1);
        }
    }
}
