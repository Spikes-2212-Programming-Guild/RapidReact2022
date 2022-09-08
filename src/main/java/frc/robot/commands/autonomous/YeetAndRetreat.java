package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ReleaseCargo;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class YeetAndRetreat extends SequentialCommandGroup {

    public static final double RELEASE_DURATION = 1.0;
    public static final double DRIVE_SPEED = 0.7;
    public static final double DRIVE_ROTATE = 0;
    public static final double DRIVE_DURATION = 1.2212;

    private static RootNamespace rootNamespace = new RootNamespace("yeet and retreat");

    /**
     * The time the robot waits between releasing the cargo and driving away. It is there to limit the interference
     * between our the alliance's teams' autonomous.
     */
    public static final Supplier<Double> WAIT_DURATION = rootNamespace.addConstantDouble("wait duration", 0);

    public YeetAndRetreat() {
        super(
                new ReleaseCargo().withTimeout(RELEASE_DURATION),
                new WaitCommand(WAIT_DURATION.get()),
                new DriveArcade(Drivetrain.getInstance(), DRIVE_SPEED, DRIVE_ROTATE).withTimeout(DRIVE_DURATION)
        );
    }
}
