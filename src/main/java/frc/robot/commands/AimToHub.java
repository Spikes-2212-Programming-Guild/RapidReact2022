package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Transfer;

import java.util.function.Supplier;

/**
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 */
public class AimToHub extends DriveArcadeWithPID {

    private static final int LIMELIGHT_PIPELINE = 1;
    private static final RootNamespace aimToHubNS = new RootNamespace("aim to hub");
    private static final Supplier<Double> MOVE_VALUE = aimToHubNS.addConstantDouble("move value", 0);

    private final Transfer transfer;
    private int initialPipeline;
    private final Limelight limelight;

    public AimToHub(Drivetrain drivetrain, Transfer transfer) {
       super(drivetrain, transfer.getLimelight()::getHorizontalOffsetFromTarget, 0, MOVE_VALUE.get(),
               drivetrain.getPIDSettings(), drivetrain.getFFSettings());
       this.transfer = transfer;
       this.limelight = transfer.getLimelight();
    }

    @Override
    public void initialize() {
        initialPipeline = limelight.getPipeline();
        limelight.setPipeline(LIMELIGHT_PIPELINE);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        limelight.setPipeline(initialPipeline);
    }
}
