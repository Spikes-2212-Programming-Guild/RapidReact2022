package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeToTransfer;

public class GyroAutonomous extends SequentialCommandGroup {

    public static final double DRIVE_SPEED_TO_CARGO = 0.3;
    public static final double DRIVE_SPEED_TO_HUB = -0.5;

    public GyroAutonomous() {
        super(
                new ParallelDeadlineGroup(
                        new IntakeCargo(),
                        new SequentialCommandGroup(
                                new MoveToCargo(Drivetrain.getInstance()).withInterrupt(
                                        IntakeToTransfer.getInstance()::getLimit),
                                new DriveArcade(Drivetrain.getInstance(), DRIVE_SPEED_TO_CARGO, 0)
                        )
                ),
                new ReturnByGyro(Drivetrain.getInstance(), 0),
                new DriveArcade(Drivetrain.getInstance(), DRIVE_SPEED_TO_HUB, 0) {
                    @Override
                    public void end(boolean interrupted) {
                    }
                }.withTimeout(0.3),
                new DriveUntilHitHub(Drivetrain.getInstance())
                        .withTimeout(3),
                new ReleaseCargo().withTimeout(2)
        );
    }
}
