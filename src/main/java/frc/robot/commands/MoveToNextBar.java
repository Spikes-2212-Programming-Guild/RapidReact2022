package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

import java.util.function.Supplier;

/**
 * Moves the robot to the next bar. <br>
 * Steps: <br>
 * 1. Moves the winch up to release the hooks from the bar.<br>
 * 2. Moves the placers downwards.<br>
 * 3. Extends the winch to it's full extent.<br>
 * 4. Raises the placers until they hit the next bar and go into a stall.<br>
 * 5. Closes the winch to move the robot to the next bar.<br>
 * Must start the command <b>ONLY</b> when you are already on a bar.
 */
public class MoveToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move to next bar");

    private static final Supplier<Double> RELEASE_HOOKS_TIMEOUT = rootNamespace.addConstantDouble("release hooks timeout", 1);
    private static final Supplier<Double> DROP_PLACERS_TIMEOUT = rootNamespace.addConstantDouble("drop placers timeout", 1);
    private static final Supplier<Double> HOOK_BAR_TIMEOUT = rootNamespace.addConstantDouble("hook bar timeout", 1);

    public MoveToNextBar() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addCommands(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED).withTimeout(RELEASE_HOOKS_TIMEOUT.get()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(leftPlacer, ClimberPlacer.MIN_SPEED),
                        new MoveGenericSubsystem(rightPlacer, ClimberPlacer.MIN_SPEED)
                ).withTimeout(DROP_PLACERS_TIMEOUT.get()),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new InstantCommand(() -> {
                    leftPlacer.setIdleMode(IdleMode.kBrake);
                    rightPlacer.setIdleMode(IdleMode.kBrake);
                }),
                new MoveBothPlacersToNextBar(),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED).withTimeout(HOOK_BAR_TIMEOUT.get()),
                new InstantCommand(() -> {
                    leftPlacer.setIdleMode(IdleMode.kBrake);
                    rightPlacer.setIdleMode(IdleMode.kBrake);
                }),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED)
        );
    }
}
