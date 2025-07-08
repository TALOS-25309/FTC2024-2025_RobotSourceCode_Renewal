package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.features.TelemetrySystem;
import org.firstinspires.ftc.teamcode.part.Part;

public class Drive implements Part {
    Commands commandProcessor;

    SampleMecanumDrive sampleMecanumDrive;
    Pose2d robotPose;

    @Override
    public void init(HardwareMap hardwareMap) {
        Constants.SyncDriveConstantsWithRoadRunner();
        this.commandProcessor = new Commands(this);

        // Initialize Drive
        this.sampleMecanumDrive = new SampleMecanumDrive(hardwareMap);

        this.sampleMecanumDrive.setPoseEstimate(Constants.INITIAL_POSITION);
        sampleMecanumDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void update() {
        this.sampleMecanumDrive.update();

        // Get Current Pose
        this.robotPose = this.sampleMecanumDrive.getPoseEstimate();
        TelemetrySystem.addClassData("Drive","robot x", robotPose.getX());
        TelemetrySystem.addClassData("Drive","[drive] robot y", robotPose.getY());
        TelemetrySystem.addClassData("Drive","[drive] robot heading", robotPose.getHeading());
    }

    @Override
    public void stop() {
        this.sampleMecanumDrive.breakFollowing();
        this.command().stop();
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

    public SampleMecanumDrive drive() {
        return this.sampleMecanumDrive;
    }
}