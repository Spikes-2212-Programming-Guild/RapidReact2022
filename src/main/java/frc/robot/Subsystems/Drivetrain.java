package frc.robot.Subsystems;


import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.util.PigeonWrapper;

public class Drivetrain extends TankDrivetrain {
    private PIDSettings pidSettings;
    private FeedForwardSettings ffSettings;

    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if (instance == null)
            instance = new Drivetrain();
        return instance;
    }

    private Drivetrain() {

    }

    public PIDSettings getPIDSettings() {
        return pidSettings;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }
}

