package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.ClimberWinch;

public class CloseTelescopic extends SequentialCommandGroup {

    public CloseTelescopic() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        addRequirements(climberWinch);
        addCommands(
                new MoveGenericSubsystem(climberWinch, climberWinch.getDownSpeed()) {
                    @Override
                    public boolean isFinished() {
                        return (climberWinch.isLeftHooked() && climberWinch.isRightHooked()) || super.isFinished();
                    }
                },
                new MoveGenericSubsystem(climberWinch, climberWinch.getHookedDownSpeed())
        );
    }
}
