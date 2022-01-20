package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;

public class IntakeCargo extends SequentialCommandGroup {

    private IntakeRoller intakeRoller = IntakeRoller.getInstance();
    private IntakePlacer intakePlacer = IntakePlacer.getInstance();

    public IntakeCargo() {
        addRequirements(intakeRoller, intakePlacer);
        addCommands(
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.DOWN_SPEED),
                new MoveGenericSubsystem(intakeRoller, IntakeRoller.SPEED),
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.UP_SPEED)
        );
    }
}
