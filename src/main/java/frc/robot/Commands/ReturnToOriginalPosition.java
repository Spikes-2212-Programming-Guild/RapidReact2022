package frc.robot.Commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Drivetrain;

import java.util.ArrayList;

public class ReturnToOriginalPosition extends CommandBase {
    //yea i don't really know
    private static final double MAX_VELOCITY = 200;
    private static final double MAX_ACCELERATION = 500;

    private final FollowTrajectory followTrajectory;

    public ReturnToOriginalPosition() {
        Drivetrain drivetrain = Drivetrain.getInstance();

        double startX = drivetrain.getPose().getX();
        double startY = drivetrain.getPose().getY();
        Rotation2d startRotation = drivetrain.getPose().getRotation();

        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(new Pose2d(startX, startY, startRotation),
                new ArrayList<Translation2d>(), new Pose2d(0, 0, startRotation),
                new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION));

        followTrajectory = new FollowTrajectory(drivetrain, trajectory, drivetrain.getPIDSettings(),
                drivetrain.getPIDSettings(), drivetrain.getFFSettings());
    }

    @Override
    public void initialize() {
        followTrajectory.initialize();
    }

    @Override
    public void execute() {
        followTrajectory.execute();
    }

    @Override
    public void end(boolean interrupted) {
        followTrajectory.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return followTrajectory.isFinished();
    }
}
