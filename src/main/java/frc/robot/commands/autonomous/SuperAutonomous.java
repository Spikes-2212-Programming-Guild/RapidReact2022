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
    private static final Supplier<Double> AIM_TO_HUB_MOVE_VALUE = rootNamespace.addConstantDouble("aim to hub move value", 0.5);

    private static final Namespace aimToHubPIDNamespace = rootNamespace.addChild("aim to hub PID");
    private static final Supplier<Double> AIM_TO_HUB_KP = rootNamespace.addConstantDouble("kP", 0.0013);
    private static final Supplier<Double> AIM_TO_HUB_KI = rootNamespace.addConstantDouble("kI", 0.0);
    private static final Supplier<Double> AIM_TO_HUB_KD = rootNamespace.addConstantDouble("kD", 0.0);
    private static final Supplier<Double> AIM_TO_HUB_TOLERANCE = rootNamespace.addConstantDouble("tolerance", 45);
    private static final Supplier<Double> AIM_TO_HUB_WAIT_TIME = rootNamespace.addConstantDouble("wait time", 2);
    private static final PIDSettings aimToHubPIDSettings = new PIDSettings(AIM_TO_HUB_KP, AIM_TO_HUB_KI, AIM_TO_HUB_KD,
            AIM_TO_HUB_TOLERANCE, AIM_TO_HUB_WAIT_TIME);

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

    /**
     * @return a command that turns the robot until the angle between the limelight and target is small enough
     */
    private Command seekHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE).withInterrupt(() ->
                -SEEK_HUB_TOLERANCE.get() <= limelight.getHorizontalOffsetFromTargetInDegrees() &&
                        limelight.getHorizontalOffsetFromTargetInDegrees() <= SEEK_HUB_TOLERANCE.get());
    }

    private DriveArcadeWithPID aimToHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcadeWithPID(drivetrain, limelight::getHorizontalOffsetFromTargetInPixels, () -> 0.0, AIM_TO_HUB_MOVE_VALUE,
                aimToHubPIDSettings);
    }

    /**
     * @return a command that turns the robot until it
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
