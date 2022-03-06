package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

import java.util.function.Supplier;

/**
 * Moves the robot to the next bar. <br>
 * Must start the command <b>ONLY</b> when you are already on a bar.
 */
public class MoveToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move to next bar");

    private static final Supplier<Double> RELEASE_HOOKS_TIMEOUT = rootNamespace.addConstantDouble("release hooks timeout", 1);
    private static final Supplier<Double> RAISE_PLACER_TIMEOUT = rootNamespace.addConstantDouble("raise placer timeout", 1);

    public MoveToNextBar() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        addCommands(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED).withTimeout(RELEASE_HOOKS_TIMEOUT.get()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(ClimberPlacer.getLeftInstance(), ClimberPlacer.MIN_SPEED),
                        new MoveGenericSubsystem(ClimberPlacer.getRightInstance(), ClimberPlacer.MIN_SPEED)
                ).withTimeout(RAISE_PLACER_TIMEOUT.get()),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new MoveBothPlacersToNextBar(),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED)
        );
    }
}
