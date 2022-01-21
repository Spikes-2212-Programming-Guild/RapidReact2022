package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Transfer;


/**
 * @author Yoel Perman Brilliant
 */
public class ReleaseCargo extends SequentialCommandGroup {

    public static final double DISPENSER_WARM_UP_TIME = 3;
    public static final double TRANSFER_PLUS_DISPENSE_TIME = 5;


    public ReleaseCargo() {
        Dispenser dispenser = Dispenser.getInstance();
        Transfer transfer = Transfer.getInstance();
        addCommands(
                new MoveGenericSubsystem(dispenser, Dispenser.SPEED).withTimeout(DISPENSER_WARM_UP_TIME),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(dispenser, Dispenser.SPEED),
                        new MoveGenericSubsystem(transfer, Transfer.SPEED)
                ).withTimeout(TRANSFER_PLUS_DISPENSE_TIME)
        );
    }
}

