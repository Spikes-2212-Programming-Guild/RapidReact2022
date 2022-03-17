package frc.robot.commands.climbing;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.subsystems.ClimberPlacer;

/**
 * Moves a {@code ClimberPlacer} instance until it reaches the next bar.
 *
 * @see ClimberPlacer
 */
public class MovePlacerToNextBar extends MoveGenericSubsystem {

    public MovePlacerToNextBar(ClimberPlacer placer) {
        super(placer, placer.RAISE_SPEED);
    }

    @Override
    public boolean isFinished() {
        ClimberPlacer climberPlacer = (ClimberPlacer) subsystem;
        return climberPlacer.hasHitNextBar();
    }
}
