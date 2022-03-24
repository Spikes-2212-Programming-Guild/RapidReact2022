package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeToTransfer;

public class GyroAutonomous extends SequentialCommandGroup {

    public static final double MOVE_SERVO_DOWN_TIMEOUT = 1;

    public static final double DRIVE_SPEED_GYRO = -0.5;

    public static final double RETREAT_DRIVE_SPEED = 0.7;
    public static final double RETREAT_DRIVE_ROTATE = -0.3;
    public static final double RETREAT_DRIVE_DURATION = 2.5;

    public static final double RETURN_BY_GYRO_TIMEOUT = 3;
    public static final double DRIVE_UNTIL_HIT_HUB_TIMEOUT = 3;
    public static final double RELEASE_CARGO_TIMEOUT = 3;

    public GyroAutonomous() {
        Drivetrain drivetrain = Drivetrain.getInstance();
        addCommands(
                new InstantCommand(() -> IntakePlacer.getInstance().setServoAngle(IntakePlacer.SERVO_TARGET_ANGLE.get())),
                new WaitCommand(MOVE_SERVO_DOWN_TIMEOUT),
                new ParallelCommandGroup(
                        new IntakeCargo(false),
                        new SequentialCommandGroup(
                                new MoveToCargo(drivetrain, MoveToCargo.CARGO_MOVE_VALUE),
                                new DriveArcade(drivetrain, MoveToCargo.CARGO_MOVE_VALUE, () -> 0.0)
                        ).withInterrupt(IntakeToTransfer.getInstance()::getLimit)
                ).withTimeout(MoveToCargo.MOVE_TO_CARGO_TIMEOUT),
                //Moves the robot back to its original position using the gyro sensor.
                new DriveArcadeWithPID(drivetrain, () -> -drivetrain.getYaw(), 0, DRIVE_SPEED_GYRO,
                        drivetrain.getGyroPIDSettings(), drivetrain.getFFSettings()).withTimeout(RETURN_BY_GYRO_TIMEOUT),
                new DriveUntilHitHub(drivetrain).withTimeout(DRIVE_UNTIL_HIT_HUB_TIMEOUT),
                new ReleaseCargo().withTimeout(RELEASE_CARGO_TIMEOUT),
                new ParallelCommandGroup(
                        new IntakePlacerUp(),
                        new DriveArcade(Drivetrain.getInstance(), RETREAT_DRIVE_SPEED, RETREAT_DRIVE_ROTATE).withTimeout(RETREAT_DRIVE_DURATION)
                )
        );
    }
}
