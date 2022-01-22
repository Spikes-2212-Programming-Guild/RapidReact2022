package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Dispenser;
import frc.robot.Subsystems.Drivetrain;

/**
 * This command moves the {@code Drivetrain} till either its {@code ADXRS450_Gyro} angle is zero or the
 * {@code Limelight} has a valid target.
 *
 * @author Ofri Rosenbaum
 */
public class ReturnByGyro extends CommandBase {

    public static final double SPEED = 0.7;

    /**
     * The speed difference between the two sides of the {@code Drivetrain}.
     */
    public static final double DIFFERENCE = 0.2;

    /**
     * The {@code ADXRS450_Gyro}'s tolerance.
     */
    public static final double TOLERANCE = 2;

    private Drivetrain drivetrain;
    private Dispenser dispenser;

    public ReturnByGyro() {
        this.drivetrain = Drivetrain.getInstance();
        this.dispenser = Dispenser.getInstance();
    }

    @Override
    public void initialize() {
        dispenser.setLimelightPipeline(1);
    }

    @Override
    public void execute() {
        if (drivetrain.getGyroAngle() > 0) {
            drivetrain.tankDrive(SPEED, SPEED - DIFFERENCE);
        } else {
            drivetrain.tankDrive(SPEED - DIFFERENCE, SPEED);
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return dispenser.isOnTarget() || Math.abs(drivetrain.getGyroAngle()) <= TOLERANCE;
    }
}
