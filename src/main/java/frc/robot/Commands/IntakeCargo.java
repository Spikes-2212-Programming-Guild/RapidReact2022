package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Transfer transfer = Transfer.getInstance();
        addRequirements(intakeRoller, intakePlacer, transfer);
        if (transfer.isTopPressed()) {
            addCommands(new MoveGenericSubsystem(transfer, -Transfer.SPEED).withTimeout(Transfer.CARGO_RETURN_TIME));
        }
        addCommands(
                new MoveGenericSubsystemWithPID(intakePlacer,
                        () -> IntakePlacer.POTENTIOMETER_DOWN_SETPOINT,
                        intakePlacer::getPotentiometerAngle, intakePlacer.pidSettings, intakePlacer.feedForwardSettings),
                new MoveGenericSubsystem(intakeRoller, IntakeRoller.SPEED),
                new MoveGenericSubsystem(transfer, Transfer.SPEED).withTimeout(Transfer.TRANSFER_TIME),
                new MoveGenericSubsystemWithPID(intakePlacer, () -> IntakePlacer.POTENTIOMETER_UP_SETPOINT,
                        intakePlacer::getPotentiometerAngle, intakePlacer.pidSettings, intakePlacer.feedForwardSettings)
        );
    }
}
