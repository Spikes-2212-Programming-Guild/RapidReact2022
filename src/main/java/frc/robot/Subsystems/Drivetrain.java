package frc.robot.Subsystems;


import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends TankDrivetrain {
    public PIDSettings aimToHubPID;
    public FeedForwardSettings aimToHubFeedForward;

    // With eager singleton initialization, any static variables/fields used in the 
    // constructor must appear before the "INSTANCE" variable so that they are initialized 
    // before the constructor is called when the "INSTANCE" variable initializes.

    private final static Drivetrain INSTANCE = new Drivetrain();

    public static Drivetrain getInstance() {
        return INSTANCE;
    }

    private Drivetrain() {
        // TODO: Set the default command, if any, for this subsystem by calling setDefaultCommand(command)
        //       in the constructor or in the robot coordination class, such as RobotContainer.
        //       Also, you can call addChild(name, sendableChild) to associate sendables with the subsystem
        //       such as SpeedControllers, Encoders, DigitalInputs, etc.
    }
}

