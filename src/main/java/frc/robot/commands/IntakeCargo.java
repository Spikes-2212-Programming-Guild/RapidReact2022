package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;

public class IntakeCargo extends SequentialCommandGroup {

    public static final double INTAKE_PLACER_DOWN_TIMEOUT = 4;

    private boolean hasCargo;

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        Transfer transfer = Transfer.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        addCommands(
                new MoveIntakePlacerDown().withTimeout(INTAKE_PLACER_DOWN_TIMEOUT),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new MoveGenericSubsystem(intakeRoller, IntakeRoller.MIN_SPEED) {
                                    @Override
                                    public boolean isFinished() {
                                        return intakeToTransfer.getLimit();
                                    }
                                },
                                new MoveGenericSubsystem(transfer, transfer.MOVE_SPEED) {
                                    @Override
                                    public boolean isFinished() {
                                        return transfer.getEntranceSensor();
                                    }
                                }
                        ),
                        new MoveGenericSubsystem(intakeToTransfer, IntakeToTransfer.SPEED) {
                            @Override
                            public boolean isFinished() {
                                return (!hasCargo && transfer.getEntranceSensor()) || (hasCargo && intakeToTransfer.getLimit());
                            }
                        }
                )
        );
    }

    @Override
    public void initialize() {
        super.initialize();
        hasCargo = Transfer.getInstance().getEntranceSensor();
    }
}
