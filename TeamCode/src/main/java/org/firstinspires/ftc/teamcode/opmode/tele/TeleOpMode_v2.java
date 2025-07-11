package org.firstinspires.ftc.teamcode.opmode.tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

@TeleOp(group = "Telemetry")
public class TeleOpMode_v2 extends OpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Part[] part_list;

    private SmartGamepad smartGamepad1, smartGamepad2;

    private final Intake intake = new Intake();
    private final Deposit deposit = new Deposit();
    private final Drive drive = new Drive();

    private boolean lastDetecting = Global.DETECTING;

    @Override
    public void init() {
        Global.OPMODE = Global.OpMode.TELE;
        Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
        Global.ASCENDING = false;
        Global.DETECTING = false;

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

        TelemetrySystem.enableClass("Vision");
        TelemetrySystem.enableClass("Drive");
        TelemetrySystem.setDebugMode(true);
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

        // Auto Transfer for Specimen
        if(Global.TRANSFER_TYPE == Global.TransferType.SPECIMEN) {
            if (intake.isLinearSlideInside()
                    && intake.state() == IntakeState.READY_FOR_TRANSFER
                    && deposit.state() == DepositState.REST) {
                intake.command().transfer();
                deposit.command().transfer();
                deposit.command().poseForHighSpecimenScoringBackward();
            }
        }

        // Checking driver inputs
        checkEmergency();
        if(Global.IS_EMERGENCY) {
            for (Part part : part_list) {
                part.stop();
            }
        } else {
            controlGamepad1(smartGamepad1);
            controlGamepad2(smartGamepad2);
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

    public void controlGamepad1(SmartGamepad gamepad) {
        // Controlling Drive Part
        drive.command().drive(
                Math.pow(gamepad.triggerLeftStickX().getValue(), 3),
                -Math.pow(gamepad.triggerLeftStickY().getValue(), 3),
                (gamepad.triggerRightTrigger().getValue()
                        - gamepad.triggerLeftTrigger().getValue()) * 0.4
        );

        if(Global.ASCENDING) {
            if(gamepad.buttonPS().isPressed()) {
                if (deposit.state() == DepositState.ASCENDING) {
                    deposit.command().ascendingReady();
                } else {
                    deposit.command().ascend();
                }
            }
            return;
        }

        if (gamepad.buttonLeftStick().isPressed()) {
            if (Global.TRANSFER_TYPE == Global.TransferType.SPECIMEN)
                Global.TRANSFER_TYPE = Global.TransferType.SAMPLE;
            else
                Global.TRANSFER_TYPE = Global.TransferType.SPECIMEN;
        }

        // Controlling Intake & Deposit Parts
        if (intake.state() == IntakeState.READY_FOR_PICKUP
                || intake.state() == IntakeState.PICKED_UP) { // Manual Control (Only Linear)
            intake.command().movePositiondXdY(
                    0,
                    -gamepad.triggerRightStickY().getValue() * 1.3
            );
        }
        if (gamepad.buttonCircle().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_PICKUP) { // Auto Pickup (Yellow Sample)
                intake.command().automaticTargetForYellowSample();
            }
        }
        if (gamepad.buttonSquare().isPressed()) {
            if (intake.state() == IntakeState.READY_FOR_PICKUP) { // Auto Pickup (Alliance Sample)
                intake.command().automaticTargetForAllianceSample();
            }
        }
        if (gamepad.buttonTriangle().isPressed()) { // Integrated Control
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
        if (gamepad.buttonCross().isPressed()) { // Discard
            deposit.command().discard();
        }

        // Controlling Ascending
        if (gamepad.buttonPS().isPressed()) {
            Global.ASCENDING = true;
            resetParts();
            deposit.command().ascendingReady();
        }
    }

    public void controlGamepad2(SmartGamepad gamepad) {
        if(Global.ASCENDING)
            return;

        // Controlling Intake Part
        if (gamepad.buttonDPadUp().isPressed()) { // UP : Manual Pickup
            if (intake.state() == IntakeState.READY_FOR_PICKUP){
                intake.command().pickup();
            }
        }
        if (gamepad.buttonDPadDown().isPressed()) {
            // DOWN : Compact Ready (Manual Control) (= Intake Discard)
            intake.command().compactReady();
        }
        if (intake.state() == IntakeState.READY_FOR_PICKUP) { // Manual Control
            int left = gamepad.buttonLeftBumper().isHeld() ? 1 : 0;
            int right = gamepad.buttonRightBumper().isHeld() ? 1 : 0;
            intake.command().setPositionDelta(
                    gamepad.triggerLeftStickX().getValue(),
                    -gamepad.triggerLeftStickY().getValue(),
                    right-left
            );
        }

        // Controlling Deposit Part
        if (gamepad.buttonTriangle().isPressed()) { // TRIANGLE : Pickup Specimen
            if (deposit.state() == DepositState.REST) {
                deposit.command().poseForSpecimenPickup();
            }
        }
        if (gamepad.buttonCircle().isPressed()) { // CIRCLE : Pose for High Scoring (Only Basket)
            if (deposit.state() == DepositState.REST
                    && intake.state() == IntakeState.READY_FOR_TRANSFER
                    && Global.TRANSFER_TYPE.equals(Global.TransferType.SAMPLE)) {
                intake.command().transfer();
                deposit.command().transfer();
                deposit.command().poseForHighBasketScoring();
            } else if (deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
                deposit.command().poseForHighBasketScoring();
            }
        }
        if (gamepad.buttonSquare().isPressed()) { // SQUARE : Pose for Low Scoring (Only Basket)
            if (deposit.state() == DepositState.REST
                    && intake.state() == IntakeState.READY_FOR_TRANSFER
                    && Global.TRANSFER_TYPE.equals(Global.TransferType.SAMPLE)) {
                intake.command().transfer();
                deposit.command().transfer();
                deposit.command().poseForLowBasketScoring();
            } else if (deposit.state() == DepositState.READY_FOR_DEPOSIT_BASKET) {
                deposit.command().poseForLowBasketScoring();
            }
        }
        if (gamepad.buttonCross().isPressed()) { // CROSS : Discard
            intake.command().drop();
        }
    }

    public void checkEmergency() {
        if ((smartGamepad1.buttonLeftBumper().isHeld()
                && smartGamepad1.buttonRightBumper().isHeld()
                && smartGamepad1.triggerLeftTrigger().isHeld()
                && smartGamepad1.triggerRightTrigger().isHeld())
            || (smartGamepad2.buttonLeftBumper().isHeld()
                && smartGamepad2.buttonRightBumper().isHeld()
                && smartGamepad2.triggerLeftTrigger().isHeld()
                && smartGamepad2.triggerRightTrigger().isHeld())) {
            if(!Global.IS_EMERGENCY) {
                smartGamepad1.rumble(0.5);
                smartGamepad2.rumble(0.5);
                SmartMotor.emergencyStop();
                SmartServo.emergencyStop();
            }
            Global.IS_EMERGENCY = true;
        } else {
            if(Global.IS_EMERGENCY) {
                SmartServo.normalState();
                resetParts();
                smartGamepad1.rumble(0.1);
                smartGamepad2.rumble(0.1);
            }
            Global.IS_EMERGENCY = false;
        }
    }

    public void resetParts(){
        intake.command().compactReady();
        deposit.command().rest();
        drive.command().stop();
    }
}
