package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import frc.robot.Subsystems.Drivetrain;
import com.spikes2212.util.Limelight;
import frc.robot.Subsystems.Transfer;

/**
 * This command moves the {@link Drivetrain} till either its gyro angle is zero or the
 * {@link Limelight} has a valid target.
 *
 * @author Ofri Rosenbaum
 */
public class ReturnByGyro extends DriveArcadeWithPID {

    private static final double SPEED = 0.7;
    private static final int LIMELIGHT_PIPELINE = 1;

    private int previousPipeline;

    private Limelight limelight;

    public ReturnByGyro(Drivetrain drivetrain) {
        super(drivetrain, drivetrain::getGyroAngle, 0, SPEED, drivetrain.getPIDSettings(),
                drivetrain.getFeedForwardSettings());
        this.limelight = Transfer.getInstance().getLimelight();
    }

    @Override
    public void initialize() {
        previousPipeline = limelight.getPipeline();
        limelight.setPipeline(LIMELIGHT_PIPELINE);
    }

    @Override
    public boolean isFinished() {
        return limelight.isOnTarget() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        limelight.setPipeline(previousPipeline);
    }
}
