package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ReleaseCargo;
import frc.robot.subsystems.Drivetrain;

public class YeetAndRetreat extends SequentialCommandGroup {

    public static final double RELEASE_DURATION = 1.0;
    public static final double DRIVE_SPEED = 0.7;
    public static final double DRIVE_ROTATE = 0;
    public static final double DRIVE_DURATION = 1;

    public YeetAndRetreat() {
        super(
                new ReleaseCargo().withTimeout(RELEASE_DURATION),
                new DriveArcade(Drivetrain.getInstance(), DRIVE_SPEED, DRIVE_ROTATE).withTimeout(DRIVE_DURATION)
        );
    }
}
