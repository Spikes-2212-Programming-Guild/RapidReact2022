package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePlacer;

/**
 * Opens the intake placer's latch so the intake placer can move down.
 */
public class MoveIntakePlacerDown extends CommandBase {

    private final IntakePlacer intakePlacer;
    private final boolean ignoreLimit;

    /**
     * @param ignoreLimit whether the command should end once the latch has been opened, or wait until the
     *                    lower limit is hit
     */
    public MoveIntakePlacerDown(boolean ignoreLimit) {
        this.intakePlacer = IntakePlacer.getInstance();
        this.ignoreLimit = ignoreLimit;
        addRequirements(intakePlacer);
    }

    @Override
    public void initialize() {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get());
    }

    @Override
    public boolean isFinished() {
        return ignoreLimit || intakePlacer.isDown();
    }

    @Override
    public void end(boolean interrupted) {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());
    }
}
