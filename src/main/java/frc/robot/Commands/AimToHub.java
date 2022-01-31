package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.util.Limelight;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Transfer;

/**
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 */
public class AimToHub extends DriveArcadeWithPID {

    private static final int LIMELIGHT_PIPELINE = 1;

    private final Transfer transfer;
    private int initialPipeline;
    private final Limelight limelight;

    public AimToHub(Drivetrain drivetrain, Transfer transfer) {
       super(drivetrain, transfer.getLimelight()::getHorizontalOffsetFromTarget, 0, 0,
               drivetrain.getPIDSettings(), drivetrain.getFFSettings());
       this.transfer = transfer;
       this.limelight = transfer.limelight;
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
