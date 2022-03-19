package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.subsystems.IntakePlacer;

public class MoveIntakePlacerDown extends MoveGenericSubsystem {

    public MoveIntakePlacerDown() {
        super(IntakePlacer.getInstance(), IntakePlacer.MIN_SPEED);
    }

    @Override
    public void initialize() {
        IntakePlacer intakePlacer = (IntakePlacer) subsystem;
        intakePlacer.setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE);
    }

    @Override
    public void end(boolean interrupted) {
        IntakePlacer intakePlacer = (IntakePlacer) subsystem;
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE);
        super.end(interrupted);
    }
}
