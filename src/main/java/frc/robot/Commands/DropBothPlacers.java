package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;

public class DropBothPlacers extends ParallelCommandGroup {

    public DropBothPlacers() {
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addRequirements(leftPlacer, rightPlacer);
        addCommands(
                new DropPlacer(leftPlacer),
                new DropPlacer(rightPlacer)
        );
    }
}
