package frc.robot.Subsystems;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/**
 * This is a drivetrain which includes an odometry, as well as the methods required to follow a trajectory.
 * For a simple example of an implementation, see
 * <a href=https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/trajectory-tutorial/
 * creating-drive-subsystem.html>here</a>.
 */
public abstract class OdometryDrivetrain extends TankDrivetrain {

    public OdometryDrivetrain(MotorControllerGroup left, MotorControllerGroup right) {
        super(left, right);
    }

    /**
     * Updates the odometry, the field2d and the dashboard.
     **/
    public abstract void periodic();

    /**
     * Returns the robot's current Pose2d, which includes its current position and rotation.
     **/
    public abstract Pose2d getPose();

    /**
     * returns the current wheel speeds of the robot
     **/
    public abstract DifferentialDriveWheelSpeeds getWheelSpeeds();

    /**
     * returns the robot's odometry and sensors
     **/
    public abstract void resetOdometry(Pose2d pose);

    /**
     * returns a Rotation2d object based on the robot's current angle
     **/
    public abstract Rotation2d rotFromAngle();

    /**
     * sets the speed controllers' voltage
     *
     * @param leftVolts  the left speed controller's voltage
     * @param rightVolts the right speed controller's voltage
     **/
    public abstract void tankDriveVolts(double leftVolts, double rightVolts);

    public abstract double getWidth();

    public abstract PIDSettings getPIDSettings();

    public abstract FeedForwardSettings getFFSettings();
}
