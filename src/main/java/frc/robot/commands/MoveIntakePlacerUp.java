package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.IntakePlacer;

/**
 * Moves the IntakePlacer up until {@value TIMEOUT} seconds have passed since the upper limit was hit.
 */
public class MoveIntakePlacerUp extends MoveGenericSubsystem {

    private static final double TIMEOUT = 0.3;
    private double meetLimitTime;

    public MoveIntakePlacerUp() {
        super(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED);
    }

    @Override
    public void initialize() {
        IntakePlacer.getInstance().setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());
        meetLimitTime = 0;
    }

    @Override
    public boolean isFinished() {
        if (IntakePlacer.getInstance().isUp() && meetLimitTime == 0)
            meetLimitTime = Timer.getFPGATimestamp();
        return meetLimitTime != 0 && Timer.getFPGATimestamp() >= meetLimitTime + TIMEOUT;
    }
}
