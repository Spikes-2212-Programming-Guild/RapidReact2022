package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePlacer;

/**
 * Opens the intake placer's latch so the intake placer can move down, by rotating a servo that is
 * connected to the latch.
 */
public class MoveIntakePlacerDown extends CommandBase {

    private final IntakePlacer intakePlacer;
    private final boolean ignoreLimit;
    private double startTime;
    private final double TIMEOUT = 0.6;

    /**
     * @param ignoreLimit Represents whether the command should end once the latch has been opened, or wait until the
     *                    lower limit is hit.
     */
    public MoveIntakePlacerDown(boolean ignoreLimit) {
        this.intakePlacer = IntakePlacer.getInstance();
        this.ignoreLimit = ignoreLimit;
        addRequirements(intakePlacer);
    }

    @Override
    public void initialize() {
        intakePlacer.move(0.3);
        startTime = Timer.getFPGATimestamp();
        intakePlacer.setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get());
    }

    @Override
    public void execute() {
        if (Timer.getFPGATimestamp() > startTime + TIMEOUT)
//            intakePlacer.move(IntakePlacer.ACTIVE_DROP_SPEED);
            intakePlacer.stop();
    }

    @Override
    public boolean isFinished() {
        if (!ignoreLimit)
            return intakePlacer.isDown();
        else
            return true;
    }

    @Override
    public void end(boolean interrupted) {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());
        intakePlacer.stop();
    }
}
