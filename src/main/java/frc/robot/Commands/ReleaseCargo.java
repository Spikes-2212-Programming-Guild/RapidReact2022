package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Transfer;


/**
 * A command which releases the cargo into the lower hub using the {@link Transfer} and {@link  Dispenser} subsystems.
 * @author Yoel Perman Brilliant
 */
public class ReleaseCargo extends SequentialCommandGroup {

    public ReleaseCargo() {
        Dispenser dispenser = Dispenser.getInstance();
        Transfer transfer = Transfer.getInstance();
        addCommands(
                new MoveGenericSubsystemWithPID(dispenser, ()->Dispenser.TARGET_SPEED, dispenser::getSpeed,
                        dispenser.getPIDSettings(), dispenser.getFeedForwardSettings()) {
                    @Override
                    public void end(boolean interrupted) {}
                },
                new ParallelCommandGroup(
                        new MoveGenericSubsystemWithPID(dispenser, ()->Dispenser.TARGET_SPEED, dispenser::getSpeed,
                                dispenser.getPIDSettings(), dispenser.getFeedForwardSettings()),
                        new MoveGenericSubsystemWithPID(transfer, () -> Transfer.TARGET_SPEED, transfer::getSpeed,
                                transfer.getReleaseCargoPID(), transfer.getReleaseCargoFeedForward())
                ).withTimeout(Dispenser.TRANSFER_PLUS_DISPENSE_TIME)
        );
        addRequirements(dispenser, transfer);
    }
}

