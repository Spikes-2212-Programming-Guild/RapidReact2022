package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.ClimberWinch;

/**
 * Moves the dynamic climbers downward towards the ground until they hit the third bar.
 */
public class MoveBetweenBars extends SequentialCommandGroup {

    public MoveBetweenBars() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        addCommands(
                new MoveGenericSubsystem(climberWinch, climberWinch.getUpSpeed()),
                new DropBothPlacers(),
                new CloseTelescopic()
        );
    }
}
