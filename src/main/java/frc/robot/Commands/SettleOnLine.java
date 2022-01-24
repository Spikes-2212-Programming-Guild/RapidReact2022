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

    private final Color allianceLeftLine;
    private final Color allianceRightLine;

    //@todo calibrate final values to each of the below
    private Color leftBlueLine;
    private Color rightBlueLine;
    private Color leftRedLine;
    private Color rightRedLine;
    private Color leftCarpetColor;
    private Color rightCarpetColor;
    private ColorMatch leftColorMatch;
    private ColorMatch rightColorMatch;

    /**
     * provide {@code Color.kRed} if the robot is in the red alliance,
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
