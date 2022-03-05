package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

public class DropBothPlacers extends ParallelCommandGroup {

    public DropBothPlacers() {
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addCommands(
                new DropPlacer(leftPlacer),
                new DropPlacer(rightPlacer)
        );
    }
}