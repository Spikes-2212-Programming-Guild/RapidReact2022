package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

public class ReleaseCargo extends ParallelCommandGroup {

    public ReleaseCargo() {
        super(
                new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().getTransferSpeed())
        );
    }
}
