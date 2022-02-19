package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    private boolean hasCargo;

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
                                new MoveGenericSubsystem(intakeRoller, IntakeRoller.MAX_SPEED) {
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
                                return (transfer.getEntranceSensor() && !hasCargo) || (intakeToTransfer.getLimit() && hasCargo);
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

    @Override
    public void initialize() {
        hasCargo = Transfer.getInstance().getEntranceSensor();
    }
}
