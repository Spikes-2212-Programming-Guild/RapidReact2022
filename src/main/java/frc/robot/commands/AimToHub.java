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

    public static final int LIMELIGHT_PIPELINE = 1;
    public static final RootNamespace aimToHubNS = new RootNamespace("aim to hub");
    public static final Supplier<Double> MOVE_VALUE = aimToHubNS.addConstantDouble("move value", 0);

    private final Limelight limelight;

    public AimToHub(Drivetrain drivetrain) {
       super(drivetrain, drivetrain.getLimelight()::getHorizontalOffsetFromTarget, () -> 0.0, MOVE_VALUE,
               drivetrain.getLimelightPIDSettings(), drivetrain.getFFSettings());
       this.limelight = drivetrain.getLimelight();
    }

    @Override
    public void initialize() {
        limelight.setPipeline(LIMELIGHT_PIPELINE);
    }
}
