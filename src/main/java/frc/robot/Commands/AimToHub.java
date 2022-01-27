package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Transfer;

/**
 * <p>
 * A command which centers the robot on one of the reflective sprites that are on the hub.
 * </p>
 *
 * @author Yoel Perman Brilliant
 */
public class AimToHub extends DriveArcadeWithPID {
    private static final int LIMELIGHT_PIPELINE = 1;

    private final Transfer transfer;
    private int initialPipeline;


    public AimToHub(Drivetrain drivetrain, Transfer transfer) {
       super(drivetrain, transfer.getLimelight()::getHorizontalOffsetFromTarget, 0, 0,
               drivetrain.getPIDSettings(), drivetrain.getFFSettings());
       this.transfer = transfer;
    }


    @Override
    public void initialize() {
        initialPipeline = transfer.getLimelight().getPipeline();
        transfer.getLimelight().setPipeline(LIMELIGHT_PIPELINE);
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        transfer.getLimelight().setPipeline(initialPipeline);
    }
}
