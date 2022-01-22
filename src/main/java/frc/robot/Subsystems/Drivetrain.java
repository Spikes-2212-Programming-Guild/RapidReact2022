package frc.robot.Subsystems;


import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends TankDrivetrain {
    public PIDSettings aimToHubPID;
    public FeedForwardSettings aimToHubFeedForward;


    private final static Drivetrain INSTANCE = new Drivetrain();

    public static Drivetrain getInstance() {
        return INSTANCE;
    }

    private Drivetrain() {

    }
}

