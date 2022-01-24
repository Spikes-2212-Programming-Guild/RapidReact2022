package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;
import com.spikes2212.util.Limelight;

/**
 * This command moves the {@link Drivetrain} till either its gyro angle is zero or the
 * {@link Limelight} has a valid target.
 *
 * @author Ofri Rosenbaum
 */
public class ReturnByGyro extends DriveArcadeWithPID {

    private static final double SPEED = 0.7;
    private static final int LIMELIGHT_PIPELINE = 1;

    /**
     * The gyro's tolerance.
     */
    private static final double TOLERANCE = 2;

    private PIDSettings pidSettings;
    private FeedForwardSettings feedForwardSettings;

    private Dispenser dispenser;

    private int previousPipeline;

    public ReturnByGyro(Drivetrain drivetrain, PIDSettings pidSettings, FeedForwardSettings feedForwardSettings) {
        super(drivetrain, drivetrain::getGyroAngle, 0, SPEED, pidSettings, feedForwardSettings);
        this.dispenser = Dispenser.getInstance();
    }

    @Override
    public void initialize() {
        previousPipeline = dispenser.getLimelightPipeline();
        dispenser.setLimelightPipeline(LIMELIGHT_PIPELINE);
    }

    @Override
    public boolean isFinished() {
        return dispenser.isOnTarget() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        dispenser.setLimelightPipeline(previousPipeline);
    }
}
