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

        // Checking driver inputs
        checkEmergency();
        if(!Global.IS_EMERGENCY) {
            controlGamepad1(smartGamepad1);
            controlGamepad2(smartGamepad2);
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

    public void controlGamepad1(SmartGamepad gamepad) {
        // Controlling Drive Part
        drive.command().drive(
                Math.pow(gamepad.triggerLeftStickX().getValue(), 3),
                -Math.pow(gamepad.triggerLeftStickY().getValue(), 3),
                (gamepad.triggerRightTrigger().getValue()
                        - gamepad.triggerLeftTrigger().getValue()) * 0.4
        );

        // Controlling Intake & Deposit Parts
        if (intake.state() == IntakeState.READY_TO_PICKUP
                || intake.state() == IntakeState.PICKED_UP) { // Manual Control (Only Linear)
            intake.command().movePositiondXdY(
                    0,
                    -gamepad.triggerLeftStickY().getValue() * 2.0
            );
        }
        if (gamepad.buttonCircle().isPressed()) {
            if (intake.state() == IntakeState.READY_TO_PICKUP) { // Auto Pickup (Yellow Sample)
                intake.command().automaticTargetForYellowSample();
            }
        }
        if (gamepad.buttonSquare().isPressed()) {
            if (intake.state() == IntakeState.READY_TO_PICKUP) { // Auto Pickup (Alliance Sample)
                intake.command().automaticTargetForAllianceSample();
            }
        }
        if (gamepad.buttonTriangle().isPressed()) { // Integrated Control
            if(intake.state() == IntakeState.READY_FOR_TRANSFER) {
                intake.command().drop();
            }
            if (deposit.state() == DepositState.READY_TO_PICKUP) {
                deposit.command().pickupSpecimen();
            } else if (deposit.state() == DepositState.READY_TO_DEPOSIT_BASKET) {
                deposit.command().scoringBasket();
            } else if (deposit.state() == DepositState.READY_TO_DEPOSIT_SPECIMEN) {
                deposit.command().scoringSpecimen();
            } else if (deposit.state() == DepositState.READY_TO_DISCARD) {
                deposit.command().discard();
            }
        }
        if (gamepad.buttonCross().isPressed()) { // Discard
            deposit.command().discard();
        }
    }

    public void controlGamepad2(SmartGamepad gamepad) {
        // Controlling Intake Part
        if (gamepad.buttonDPadUp().isPressed()) { // UP : Manual Pickup
            if (intake.state() == IntakeState.READY_TO_PICKUP){
                intake.command().pickup();
            }
        }
        if (gamepad.buttonDPadDown().isPressed()) {
            // DOWN : Compact Ready (Manual Control) (= Intake Discard)
            intake.command().compactReady();
        }
        if (intake.state() == IntakeState.READY_TO_PICKUP) { // Manual Control
            intake.command().movePositiondXdY(
                    smartGamepad1.triggerLeftStickX().getValue(),
                    -smartGamepad1.triggerLeftStickY().getValue()
            );
            int left = smartGamepad1.buttonLeftBumper().isHeld() ? 1 : 0;
            int right = smartGamepad1.buttonRightBumper().isHeld() ? 1 : 0;
            intake.command().rotateDeltaOrientation(right-left);
        }

        // Controlling Deposit Part
        if (gamepad.buttonTriangle().isPressed()) { // TRIANGLE : Pickup Specimen
            if (deposit.state() == DepositState.REST) {
                deposit.command().poseForSpecimenPickup();
            }
        }
        if (gamepad.buttonCircle().isPressed()) { // CIRCLE : Pose for High Scoring (Only Basket)
            if (deposit.state() == DepositState.REST
                    && intake.state() == IntakeState.READY_FOR_TRANSFER) {
                intake.command().transfer();
                deposit.command().transfer();
                deposit.command().poseForHighBasketScoring();
            }
        }
        if (gamepad.buttonSquare().isPressed()) { // SQUARE : Pose for Low Scoring (Only Basket)
            if (deposit.state() == DepositState.REST
                    && intake.state() == IntakeState.READY_FOR_TRANSFER) {
                intake.command().transfer();
                deposit.command().transfer();
                deposit.command().poseForLowBasketScoring();
            }
        }
        if (gamepad.buttonCross().isPressed()) { // CROSS : Discard
            deposit.command().poseForDiscard();
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
