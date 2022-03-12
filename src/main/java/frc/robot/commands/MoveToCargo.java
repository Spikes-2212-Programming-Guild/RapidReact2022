package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class MoveToCargo extends DriveArcadeWithPID {

    public static final Supplier<Double> CARGO_MOVE_VALUE = () -> 0.53;
    public static final double SETPOINT = 10.0;

    public MoveToCargo(Drivetrain drivetrain, Supplier<Double> speed) {
        super(drivetrain, () -> -MoveToCargo.getCargoX(), () -> SETPOINT, speed, drivetrain.getCameraPIDSettings(),
                drivetrain.getFFSettings());
    }

    public static double getCargoX() {
        try {
            RootNamespace imageProcess = new RootNamespace("Image Processing");
            Namespace contourInfo = imageProcess.addChild("contour 0");
            return contourInfo.getNumber("x");
        }
        catch (Exception e) {
            return SETPOINT;
        }
    }
}
