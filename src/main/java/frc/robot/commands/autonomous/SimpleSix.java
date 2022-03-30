package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeToTransfer;

public class SimpleSix extends SequentialCommandGroup {

    public static final double RETREAT_DRIVE_SPEED = 0.7;
    public static final double RETREAT_DRIVE_ROTATE = -0.3;
    public static final double RETREAT_DRIVE_DURATION = 2.5;

    public static final double DRIVE_UNTIL_HIT_HUB_TIMEOUT = 3;
    public static final double RELEASE_CARGO_TIMEOUT = 3;

    public SimpleSix() {
        Drivetrain drivetrain = Drivetrain.getInstance();
        addCommands(
                new ParallelCommandGroup(
                        new IntakeCargo(false),
                        new DriveArcade(drivetrain, MoveToCargoWithIntake.CARGO_MOVE_VALUE, 0.0)
                                .withInterrupt(IntakeToTransfer.getInstance()::getLimit)
                ).withTimeout(MoveToCargoWithIntake.MOVE_TO_CARGO_TIMEOUT),
                new DriveUntilHitHub(drivetrain).withTimeout(DRIVE_UNTIL_HIT_HUB_TIMEOUT),
                new ReleaseCargo().withTimeout(RELEASE_CARGO_TIMEOUT),
                new DriveArcade(drivetrain, RETREAT_DRIVE_SPEED, RETREAT_DRIVE_ROTATE)
                        .withTimeout(RETREAT_DRIVE_DURATION)
        );
    }
}
