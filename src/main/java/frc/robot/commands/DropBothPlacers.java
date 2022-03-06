package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

public class DropBothPlacers extends ParallelCommandGroup {

    public DropBothPlacers() {
        addCommands(
                new DropPlacer(ClimberPlacer.getLeftInstance()),
                new DropPlacer(ClimberPlacer.getRightInstance())
        );
    }
}
