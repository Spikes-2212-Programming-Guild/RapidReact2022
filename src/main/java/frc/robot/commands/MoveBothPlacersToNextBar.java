package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

public class MoveBothPlacersToNextBar extends ParallelCommandGroup {

    public MoveBothPlacersToNextBar() {
        addCommands(
                new MovePlacerToNextBar(ClimberPlacer.getLeftInstance()),
                new MovePlacerToNextBar(ClimberPlacer.getRightInstance())
        );
    }
}
