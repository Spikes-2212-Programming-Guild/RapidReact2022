package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

public class ReleaseCargo extends SequentialCommandGroup {

    public ReleaseCargo() {
        super(
                new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MIN_SPEED),
                new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().FIRST_CARGO_RELEASE_SPEED).withTimeout(0.3),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                        new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().SECOND_CARGO_RELEASE_SPEED)
                )
        );
    }
}
