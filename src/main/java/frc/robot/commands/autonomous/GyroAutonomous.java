package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeToTransfer;

public class GyroAutonomous extends SequentialCommandGroup {

    public static final double DRIVE_SPEED_TO_HUB = -0.7;

    public static final double RETREAT_DRIVE_SPEED = 0.7;
    public static final double RETREAT_DRIVE_ROTATE = -0.3;
    public static final double RETREAT_DRIVE_DURATION = 2.5;

    public GyroAutonomous(Drivetrain drivetrain) {
        super(
                new ParallelCommandGroup(
                        new IntakeCargo(),
                        new SequentialCommandGroup(
                                new MoveToCargo(drivetrain, MoveToCargo.CARGO_MOVE_VALUE),
                                new DriveArcade(drivetrain, MoveToCargo.CARGO_MOVE_VALUE, () -> 0.0)
                        ).withInterrupt(IntakeToTransfer.getInstance()::getLimit)
                ),
                new ReturnByGyro(drivetrain, 0).withTimeout(3),
                new DriveArcade(drivetrain, DRIVE_SPEED_TO_HUB, 0) {
                    @Override
                    public void end(boolean interrupted) {
                    }
                }.withTimeout(0.2),
                new DriveUntilHitHub(drivetrain).withTimeout(3),
                new ReleaseCargo().withTimeout(2),
                new DriveArcade(Drivetrain.getInstance(), RETREAT_DRIVE_SPEED, RETREAT_DRIVE_ROTATE).withTimeout(RETREAT_DRIVE_DURATION)
        );
    }
}
