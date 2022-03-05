package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberWinch;

/**
 * Moves the dynamic climbers downward towards the ground until they hit the third bar.
 */
public class MoveBetweenBars extends SequentialCommandGroup {

    public MoveBetweenBars() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        addCommands(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new DropBothPlacers(),
                new MoveGenericSubsystem(ClimberWinch.getInstance(), ClimberWinch.DOWN_SPEED)
        );
    }
}
