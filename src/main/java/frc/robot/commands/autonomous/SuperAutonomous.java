package frc.robot.commands.autonomous;

import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class SuperAutonomous extends SequentialCommandGroup {

    private final Drivetrain drivetrain;

    public SuperAutonomous() {
        this.drivetrain = Drivetrain.getInstance();
    }

    private DriveTankWithPID returnByEncoders() {
        return new DriveTankWithPID(drivetrain, drivetrain.getEncodersPIDSettings(), drivetrain.getEncodersPIDSettings(),
                drivetrain.getLeftDistance() / 2, drivetrain.getRightDistance() / 2,
                drivetrain::getLeftDistance, drivetrain::getRightDistance);
    }
}
