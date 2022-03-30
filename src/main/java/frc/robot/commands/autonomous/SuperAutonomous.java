package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import java.util.function.Supplier;

public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_ROTATE_VALUE = rootNamespace.addConstantDouble("seek rotate value", 0.5);
    private static final Supplier<Double> SEEK_HUB_TOLERANCE = rootNamespace.addConstantDouble("seek hub tolerance", 18);

    public static final double FIRST_RELEASE_CARGO_TIMEOUT = 1;

    public static final double INTAKE_FIRST_CARGO_TIMEOUT = 2.5;
    public static final double INTAKE_SECOND_CARGO_TIMEOUT = 2;

    public static final double SEEK_CARGO_TOLERANCE = 90;
    public static final double SEEK_CARGO_TIMEOUT = 1;

    public static final double RETURN_HALFWAY_TIMEOUT = 1;

    public static final double SEEK_HUB_TIMEOUT = 1;

    public static final double DRIVE_UNTIL_HIT_HUB_TIMEOUT = 2;

    public static final double SECOND_RELEASE_CARGO_TIMEOUT = 2;

    private final Drivetrain drivetrain;

    /**
     * The time in which the robot has started to move towards the second cargo it intakes.
     */
    private double secondCargoStartTime;

    /**
     * The time in which the robot finished intaking the second cargo.
     */
    private double secondCargoFinishTime;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
        addCommands(
                releaseCargoAndLatch(),
                moveToCargoWithIntake().withTimeout(INTAKE_FIRST_CARGO_TIMEOUT),
                seekCargo().withTimeout(SEEK_CARGO_TIMEOUT),
                new InstantCommand(() -> secondCargoStartTime = Timer.getFPGATimestamp()),
                moveToCargoWithIntake().withTimeout(INTAKE_SECOND_CARGO_TIMEOUT),
                new InstantCommand(() -> secondCargoFinishTime = Timer.getFPGATimestamp()),
                returnByHalf(),
                seekHub().withTimeout(SEEK_HUB_TIMEOUT),
                new DriveUntilHitHub(drivetrain).withTimeout(DRIVE_UNTIL_HIT_HUB_TIMEOUT),
                new ReleaseCargo().withTimeout(SECOND_RELEASE_CARGO_TIMEOUT)
        );
    }

    private ParallelCommandGroup releaseCargoAndLatch() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> IntakePlacer.getInstance().setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get())),
                new ReleaseCargo().withTimeout(FIRST_RELEASE_CARGO_TIMEOUT)
        );
    }

    private ParallelCommandGroup moveToCargoWithIntake() {
        return new ParallelCommandGroup(
                new IntakeCargo(false),
                new MoveToCargo(drivetrain, MoveToCargo.CARGO_MOVE_VALUE));
    }

    private ParallelRaceGroup returnByHalf() {
        return new DriveArcade(drivetrain, () -> -MoveToCargo.CARGO_MOVE_VALUE.get(), () -> 0.0).withTimeout(
                0.5 * (secondCargoFinishTime - secondCargoStartTime));
    }

    private Command seekHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE).withInterrupt(() ->
                -SEEK_HUB_TOLERANCE.get() <= limelight.getHorizontalOffsetFromTarget() &&
                        limelight.getHorizontalOffsetFromTarget() <= SEEK_HUB_TOLERANCE.get());
    }

    private DriveArcade seekCargo() {
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE, () -> (hasCargoTarget() &&
                Math.abs(MoveToCargo.getCargoX() - MoveToCargo.SETPOINT) <= SEEK_CARGO_TOLERANCE));
    }

    /**
     * @return whether the image processing camera has a target
     */
    private boolean hasCargoTarget() {
        try {
            RootNamespace imageProcess = new RootNamespace("Image Processing");
            Namespace contourInfo = imageProcess.addChild("contour 0");
            return contourInfo.getBoolean("isUpdated");
        } catch (Exception e) {
            return false;
        }
    }
}
