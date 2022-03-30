package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_HUB_ROTATE_VALUE = rootNamespace.addConstantDouble("seek rotate value", 0.4);
    private static final Supplier<Double> SEEK_HUB_TOLERANCE = rootNamespace.addConstantDouble("seek rotate tolerance", 18);

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
    }

    private DriveTankWithPID returnByEncoders() {
        return new DriveTankWithPID(drivetrain, drivetrain.getEncodersPIDSettings(), drivetrain.getEncodersPIDSettings(),
                drivetrain.getLeftDistance() / 2, drivetrain.getRightDistance() / 2,
                drivetrain::getLeftDistance, drivetrain::getRightDistance);
    }

    private Command seekHub() {
        Limelight limelight = drivetrain.getLimelight();
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_HUB_ROTATE_VALUE).withInterrupt(() ->
                -SEEK_HUB_TOLERANCE.get() <= limelight.getHorizontalOffsetFromTarget() &&
                limelight.getHorizontalOffsetFromTarget() <= SEEK_HUB_TOLERANCE.get());
    }
}
