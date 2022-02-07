package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems.ClimberPlacer;

public class DropPlacer extends MoveGenericSubsystem {

    private final ClimberPlacer placer;

    private final double startTime;

    public DropPlacer(ClimberPlacer placer) {
        super(placer, placer.getDropSpeed());
        this.placer = placer;
        this.startTime = Timer.getFPGATimestamp();
    }

    private boolean oneSecondPassed() {
        return Timer.getFPGATimestamp() - startTime > 1;
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() && placer.isStalling() && oneSecondPassed();
    }
}
