package frc.robot.commands.climbing;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import frc.robot.subsystems.ClimberPlacer;

public class PlacerToBarPID extends MoveGenericSubsystemWithPID {

    public PlacerToBarPID(ClimberPlacer climberPlacer) {
        super(climberPlacer, climberPlacer.ENCODER_TO_NEXT_BAR_POSITION, climberPlacer::getEncoderPosition,
                ClimberPlacer.pidSettings);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
