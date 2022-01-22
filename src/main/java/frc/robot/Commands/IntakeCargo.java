package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Transfer transfer = Transfer.getInstance();
        addRequirements(intakeRoller, intakePlacer, transfer);
        if (transfer.isTopPressed()) {
            addCommands(new MoveGenericSubsystem(transfer, -Transfer.SPEED).withTimeout(Transfer.RETURN_CARGO_TIME));
        }
        addCommands(
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.DOWN_SPEED),
                new ParallelCommandGroup(new MoveGenericSubsystem(intakeRoller, IntakeRoller.SPEED),
                        new MoveGenericSubsystem(transfer, Transfer.SPEED)),
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.UP_SPEED)
        );
    }
}
