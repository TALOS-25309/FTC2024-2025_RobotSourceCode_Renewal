package org.firstinspires.ftc.teamcode.part.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class Commands {
    private final Drive drive;

    public Commands(Drive drive) {
        this.drive = drive;
    }

    /**
     * Drives the robot using mecanum drive.
     *
     * @param x     The x component of the drive power (left/right).
     * @param y     The y component of the drive power (forward/backward).
     * @param omega The rotational component of the drive power (turning).
     */
    public void drive(double x, double y, double omega) {
        drive.sampleMecanumDrive.setWeightedDrivePower(
                new Pose2d(
                        y * Constants.directionSign,
                        -x * Constants.directionSign,
                        -omega
                )
        );
    }

    public void stop() {
        drive.sampleMecanumDrive.setWeightedDrivePower(new Pose2d(0, 0, 0));
    }

    /**
     * Follow the given trajectory sequence with RoadRunner.
     *
     * @param trajectory
     */
    public void followTrajectory(TrajectorySequence trajectory) {
        drive.sampleMecanumDrive.followTrajectorySequenceAsync(trajectory);
    }

    /**
     * Changes the robot's facing direction.
     */
    public void changeDirection() {
        Constants.directionSign *= -1.0;
    }
}