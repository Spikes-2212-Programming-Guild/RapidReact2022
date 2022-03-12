package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

import java.util.function.Supplier;

public class MovePlacerToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move placers to next bar");

    public static final Supplier<Double> START_TIMEOUT =
            rootNamespace.addConstantDouble("start timeout", 0.2);

    public MovePlacerToNextBar(ClimberPlacer placer) {
        super(
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public void end(boolean interrupted) {}
                }.withTimeout(START_TIMEOUT.get()),
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        // Checks if the placer hit the next bar
                        return placer.isStalling();
                    }
                }
        );
    }
}
