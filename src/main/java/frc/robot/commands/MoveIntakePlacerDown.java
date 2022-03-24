package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePlacer;

/**
 * Opens the intake placer's latch so the intake placer can move down, by rotating a servo that is
 * connected to the latch.
 */
public class MoveIntakePlacerDown extends CommandBase {

    private final IntakePlacer intakePlacer;

    public MoveIntakePlacerDown() {
        this.intakePlacer = IntakePlacer.getInstance();
        addRequirements(intakePlacer);
    }

    @Override
    public void initialize() {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get());
    }

    @Override
    public boolean isFinished() {
        return intakePlacer.isDown();
    }

    @Override
    public void end(boolean interrupted) {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());
    }
}
