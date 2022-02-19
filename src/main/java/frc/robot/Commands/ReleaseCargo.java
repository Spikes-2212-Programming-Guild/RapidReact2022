package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.Transfer;

public class ReleaseCargo extends ParallelCommandGroup {

    public ReleaseCargo() {
        super(
                new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().getTransferReleaseSpeed())
        );
    }
}
