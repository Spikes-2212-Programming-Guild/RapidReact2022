package frc.robot.commands.climbing;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ClimberPlacer;

public class SetPlacersIdleMode extends InstantCommand {

    public SetPlacersIdleMode(IdleMode idleMode) {
        ClimberPlacer.getLeftInstance().setIdleMode(idleMode);
        ClimberPlacer.getRightInstance().setIdleMode(idleMode);
    }
}
