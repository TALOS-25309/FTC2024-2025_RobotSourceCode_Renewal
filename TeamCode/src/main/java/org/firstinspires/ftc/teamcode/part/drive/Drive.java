package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class Drive implements Part {
    private Telemetry telemetry;

    private SampleMecanumDrive drive;
    private Pose2d robotPose;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        // Initialize Drive
        this.drive = new SampleMecanumDrive(hardwareMap);

        this.drive.setPoseEstimate(DriveConstants.INITIAL_POSITION);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void update() {
        this.drive.update();

        // Get Current Pose
        this.robotPose = this.drive.getPoseEstimate();
        telemetry.addData("x", robotPose.getX());
        telemetry.addData("y", robotPose.getY());
        telemetry.addData("heading", robotPose.getHeading());
    }

    public void stop() {
        this.drive.breakFollowing();
    }

    public TrajectoryBuilder trajectoryBuilder() {
        return this.drive.trajectoryBuilder(this.robotPose);
    }

    public boolean isBusy() {
        return this.drive.isBusy();
    }

    public void cmdDrive(double x, double y, double omega) {
        drive.setWeightedDrivePower(
                new Pose2d(
                        y * DriveConstants.directionSign,
                        -x * DriveConstants.directionSign,
                        -omega
                )
        );
    }

    public void cmdFollowTrajectory(TrajectorySequence trajectory) {
        this.drive.followTrajectorySequence(trajectory);
    }

    public void cmdChangeDirection() {
        DriveConstants.directionSign *= -1.0;
    }
}