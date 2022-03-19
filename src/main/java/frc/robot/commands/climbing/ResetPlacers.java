package frc.robot.commands.climbing;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberPlacer;

public class ResetPlacers extends CommandBase {

    private static final double SPEED = 0.2;

    private final ClimberPlacer left;
    private final ClimberPlacer right;

    public ResetPlacers() {
        this.left = ClimberPlacer.getLeftInstance();
        this.right = ClimberPlacer.getRightInstance();
    }

    @Override
    public void execute() {
        left.move(SPEED);
        right.move(SPEED);
    }

    @Override
    public boolean isFinished() {
        return left.getLimit() && right.getLimit();
    }

    @Override
    public void end(boolean interrupted) {
        left.resetEncoders();
        right.resetEncoders();
        left.stop();
        right.stop();
    }
}
