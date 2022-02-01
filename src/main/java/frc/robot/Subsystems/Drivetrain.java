package frc.robot.Subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.util.PigeonWrapper;

public class Drivetrain extends TankDrivetrain {

    private Drivetrain() {
        super(new WPI_TalonSRX(0), new WPI_TalonSRX(0));
    }

    public PIDSettings getPIDSettings() {
        return null;
    }

    public FeedForwardSettings getFFSettings() {
        return null;
    }
}
