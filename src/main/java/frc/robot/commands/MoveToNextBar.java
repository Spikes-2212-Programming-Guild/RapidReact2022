package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

import java.util.function.Supplier;

/**
 * Moves the winch up a little to release the hooks. Then the command moves the hooks down towards the ground. Then the
 * command extends the winch to it's full length and moves it up towards the bar until it is stalled. Then the winch
 * closes and takes the robot to the next bar.
 */
public class MoveToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move to next bar");

    private static final Supplier<Double> RELEASE_HOOKS_TIMEOUT = rootNamespace.addConstantDouble("release hooks timeout", 1);
    private static final Supplier<Double> MOVE_PLACERS_DOWN_TIMEOUT = rootNamespace.addConstantDouble("move placers down timeout", 1);

    public MoveToNextBar() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        addCommands(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED).withTimeout(RELEASE_HOOKS_TIMEOUT.get()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(ClimberPlacer.getLeftInstance(), ClimberPlacer.MIN_SPEED),
                        new MoveGenericSubsystem(ClimberPlacer.getRightInstance(), ClimberPlacer.MIN_SPEED)
                ).withTimeout(MOVE_PLACERS_DOWN_TIMEOUT.get()),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new MoveBothPlacersToNextBar(),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED)
        );
    }
}
