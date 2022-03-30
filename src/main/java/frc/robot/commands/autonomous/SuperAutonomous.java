package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class SuperAutonomous extends SequentialCommandGroup {

    private static final RootNamespace rootNamespace = new RootNamespace("super auto");
    private static final Supplier<Double> SEEK_ROTATE_VALUE = rootNamespace.addConstantDouble("Seek rotate value", 0.4);

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        drivetrain = Drivetrain.getInstance();
    }

    private Command seekHub() {
        return new DriveArcade(drivetrain, () -> 0.0, SEEK_ROTATE_VALUE) {
            @Override
            public boolean isFinished() {
                return drivetrain.getLimelight().isOnTarget();
            }
        };
    }
}
