package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

public class MoveToCargo extends DriveArcadeWithPID {

    public static final double CARGO_MOVE_VALUE = 0.4;

    public MoveToCargo(Drivetrain drivetrain) {
        super(drivetrain, () -> -MoveToCargo.getCargoX(), 10, CARGO_MOVE_VALUE, drivetrain.getCameraPIDSettings(),
                drivetrain.getFFSettings());
    }

    public static double getCargoX() {
        RootNamespace imageProcess = new RootNamespace("Image Processing");
        Namespace contourInfo = imageProcess.addChild("contour 0");
        return contourInfo.getNumber("x");
    }
}
