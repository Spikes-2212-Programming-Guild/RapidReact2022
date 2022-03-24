package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePlacer;

public class MoveIntakePlacerDown extends CommandBase {

    private final IntakePlacer intakePlacer;
    private final boolean ignoreLimit;

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
        if (!ignoreLimit)
            return intakePlacer.isDown();
        else
            return true;
    }

    @Override
    public void end(boolean interrupted) {
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());
    }
}
