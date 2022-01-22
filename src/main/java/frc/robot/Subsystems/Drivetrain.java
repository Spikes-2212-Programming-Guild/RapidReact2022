package frc.robot.Subsystems;


import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardController;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;

public class Drivetrain extends TankDrivetrain {


    private final static Drivetrain instance = new Drivetrain();


    public PIDSettings aimToHubPID;
    public FeedForwardSettings aimToHubFeedForward;

    private Drivetrain() {

    }

    public static Drivetrain getInstance() {
        return instance;
    }

}

