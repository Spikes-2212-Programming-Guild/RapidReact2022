package frc.robot.commands.climbing;

import com.revrobotics.CANSparkMax.IdleMode;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

public class CloseTelescopic extends SequentialCommandGroup {

    /**
     * steps:
     * 1. sets the placers to brake<br>
     * 2. moves the robot up until the static meets the bar on the upper part<br>
     * 3. sets the placers to coast so the bar will slide on the static<br>
     * 4. moves the robot up<br>
     * 5. sets the placers and the winch to brake so the robot won't fall<br>
     */
    public CloseTelescopic() {
        ClimberWinch winch = ClimberWinch.getInstance();
        addRequirements(winch, ClimberPlacer.getLeftInstance(), ClimberPlacer.getLeftInstance());
        addCommands(
                new SetPlacersIdleMode(IdleMode.kBrake),
                new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED).withInterrupt(() -> winch.getEncoderPosition()
                        <= winch.ENCODER_STATIC_MEET_BAR_POSITION.get()),
                new SetPlacersIdleMode(IdleMode.kCoast),
                new InstantCommand(() -> winch.setIdleMode(IdleMode.kCoast)),
                new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED),
                new SetPlacersIdleMode(IdleMode.kBrake),
                new InstantCommand(() -> winch.setIdleMode(IdleMode.kBrake))
        );
    }
}
