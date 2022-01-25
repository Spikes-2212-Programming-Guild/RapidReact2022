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

    private static final double SPEED = 0.7;
    private Drivetrain drivetrain;

    /**
     * The alliance's tarmac as it is seen by the left color sensor.
     */
    private final Color allianceLeftLine;

    /**
     * The alliance's tarmac as it is seen by the right color sensor.
     */
    private final Color allianceRightLine;

    //@todo calibrate final values to each of the below

    /**
     * The blue tarmac line as it is seen by the left color sensor.
     */
    private Color leftBlueLine;

    /**
     * The blue tarmac line as it is seen by the right color sensor.
     */
    private Color rightBlueLine;

    /**
     * The red tarmac line as it is seen by the left color sensor.
     */
    private Color leftRedLine;

    /**
     * The red tarmac line as it is seen by the right color sensor.
     */
    private Color rightRedLine;

    /**
     * The carpet color as it is seen by the left color sensor.
     */
    private Color leftCarpetColor;

    /**
     * The carpet color as it is seen by the right color sensor.
     */
    private Color rightCarpetColor;

    private ColorMatch leftColorMatch;
    private ColorMatch rightColorMatch;

    /**
     * Please provide {@code Color.kRed} if the robot is in the red alliance,
     * or {@code Color.kBlue} if the robot is in the blue alliance.
     *
     * @param color the alliance's color
     */
    public SettleOnLine(Color color) {
        this.drivetrain = Drivetrain.getInstance();
        this.leftColorMatch = new ColorMatch();
        this.rightColorMatch = new ColorMatch();
        if (color.equals(Color.kRed)) {
            allianceLeftLine = leftRedLine;
            allianceRightLine = rightRedLine;
        } else {
            allianceLeftLine = leftBlueLine;
            allianceRightLine = rightBlueLine;
        }
        leftColorMatch.addColorMatch(allianceLeftLine);
        leftColorMatch.addColorMatch(leftCarpetColor);
        rightColorMatch.addColorMatch(allianceRightLine);
        rightColorMatch.addColorMatch(rightCarpetColor);
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
        return rightColorMatch.matchClosestColor(drivetrain.getRightColor()).color.equals(allianceRightLine);
    }

    private boolean isLeftOnColor() {
        return leftColorMatch.matchClosestColor(drivetrain.getLeftColor()).color.equals(allianceLeftLine);
    }
}
