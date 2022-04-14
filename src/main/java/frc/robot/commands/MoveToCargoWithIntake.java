package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;

/**
 * Moves to a cargo, while simultaneously aiming the robot to face the cargo and then intake the cargo.
 */
public class MoveToCargoWithIntake extends ParallelCommandGroup {

    public static final double CARGO_MOVE_VALUE = 0.7;
    public static final double SETPOINT = 0;

    public MoveToCargoWithIntake(Drivetrain drivetrain, double speed) {
        super(/*new IntakeCargo(false),*/
                new DriveArcadeWithPID(drivetrain, MoveToCargoWithIntake::getCargoX, SETPOINT, speed,
                        drivetrain.cameraPIDSettings));
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || OI.buttonPressed()/*IntakeToTransfer.getInstance().getLimit()*/;
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
}
