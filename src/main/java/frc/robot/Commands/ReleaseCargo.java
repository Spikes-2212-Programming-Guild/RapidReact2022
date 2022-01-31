package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Subsystems.IntakeToTranfer;
import frc.robot.Subsystems.Transfer;

/**
 * A command that releases cargos from inside the robot.
 */
public class ReleaseCargo extends ParallelCommandGroup {

    public ReleaseCargo() {
        Transfer transfer = Transfer.getInstance();
        IntakeToTranfer intakeToTranfer = IntakeToTranfer.getInstance();
        addCommands(
                new MoveGenericSubsystem(transfer, Transfer.SPEED),
                new MoveGenericSubsystem(intakeToTranfer, IntakeToTranfer.SPEED)
        );
    }
}
