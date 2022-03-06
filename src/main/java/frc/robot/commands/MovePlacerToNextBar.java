package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

import java.util.function.Supplier;

public class MovePlacerToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move placers to next bar");

    public static final Supplier<Double> GET_OVER_STALL_TIMEOUT =
            rootNamespace.addConstantDouble("get over stall timeout", 0.2);

    public MovePlacerToNextBar(ClimberPlacer placer) {
        super(
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public void end(boolean interrupted) {}
                }.withTimeout(GET_OVER_STALL_TIMEOUT.get()),
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return placer.isStalling();
                    }
                }
        );
    }
}
