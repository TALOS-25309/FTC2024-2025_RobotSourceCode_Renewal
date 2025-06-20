package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.part.Part;

public class Drive implements Part {
    Telemetry telemetry;
    Commands commandProcessor;

    SampleMecanumDrive sampleMecanumDrive;
    Pose2d robotPose;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        Constants.SyncDriveConstantsWithRoadRunner();
        this.telemetry = telemetry;
        this.commandProcessor = new Commands(this);

        // Initialize Drive
        this.sampleMecanumDrive = new SampleMecanumDrive(hardwareMap);

        this.sampleMecanumDrive.setPoseEstimate(Constants.INITIAL_POSITION);
        sampleMecanumDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update() {
        this.sampleMecanumDrive.update();

        // Get Current Pose
        this.robotPose = this.sampleMecanumDrive.getPoseEstimate();
        telemetry.addData("x", robotPose.getX());
        telemetry.addData("y", robotPose.getY());
        telemetry.addData("heading", robotPose.getHeading());
    }

    public void stop() {
        this.sampleMecanumDrive.breakFollowing();
    }

    public TrajectoryBuilder trajectoryBuilder() {
        return this.sampleMecanumDrive.trajectoryBuilder(this.robotPose);
    }

    public boolean isBusy() {
        return this.sampleMecanumDrive.isBusy();
    }

    public Commands command() {
        return this.commandProcessor;
    }
}