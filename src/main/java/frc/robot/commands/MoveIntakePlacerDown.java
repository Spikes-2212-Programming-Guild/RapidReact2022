package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakePlacer;

public class MoveIntakePlacerDown extends SequentialCommandGroup {

    public MoveIntakePlacerDown() {
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        addRequirements(intakePlacer);
        addCommands(
                new InstantCommand(() -> intakePlacer.setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE)),
                new MoveGenericSubsystem(intakePlacer, IntakePlacer.MIN_SPEED),
                new InstantCommand(() -> intakePlacer.setServoAngle(0))
        );
    }
}
