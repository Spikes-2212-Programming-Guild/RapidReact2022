package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePlacer;


public class IntakePlacerDefaultCommand extends CommandBase {

    private final IntakePlacer intakePlacer;

    public IntakePlacerDefaultCommand() {
        intakePlacer = IntakePlacer.getInstance();
        addRequirements(intakePlacer);
    }

    @Override
    public void execute() {
        if (intakePlacer.getShouldBeUp() && intakePlacer.isDown()) {
            new MoveGenericSubsystem(intakePlacer, IntakePlacer.MAX_SPEED).schedule();
        } else {
            if (intakePlacer.getShouldBeUp()) {
                intakePlacer.move(IntakePlacer.IDLE_SPEED);
            } else {
                intakePlacer.move(0);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        intakePlacer.stop();
    }
}
