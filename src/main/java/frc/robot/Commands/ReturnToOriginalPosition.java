package frc.robot.Commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.Subsystems.Drivetrain;

import java.util.ArrayList;

public class ReturnToOriginalPosition extends FollowTrajectory {

    private static final double MAX_VELOCITY = 200;
    private static final double MAX_ACCELERATION = 500;

    public ReturnToOriginalPosition(Drivetrain drivetrain) {
        super(drivetrain, getTrajectory(drivetrain), drivetrain.getPIDSettings(), drivetrain.getPIDSettings(),
                drivetrain.getFFSettings());
    }

    private static Trajectory getTrajectory(Drivetrain drivetrain) {
        final double START_X = drivetrain.getPose().getX();
        final double START_Y = drivetrain.getPose().getY();
        final Rotation2d START_ROTATION = drivetrain.getPose().getRotation();

        return TrajectoryGenerator.generateTrajectory(new Pose2d(START_X, START_Y, START_ROTATION),
                new ArrayList<Translation2d>(), new Pose2d(0, 0, START_ROTATION),
                new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION));
    }
}
