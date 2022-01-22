package frc.robot.Commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Drivetrain;

/**
 * This command moves the {@code Drivetrain} till both its {@code ColorSensorV3} are on the tarmac's line correct color.
 */
public class SettleOnLine extends CommandBase {

    public static final double SPEED = 0.7;
    private Drivetrain drivetrain = Drivetrain.getInstance();

    /**
     * The alliance's color.
     */
    private Color color;

    public SettleOnLine(Color color) {
        this.color = color;
    }

    @Override
    public void execute() {
        double leftSpeed = 0;
        double rightSpeed = 0;
        if (!isLeftOnColor()) leftSpeed = SPEED;
        if (!isRightOnColor()) rightSpeed = SPEED;
        drivetrain.tankDrive(leftSpeed, rightSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return isLeftOnColor() && isRightOnColor();
    }

    private boolean isRightOnColor() {
        return drivetrain.getRightColor().equals(color);
    }

    private boolean isLeftOnColor() {
        return drivetrain.getLeftColor().equals(color);
    }
}
