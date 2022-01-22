package frc.robot.Subsystems;


import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends TankDrivetrain {
    private PIDSettings aimToHubPID;
    private FeedForwardSettings aimToHubFeedForward;


    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if (instance == null)
            instance = new Drivetrain();
        return instance;
    }

    private Drivetrain() {

    }

    public PIDSettings getAimToHubPID() {
        return aimToHubPID;
    }

    public FeedForwardSettings getAimToHubFeedForward() {
        return aimToHubFeedForward;
    }
}

