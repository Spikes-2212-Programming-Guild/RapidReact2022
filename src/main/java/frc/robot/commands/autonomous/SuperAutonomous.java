package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.OI;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import java.util.function.Supplier;

/**
 * 1. Releases the cargo the robot starts with to the lower hub while lowering to latch to drop the intake placer. <br>
 * 2. Moves to a cargo using image processing to intake this cargo. <br>
 * 3. Rotates until the image processing camera locates another cargo. <br>
 * 4. Moves to this cargo using image processing to intake this cargo. <br>
 * 5. Returns halfway (between both intaken cargos). <br>
 * 6. Rotates until the limelight finds the higher hub. <br>
 * 7. Focuses the middle of the hub. <br>
 * 8. Drives until it hits the fender. <br>
 * 9. Release both intaken cargos to the lower hub.
 */
public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_ROTATE_VALUE = rootNamespace.addConstantDouble("seek rotate value", 0.5);
    private static final Supplier<Double> SEEK_HUB_TOLERANCE = rootNamespace.addConstantDouble("seek hub tolerance", 18);


    public static final double FIRST_RELEASE_CARGO_TIMEOUT = 1;

    public static final double INTAKE_FIRST_CARGO_TIMEOUT = 2.5;
    public static final double INTAKE_SECOND_CARGO_TIMEOUT = 2;

    public static final double SEEK_CARGO_TOLERANCE = 40;
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
        OI oi = new OI();
        addCommands(
//                releaseCargoAndLatch(),

                new MoveToCargoWithIntake(drivetrain, MoveToCargoWithIntake.CARGO_MOVE_VALUE)
                        .withTimeout(INTAKE_FIRST_CARGO_TIMEOUT),

                seekCargo(),
                new InstantCommand(() -> {
                    secondCargoStartTime = Timer.getFPGATimestamp();
                    rootNamespace.putNumber("start time", secondCargoStartTime);
                }),
                new MoveToCargoWithIntake(drivetrain, MoveToCargoWithIntake.CARGO_MOVE_VALUE),
                new InstantCommand(() -> {
                    secondCargoFinishTime = Timer.getFPGATimestamp();
                    rootNamespace.putNumber("end time", secondCargoFinishTime);
                }),
                returnByHalf(),

                seekHub().withTimeout(SEEK_HUB_TIMEOUT)

//                aimToHub(),
//                new DriveUntilHitHub(drivetrain).withTimeout(DRIVE_UNTIL_HIT_HUB_TIMEOUT)//,
//                new ReleaseCargo().withTimeout(SECOND_RELEASE_CARGO_TIMEOUT)
        );
    }

    private ParallelCommandGroup releaseCargoAndLatch() {
//        return new ParallelCommandGroup(
//                new InstantCommand(() -> IntakePlacer.getInstance().setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get())),
//                new ReleaseCargo().withTimeout(FIRST_RELEASE_CARGO_TIMEOUT)
//        );
        return new ParallelCommandGroup(new WaitCommand(FIRST_RELEASE_CARGO_TIMEOUT));
    }

    private Command returnByHalf() {
        return new DriveArcade(drivetrain, () -> -MoveToCargoWithIntake.CARGO_MOVE_VALUE, () -> 0.0).withTimeout(
                ((secondCargoFinishTime - secondCargoStartTime) / 2) + 1);
    }

    private Command seekHub() {
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE).withInterrupt(() ->
                Math.abs(getCargoX()) <= SEEK_CARGO_TOLERANCE);
    }

    private DriveArcade seekCargo() {
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE, () -> (MoveToCargoWithIntake.hasCargoTarget() &&
                Math.abs(MoveToCargoWithIntake.getCargoX() - MoveToCargoWithIntake.SETPOINT) <= SEEK_CARGO_TOLERANCE));
    }

    //@todo
    private DriveArcadeWithPID aimToHub() {

        return null;
    }


    /**
     * @return the x coordinate of the center of the cargo contour, as received by the image processing.
     */
    public static double getCargoX() {
        try {
            return -getImageGreenProcessingNamespace().getNumber("x");
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @return whether the image processing camera has a target
     */
    public static boolean hasGreenTarget() {
        try {
            return getImageGreenProcessingNamespace().getBoolean("isUpdated");
        } catch (Exception e) {
            return false;
        }
    }

    private static Namespace getImageGreenProcessingNamespace() {
        RootNamespace imageProcess = new RootNamespace("Image Processing GREEN");
        return imageProcess.addChild("contour 0");
    }
}
