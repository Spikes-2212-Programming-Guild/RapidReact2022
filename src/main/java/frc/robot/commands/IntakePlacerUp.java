package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.IntakePlacer;

public class IntakePlacerUp extends MoveGenericSubsystem {

    private static final double TIMEOUT = 0.3;
    private double meetLimitTime;

    public IntakePlacerUp() {
        super(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED);
    }

    @Override
    public void initialize() {
        meetLimitTime = 0;
    }

    @Override
    public boolean isFinished() {
        if (super.isFinished() && meetLimitTime == 0)
            meetLimitTime = Timer.getFPGATimestamp();
        return meetLimitTime != 0 && Timer.getFPGATimestamp() >= meetLimitTime + TIMEOUT;
    }
}
