package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.subsystems.IntakePlacer;


public class IntakePlacerDefaultCommand extends MoveGenericSubsystem {

    public IntakePlacerDefaultCommand() {
        super(IntakePlacer.getInstance(), IntakePlacer.IDLE_SPEED);
    }

    @Override
    public void execute() {
        IntakePlacer intakePlacer = (IntakePlacer) subsystem;
        if (intakePlacer.getShouldBeUp() && intakePlacer.isDown()) {
            new MoveGenericSubsystem(intakePlacer, IntakePlacer.MAX_SPEED).schedule();
        } else {
            if (intakePlacer.getShouldBeUp()) {
                subsystem.move(speedSupplier.get());
            } else {
                subsystem.move(0);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
