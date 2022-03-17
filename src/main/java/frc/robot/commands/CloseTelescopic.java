package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

public class CloseTelescopic extends SequentialCommandGroup {

    public CloseTelescopic() {
        ClimberWinch winch = ClimberWinch.getInstance();
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addRequirements(winch, leftPlacer, rightPlacer);
        addCommands(
                new SetClimberPlacerIdleMode(IdleMode.kBrake),
                new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED).withInterrupt(winch::staticMeetBar),
                new SetClimberPlacerIdleMode(IdleMode.kCoast),
                new InstantCommand(() -> winch.setIdleMode(IdleMode.kCoast)),
                new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED),
                new SetClimberPlacerIdleMode(IdleMode.kBrake)
        );
    }
}