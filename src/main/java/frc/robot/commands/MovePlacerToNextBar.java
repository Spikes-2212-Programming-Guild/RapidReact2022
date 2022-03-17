package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

import java.util.function.Supplier;

public class MovePlacerToNextBar extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("move placers to next bar");

    public static final Supplier<Double> INITIAL_MOVE_DURATION =
            rootNamespace.addConstantDouble("initial move duration", 0.2);

    public MovePlacerToNextBar(ClimberPlacer placer) {
        super(
                /*
                  When the placer starts moving, its initial speed will be close to 0, and thus the placer will be
                  considered in stall. As a result, we first want to move the placer in order for it to obtain
                  velocity.
                 */
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public void end(boolean interrupted) {
                    }
                }.withTimeout(INITIAL_MOVE_DURATION.get()),
                new MoveGenericSubsystem(placer, placer.RAISE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        // Checks if the placer hit the next bar
                        return placer.hasHitNextBar();
                    }
                }
        );
    }
}
