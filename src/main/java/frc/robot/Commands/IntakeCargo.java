package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    public static final double TRANSFER_MOVE_TIMEOUT = 0.5;

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Transfer transfer = Transfer.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        addRequirements(intakeRoller, intakePlacer, transfer, intakeToTransfer);
        addCommands(
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.MIN_SPEED),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(intakeRoller, IntakeRoller.SPEED) {
                            @Override
                            public boolean isFinished() {
                                return intakeToTransfer.getLimit();
                            }
                        },
                        new MoveGenericSubsystem(intakeToTransfer, IntakeToTransfer.SPEED) {
                            @Override
                            public boolean isFinished() {
                                return transfer.isStartPressed();
                            }
                        }
                ),
                new MoveGenericSubsystem(transfer, Transfer.SPEED) {
                    @Override
                    public boolean isFinished() {
                        return intakeToTransfer.getLimit();
                    }
                }.withTimeout(TRANSFER_MOVE_TIMEOUT));
    }
}