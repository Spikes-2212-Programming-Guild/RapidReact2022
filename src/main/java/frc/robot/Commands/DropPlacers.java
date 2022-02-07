package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;

public class DropPlacers extends ParallelCommandGroup {

    public DropPlacers() {
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addCommands(
                new DropPlacer(leftPlacer),
                new DropPlacer(rightPlacer)
        );
    }
}
