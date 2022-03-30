package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.DriveUntilHitHub;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.MoveToCargo;
import frc.robot.commands.ReleaseCargo;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeToTransfer;

import java.util.function.Supplier;

public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_ROTATE_VALUE = rootNamespace.addConstantDouble("seek rotate value", 0.5);
    private static final Supplier<Double> SEEK_HUB_TOLERANCE = rootNamespace.addConstantDouble("seek hub tolerance", 18);

    public static final double FIRST_RELEASE_CARGO_TIMEOUT = 1;

    public static final Supplier<Double> MOVE_TO_CARGO_SPEED = () -> 0.7;

    public static final double INTAKE_FIRST_CARGO_TIMEOUT = 2.5;
    public static final double INTAKE_SECOND_CARGO_TIMEOUT = 2;

    public static final double SEEK_CARGO_TOLERANCE = 90;
    public static final double SEEK_CARGO_TIMEOUT = 1;

    public static final double RETURN_BY_ENCODERS_TIMEOUT = 1;

    public static final double SEEK_HUB_TIMEOUT = 1;

    public static final double DRIVE_UNTIL_HIT_HUB_TIMEOUT = 2;

    public static final double SECOND_RELEASE_CARGO_TIMEOUT = 2;

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
        addCommands(
                new ParallelCommandGroup(
                        new InstantCommand(() -> IntakePlacer.getInstance().setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get())),
                        new ReleaseCargo().withTimeout(FIRST_RELEASE_CARGO_TIMEOUT)
                ),
                moveToCargoWithIntake().withTimeout(INTAKE_FIRST_CARGO_TIMEOUT),
                seekCargo().withTimeout(SEEK_CARGO_TIMEOUT),
                moveToCargoWithIntake().withTimeout(INTAKE_SECOND_CARGO_TIMEOUT),
                returnByEncoders().withTimeout(RETURN_BY_ENCODERS_TIMEOUT),
                seekHub().withTimeout(SEEK_HUB_TIMEOUT),
                new DriveUntilHitHub(drivetrain).withTimeout(DRIVE_UNTIL_HIT_HUB_TIMEOUT),
                new ReleaseCargo().withTimeout(SECOND_RELEASE_CARGO_TIMEOUT)
        );
    }

    private ParallelCommandGroup moveToCargoWithIntake() {
        return new ParallelCommandGroup(
                new IntakeCargo(false),
                new SequentialCommandGroup(
                        new MoveToCargo(drivetrain, MOVE_TO_CARGO_SPEED),
                        new DriveArcade(drivetrain, MOVE_TO_CARGO_SPEED, () -> 0.0)
                ).withInterrupt(IntakeToTransfer.getInstance()::getLimit));
    }

    private DriveTankWithPID returnByEncoders() {
        return new DriveTankWithPID(drivetrain, drivetrain.getEncodersPIDSettings(), drivetrain.getEncodersPIDSettings(),
                drivetrain.getLeftDistance() / 2, drivetrain.getRightDistance() / 2,
                drivetrain::getLeftDistance, drivetrain::getRightDistance);
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
