package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;
import frc.robot.Subsystems.ClimberWinch;

/**
 * Moves the dynamic climbers downward towards the ground until they hit the third bar.
 */
public class MoveBetweenBars extends SequentialCommandGroup {

    public MoveBetweenBars() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        ClimberPlacer leftClimberPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightClimberPlacer = ClimberPlacer.getRightInstance();
        addRequirements(climberWinch, leftClimberPlacer, rightClimberPlacer);
        addCommands(
                new MoveGenericSubsystem(climberWinch, climberWinch.getUpSpeed()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(leftClimberPlacer, leftClimberPlacer.getUpSpeed()),
                        new MoveGenericSubsystem(rightClimberPlacer, rightClimberPlacer.getUpSpeed())
                ),
                new MoveGenericSubsystem(climberWinch, climberWinch.getDownSpeed()),
                new MoveGenericSubsystem(climberWinch, climberWinch.getHookedDownSpeed())
        );
    }
}
