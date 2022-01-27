package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;
/**
 * <p>
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 * </p>
 *
 * @author Yoel Perman Brilliant
 */
public class AimToHub extends SequentialCommandGroup {
    private static final int LIMELIGHT_PIPELINE = 1;

    public AimToHub() {
        Dispenser dispenser = Dispenser.getInstance();
        Drivetrain drivetrain = Drivetrain.getInstance();
        int initialPipeline = dispenser.getLimelight().getPipeline();
        dispenser.getLimelight().setPipeline(LIMELIGHT_PIPELINE);
        addCommands(
                new DriveArcadeWithPID(drivetrain, dispenser.getLimelight()::getHorizontalOffsetFromTarget,
                        () -> 0.0, () -> 0.0, drivetrain.getAimToHubPID(), drivetrain.getAimToHubFeedForward())
        );
        dispenser.getLimelight().setPipeline(initialPipeline);
    }
}
