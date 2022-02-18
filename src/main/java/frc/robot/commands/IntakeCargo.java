package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeRoller;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Transfer transfer = Transfer.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        addRequirements(intakeRoller, intakePlacer, transfer, intakeToTransfer);
        addCommands(
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.MIN_SPEED),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new MoveGenericSubsystem(intakeRoller, IntakeRoller.MIN_SPEED) {
                                    @Override
                                    public boolean isFinished() {
                                        return intakeToTransfer.getLimit();
                                    }
                                },
                                new MoveGenericSubsystem(transfer, transfer.getTransferSpeed()) {
                                    @Override
                                    public boolean isFinished() {
                                        return transfer.getEntranceSensor();
                                    }
                                }
                        ),
                        new MoveGenericSubsystem(intakeToTransfer, IntakeToTransfer.SPEED) {
                            @Override
                            public boolean isFinished() {
                                return transfer.getEntranceSensor();
                            }
                        }
                ),
                new MoveGenericSubsystem(transfer, transfer.getTransferSpeed()) {
                    @Override
                    public boolean isFinished() {
                        return intakeToTransfer.getLimit();
                    }
                }.withTimeout(transfer.getTransferMoveTimeout()));
    }
}
