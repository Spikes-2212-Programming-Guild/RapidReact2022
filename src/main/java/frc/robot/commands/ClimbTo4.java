package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

import java.util.function.Supplier;

public class ClimbTo4 extends SequentialCommandGroup {
    private static final RootNamespace rootNamespace = new RootNamespace("Climb");

    private static final Supplier<Double> MOVE_PLACERS_DOWN_TIMEOUT = rootNamespace.addConstantDouble("Move placers down timeout", 0);

    private static final ClimberWinch climberWinch = ClimberWinch.getInstance();
    private static final ClimberPlacer leftClimberPlacer = ClimberPlacer.getLeftInstance();
    private static final ClimberPlacer rightClimberPlacer = ClimberPlacer.getRightInstance();

    public ClimbTo4() {
        super(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED),
                new DropBothPlacers(),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(leftClimberPlacer, ClimberPlacer.MAX_SPEED),
                        new MoveGenericSubsystem(rightClimberPlacer, ClimberPlacer.MAX_SPEED)
                ).withTimeout(MOVE_PLACERS_DOWN_TIMEOUT.get()),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(leftClimberPlacer, ClimberPlacer.MAX_SPEED),
                        new MoveGenericSubsystem(rightClimberPlacer, ClimberPlacer.MAX_SPEED)
                ).withTimeout(MOVE_PLACERS_DOWN_TIMEOUT.get()),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED),
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED)
        );
    }
}
