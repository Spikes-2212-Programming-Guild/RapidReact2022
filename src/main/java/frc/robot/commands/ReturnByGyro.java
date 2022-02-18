package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import frc.robot.subsystems.Drivetrain;
import com.spikes2212.util.Limelight;

/**
 * This command moves the {@link Drivetrain} till either its gyro angle is zero or the
 * {@link Limelight} has a valid target.
 */
public class ReturnByGyro extends DriveArcadeWithPID {

    private static final double SPEED = -0.5;
    private static final int LIMELIGHT_PIPELINE = 1;

    private int previousPipeline;

//    private Limelight limelight;

    public ReturnByGyro(Drivetrain drivetrain, double setpoint) {
        super(drivetrain, () -> -drivetrain.getYaw(), setpoint, SPEED, drivetrain.getGyroPIDSettings(),
                drivetrain.getFFSettings());
//        this.limelight = Transfer.getInstance().getLimelight();
    }

//    @Override
//    public void initialize() {
//        previousPipeline = limelight.getPipeline();
//        limelight.setPipeline(LIMELIGHT_PIPELINE);
//    }

//    @Override
//    public boolean isFinished() {
//        return limelight.isOnTarget() || super.isFinished();
//    }

//    @Override
//    public void end(boolean interrupted) {
//        super.end(interrupted);
//        limelight.setPipeline(previousPipeline);
//    }
}
