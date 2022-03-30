package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveToCargo;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_ROTATE_VALUE = rootNamespace.addConstantDouble("seek rotate value", 0.5);
    private static final Supplier<Double> SEEK_HUB_TOLERANCE = rootNamespace.addConstantDouble("seek hub tolerance", 18);

    private static final double AIM_TO_HUB_MOVE_VALUE = 0.55;
    private static final Supplier<Double> AIM_TO_HUB_WAIT_TIME = rootNamespace.addConstantDouble("aim to hub wait time", 1.0);
    private static final PIDSettings aimToHubPIDSettings = Drivetrain.getInstance().getCameraPIDSettings();

    public static final double SEEK_CARGO_TOLERANCE = 90;

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
        aimToHubPIDSettings.setWaitTime(AIM_TO_HUB_WAIT_TIME);
    }

    /**
     * @return a command that turns the robot until the angle between the limelight and target is small enough
     */
    private Command seekHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE, () ->
                -SEEK_HUB_TOLERANCE.get() <= limelight.getHorizontalOffsetFromTargetInDegrees() &&
                        limelight.getHorizontalOffsetFromTargetInDegrees() <= SEEK_HUB_TOLERANCE.get());
    }

    private DriveArcadeWithPID aimToHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcadeWithPID(drivetrain, limelight::getHorizontalOffsetFromTargetInPixels, () -> 0.0, () -> AIM_TO_HUB_MOVE_VALUE,
                drivetrain.getCameraPIDSettings());
    }

    /**
     * @return a command that turns the robot until the limelight sees the hub
     */
    private DriveArcade seekCargo() {
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE, () -> (hasCargoTarget() &&
                Math.abs(MoveToCargo.getCargoX() - MoveToCargo.SETPOINT) <= SEEK_CARGO_TOLERANCE));
    }

    /**
     * @return whether the image processing camera has a target
     */
    private boolean hasCargoTarget() {
        try {
            RootNamespace imageProcess = new RootNamespace("Image Processing");
            Namespace contourInfo = imageProcess.addChild("contour 0");
            return contourInfo.getBoolean("isUpdated");
        } catch (Exception e) {
            return false;
        }
    }
}
