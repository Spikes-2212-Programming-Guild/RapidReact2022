package frc.robot.commands.climbing;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ClimberPlacer;

public class SetClimberPlacerIdleMode extends InstantCommand {

    public SetClimberPlacerIdleMode(IdleMode idleMode) {
        ClimberPlacer leftClimberPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightClimberPlacer = ClimberPlacer.getRightInstance();
        leftClimberPlacer.setIdleMode(idleMode);
        rightClimberPlacer.setIdleMode(idleMode);
    }
}
