package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberPlacer;

public class MoveBothPlacersToNextBar extends ParallelCommandGroup {

    public MoveBothPlacersToNextBar() {
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addCommands(
                new MovePlacerToNextBar(leftPlacer),
                new MovePlacerToNextBar(rightPlacer)
        );
    }
}
