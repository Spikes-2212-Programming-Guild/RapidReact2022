package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;
import frc.robot.Subsystems.ClimberWinch;

import java.util.function.Supplier;

/**
 * Moves the dynamic climbers downward towards the ground until they hit the third bar.
 */
public class MoveBetweenBars extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move between bars");

    /**
     * A value between the running current and the stalling current of the motors.
     */
    private static final Supplier<Double> STALL_CURRENT = rootNamespace.addConstantDouble("stall current", 0);
    private static final Supplier<Double> WINCH_MOVEMENT_SPEED = rootNamespace.addConstantDouble("winch movement speed", 0);
    private static final Supplier<Double> PLACER_MOVEMENT_SPEED = rootNamespace.addConstantDouble("placer movement speed", 0);

    public MoveBetweenBars() {
        super(new MoveGenericSubsystem(ClimberWinch.getInstance(), WINCH_MOVEMENT_SPEED),
              new ParallelCommandGroup(new MoveGenericSubsystem(ClimberPlacer.getLeftInstance(), PLACER_MOVEMENT_SPEED),
                                       new MoveGenericSubsystem(ClimberPlacer.getRightInstance(), PLACER_MOVEMENT_SPEED)));
    }
}
