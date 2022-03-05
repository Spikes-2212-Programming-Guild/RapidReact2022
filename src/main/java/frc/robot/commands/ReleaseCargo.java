package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

public class ReleaseCargo extends SequentialCommandGroup {

    public static final double RELEASE_FIRST_CARGO_TIMEOUT = 0.5;

    public ReleaseCargo() {
        Transfer transfer = Transfer.getInstance();
        addCommands(
                new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MIN_SPEED),
                new MoveGenericSubsystem(transfer, transfer.FIRST_CARGO_RELEASE_SPEED).withTimeout(RELEASE_FIRST_CARGO_TIMEOUT),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                        new MoveGenericSubsystem(transfer, transfer.SECOND_CARGO_RELEASE_SPEED)
                )
        );
    }
}
