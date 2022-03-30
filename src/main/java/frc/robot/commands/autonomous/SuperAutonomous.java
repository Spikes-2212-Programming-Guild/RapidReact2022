package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveToCargo;
import frc.robot.subsystems.Drivetrain;

public class SuperAutonomous extends SequentialCommandGroup {

    public static final double SEEK_CARGO_TOLERANCE = 90;

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
    }

    private DriveTankWithPID returnByEncoders() {
        return new DriveTankWithPID(drivetrain, drivetrain.getEncodersPIDSettings(), drivetrain.getEncodersPIDSettings(),
                drivetrain.getLeftDistance() / 2, drivetrain.getRightDistance() / 2,
                drivetrain::getLeftDistance, drivetrain::getRightDistance);
    }

    private DriveArcade seekCargo() {
        return new DriveArcade(drivetrain, () -> 0.0, () -> 0.0, () -> (hasTarget() &&
                Math.abs(MoveToCargo.getCargoX() - MoveToCargo.SETPOINT) <= SEEK_CARGO_TOLERANCE));
    }

    /**
     * @return whether the image processing camera has a target
     */
    private boolean hasTarget() {
        try {
            RootNamespace imageProcess = new RootNamespace("Image Processing");
            Namespace contourInfo = imageProcess.addChild("contour 0");
            return contourInfo.getBoolean("isUpdated");
        } catch (Exception e) {
            return false;
        }
    }
}
