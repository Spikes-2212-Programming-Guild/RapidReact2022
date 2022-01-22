package frc.robot.Commands;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Drivetrain;

/**
 * This command moves the {@link Drivetrain} till both its color sensors are on the tarmac's line correct color.
 *
 * @author Ofri Rosenbaum
 */
public class SettleOnLine extends CommandBase {

    public static final double SPEED = 0.7;
    private Drivetrain drivetrain;

    /**
     * The alliance's color.
     */
    private Color color;

    //@todo
    /**
     * The carpet's color.
     */
    private Color carpetColor;

    private ColorMatch colorMatch;

    public SettleOnLine(Color color) {
        this.color = color;
        this.drivetrain = Drivetrain.getInstance();
        this.colorMatch = new ColorMatch();
        colorMatch.addColorMatch(carpetColor);
        colorMatch.addColorMatch(color);
    }

    @Override
    public void execute() {
        double leftSpeed = !isLeftOnColor() ? SPEED : 0;
        double rightSpeed = !isRightOnColor() ? SPEED : 0;
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
        return colorMatch.matchClosestColor(drivetrain.getRightColor()).color.equals(color);
    }

    private boolean isLeftOnColor() {
        return colorMatch.matchClosestColor(drivetrain.getLeftColor()).color.equals(color);
    }
}
