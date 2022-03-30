package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

/**
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 */
public class AimToHub extends DriveArcadeWithPID {

    public static final RootNamespace aimToHubNS = new RootNamespace("aim to hub");
    public static final Supplier<Double> MOVE_VALUE = aimToHubNS.addConstantDouble("move value", 0);

    public AimToHub(Drivetrain drivetrain) {
       super(drivetrain, drivetrain.getLimelight()::getHorizontalOffsetFromTarget, () -> 0.0, MOVE_VALUE,
               drivetrain.getLimelightPIDSettings(), drivetrain.getFFSettings());
    }
}
