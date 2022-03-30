package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeToTransfer;

/**
 * Moves to a cargo, while simultaneously aiming the robot to face the cargo and then intake the cargo.
 */
public class MoveToCargoWithIntake extends ParallelCommandGroup {

    public static final double CARGO_MOVE_VALUE = 0.7;
    public static final double SETPOINT = 10.0;
    public static final double MOVE_TO_CARGO_TIMEOUT = 4.2212;

    public MoveToCargoWithIntake(Drivetrain drivetrain, double speed) {
        super(new IntakeCargo(false),
                new DriveArcadeWithPID(drivetrain, MoveToCargoWithIntake::getCargoX, SETPOINT, speed,
                        drivetrain.getCameraPIDSettings()));
    }

    /**
     * @return the x coordinate of the center of the cargo contour, as received by the image processing.
     */
    public static double getCargoX() {
        try {
            return -getImageProcessingNamespace().getNumber("x");
        } catch (Exception e) {
            return SETPOINT;
        }
    }

    /**
     * @return whether the image processing camera has a target
     */
    public static boolean hasCargoTarget() {
        try {
            return getImageProcessingNamespace().getBoolean("isUpdated");
        } catch (Exception e) {
            return false;
        }
    }

    private static Namespace getImageProcessingNamespace() {
        RootNamespace imageProcess = new RootNamespace("Image Processing");
        return imageProcess.addChild("contour 0");
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || IntakeToTransfer.getInstance().getLimit();
    }
}
