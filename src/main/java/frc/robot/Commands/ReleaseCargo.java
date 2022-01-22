package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
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
                new MoveGenericSubsystem(dispenser, Dispenser.SPEED).withTimeout(Dispenser.DISPENSER_WARM_UP_TIME),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(dispenser, Dispenser.SPEED),
                        new MoveGenericSubsystem(transfer, Transfer.SPEED)
                ).withTimeout(Dispenser.TRANSFER_PLUS_DISPENSE_TIME)
        );
        addRequirements(dispenser, transfer);
    }
}

