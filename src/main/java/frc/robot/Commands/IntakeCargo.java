package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;

public class IntakeCargo extends SequentialCommandGroup {

    private IntakeRoller intakeRoller = IntakeRoller.getInstance();
    private IntakePlacer intakePlacer = IntakePlacer.getInstance();

    public IntakeCargo() {
        addCommands(
                new MoveGenericSubsystem(intakePlacer, intakePlacer.DOWN_SPEED),
                new MoveGenericSubsystem(intakeRoller, 0.4),
                new MoveGenericSubsystem(intakePlacer, intakePlacer.UP_SPEED)
        );
    }
}
