package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;
import com.spikes2212.util.Limelight;

/**
 * This command moves the {@link Drivetrain} till either its gyro angle is zero or the
 * {@link Limelight} has a valid target.
 *
 * @author Ofri Rosenbaum
 */
public class ReturnByGyro extends CommandBase {

    public static final double SPEED = 0.7;
    public static final int LIMELIGHT_PIPELINE = 1;

    /**
     * The gyro's tolerance.
     */
    public static final double TOLERANCE = 2;

    public PIDSettings pidSettings;
    public FeedForwardSettings feedForwardSettings;

    private Drivetrain drivetrain;
    private Dispenser dispenser;

    private int previousPipeline;

    public ReturnByGyro(PIDSettings pidSettings, FeedForwardSettings feedForwardSettings) {
        this.drivetrain = Drivetrain.getInstance();
        this.dispenser = Dispenser.getInstance();
        this.pidSettings = pidSettings;
        this.feedForwardSettings = feedForwardSettings;
    }

    @Override
    public void initialize() {
        previousPipeline = dispenser.getLimelightPipeline();
        dispenser.setLimelightPipeline(LIMELIGHT_PIPELINE);
    }

    @Override
    public void execute() {
        new DriveArcadeWithPID(drivetrain, drivetrain::getGyroAngle, 0.0, SPEED, pidSettings,
                feedForwardSettings);
    }

    @Override
    public boolean isFinished() {
        return dispenser.isOnTarget() || Math.abs(drivetrain.getGyroAngle()) <= TOLERANCE;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
        dispenser.setLimelightPipeline(previousPipeline);
    }
}
