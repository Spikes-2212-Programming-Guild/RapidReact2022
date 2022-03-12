package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

import java.util.function.Supplier;

public class ShootToHigherHub extends SequentialCommandGroup {

    private final RootNamespace rootNamespace = new RootNamespace("shoot to higher hub");

    public final Supplier<Double> ACCELERATION_TIMEOUT = rootNamespace.addConstantDouble("acceleration timeout", 3);
    public static final double SHOOTING_SPEED = 1;

    public ShootToHigherHub() {
        Transfer transfer = Transfer.getInstance();
        addCommands(
                new MoveGenericSubsystem(transfer, SHOOTING_SPEED) {
                    @Override
                    public void end(boolean interrupted) {}
                }.withTimeout(ACCELERATION_TIMEOUT.get()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED),
                        new MoveGenericSubsystem(transfer, SHOOTING_SPEED)
                )
        );
    }
}
