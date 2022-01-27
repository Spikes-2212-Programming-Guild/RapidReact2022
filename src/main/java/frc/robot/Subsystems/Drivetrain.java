package frc.robot.Subsystems;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Drivetrain extends TankDrivetrain {

    private Drivetrain(MotorController left, MotorController right) {
        super(left, right);
    }

    public static Drivetrain getInstance() {
        return null;
    }

    //@todo
    public double getYaw() {
        return 0;
    }

    public PIDSettings getPIDSettings() {
        return null;
    }

    public FeedForwardSettings getFFSettings() {
        return null;
    }
}
