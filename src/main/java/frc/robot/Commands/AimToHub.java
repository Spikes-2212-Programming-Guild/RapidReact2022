package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;


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
