package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class Commands {
    private final Drive drive;

    public Commands(Drive drive) {
        this.drive = drive;
    }

    public void drive(double x, double y, double omega) {
        drive.sampleMecanumDrive.setWeightedDrivePower(
                new Pose2d(
                        y * Constants.directionSign,
                        -x * Constants.directionSign,
                        -omega
                )
        );
    }

    public void followTrajectory(TrajectorySequence trajectory) {
        drive.sampleMecanumDrive.followTrajectorySequence(trajectory);
    }

    public void changeDirection() {
        Constants.directionSign *= -1.0;
    }
}