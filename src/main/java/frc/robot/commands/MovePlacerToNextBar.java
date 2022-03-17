package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

import java.util.function.Supplier;

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
