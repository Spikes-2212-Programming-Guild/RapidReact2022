package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Transfer;

/**
 * <p>
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 * </p>
 *
 * @author Yoel Perman Brilliant
 */
public class AimToHub extends SequentialCommandGroup {
    private static final int LIMELIGHT_PIPELINE = 1;

    private final Transfer transfer = Transfer.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final int INITIAL_PIPE_LINE = transfer.getLimelight().getPipeline();


    public AimToHub() {
        addCommands(
                new DriveArcadeWithPID(drivetrain, transfer.getLimelight()::getHorizontalOffsetFromTarget,
                        () -> 0.0, () -> 0.0, drivetrain.getAimToHubPID(), drivetrain.getAimToHubFeedForward())
        );
    }

    @Override
    public void initialize() {
        transfer.getLimelight().setPipeline(LIMELIGHT_PIPELINE);
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        transfer.getLimelight().setPipeline(INITIAL_PIPE_LINE);
    }
}
