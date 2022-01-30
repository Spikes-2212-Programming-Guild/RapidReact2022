package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.ClimberPlacer;
import frc.robot.Subsystems.ClimberWinch;

public class Climb extends SequentialCommandGroup {

    public Climb() {
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        ClimberPlacer climberPlacer = ClimberPlacer.getInstance();
        addRequirements(climberWinch, climberPlacer);
        addCommands(
                new MoveGenericSubsystem(climberWinch, climberWinch.getUpSpeed()),
                new MoveGenericSubsystem(climberWinch, climberWinch.getDownSpeed()) {
                    @Override
                    public boolean isFinished() {
                        return climberWinch.isHooked();
                    }
                },
                new MoveGenericSubsystem(climberWinch, climberWinch.getHookedDownSpeed())
        );
    }
}
