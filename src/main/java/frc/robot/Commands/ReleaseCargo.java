package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.Transfer;

public class ReleaseCargo extends SequentialCommandGroup {

    public ReleaseCargo() {
        super(
                new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().MOVE_SPEED).withTimeout(0.3),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                        new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().RELEASE_SPEED)
                )
        );
    }
}
