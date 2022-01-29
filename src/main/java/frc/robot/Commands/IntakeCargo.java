package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.Transfer;

public class IntakeCargo extends SequentialCommandGroup {

    public IntakeCargo() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Transfer transfer = Transfer.getInstance();
        PIDSettings pidSettings = intakePlacer.getPIDSettings();
        FeedForwardSettings feedForwardSettings = intakePlacer.getFeedForwardSettings();
        addRequirements(intakeRoller, intakePlacer, transfer);
        addCommands(
                new MoveGenericSubsystemWithPID(intakePlacer,
                        () -> IntakePlacer.POTENTIOMETER_RANGE_VALUE,
                        intakePlacer::getPotentiometerAngle, pidSettings, feedForwardSettings),
                new MoveGenericSubsystem
                        (intakeRoller, IntakeRoller.SPEED) {
                    @Override
                    public boolean isFinished() {
                        return !transfer.isStartPressed();
                    }
                },
                new MoveGenericSubsystem(transfer, 0).withTimeout(Transfer.TRANSFER_TIME),
                new MoveGenericSubsystemWithPID(intakePlacer, () -> IntakePlacer.POTENTIOMETER_STARTING_POINT,
                        intakePlacer::getPotentiometerAngle, pidSettings, feedForwardSettings)
        );
    }
}
