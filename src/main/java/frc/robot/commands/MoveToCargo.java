package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

/**
 * Moves to a cargo, while simultaneously aiming the robot to face the cargo.
 */
public class MoveToCargo extends DriveArcadeWithPID {

    public static final Supplier<Double> CARGO_MOVE_VALUE = () -> 0.55;
    public static final double SETPOINT = 10.0;
    public static final double MOVE_TO_CARGO_TIMEOUT = 7;

    public MoveToCargo(Drivetrain drivetrain, Supplier<Double> speed) {
        super(drivetrain, () -> -MoveToCargo.getCargoX(), () -> SETPOINT, speed, drivetrain.getCameraPIDSettings(),
                drivetrain.getFFSettings());
    }

    /**
     * @return the x coordinate of the center of the cargo contour, as received by the image processing.
     */
    public static double getCargoX() {
        try {
            RootNamespace imageProcess = new RootNamespace("Image Processing");
            Namespace contourInfo = imageProcess.addChild("contour 0");
            return contourInfo.getNumber("x");
        } catch (Exception e) {
            return SETPOINT;
        }
    }
}
