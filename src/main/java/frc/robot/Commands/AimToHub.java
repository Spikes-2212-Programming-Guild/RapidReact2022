package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;

/**
 * <p>
 * A command which causes the robot to rotate in place until it centers on one of the
 * reflective sprites that are on the hub.
 * </p>
 * @author Yoel Perman Brilliant
 */
public class AimToHub extends SequentialCommandGroup {
    private static final int LIMELIGHT_PIPE = 1;

    public AimToHub() {
        Dispenser dispenser = Dispenser.getInstance();
        Drivetrain drivetrain = Drivetrain.getInstance();
        addCommands(
                new InstantCommand(() -> dispenser.limelight.setPipeline(LIMELIGHT_PIPE)),
                new DriveArcadeWithPID(drivetrain, dispenser.limelight::getHorizontalOffsetFromTarget,
                        () -> 0.0, () -> 0.0, drivetrain.aimToHubPID, drivetrain.aimToHubFeedForward)
        );
    }


}