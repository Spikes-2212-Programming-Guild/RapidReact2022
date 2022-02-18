package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.MoveToCargo;
import frc.robot.commands.ReleaseCargo;
import frc.robot.commands.ReturnByGyro;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeToTransfer;

public class GyroAutonomous extends SequentialCommandGroup {

    public static final double SPEED = 0.3;

    public GyroAutonomous() {
        super(
                new ParallelDeadlineGroup(
                        new IntakeCargo(),
                        new SequentialCommandGroup(
                                new MoveToCargo(Drivetrain.getInstance()).withInterrupt(
                                        IntakeToTransfer.getInstance()::getLimit),
                                new DriveArcade(Drivetrain.getInstance(), SPEED, 0)
                        )
                ),
                new ReturnByGyro(Drivetrain.getInstance(), 0),
//                new DriveUntilHitHub()
                new ReleaseCargo()
        );
    }
}
