package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;

public class DropPlacers extends ParallelCommandGroup {

    public DropPlacers() {
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addRequirements(
                leftPlacer,
                rightPlacer
        );
        addCommands(
                new MoveGenericSubsystem(leftPlacer, leftPlacer.getDropSpeed()),
                new MoveGenericSubsystem(rightPlacer, rightPlacer.getDropSpeed())
        );
    }
}
