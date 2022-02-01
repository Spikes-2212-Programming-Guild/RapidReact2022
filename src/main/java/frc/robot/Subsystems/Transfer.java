package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;

public class Transfer extends MotoredGenericSubsystem {
    /**
     * arbitrary speed value for compilability
     */
    public static final double SPEED = 0.5;

    /**
     * arbitrary constractor for compilability
     */
    public Transfer() {
        super("transfer");
    }

    public static Transfer getInstance() {
        return null;
    }

    /**
     * @return null for compilability
     */
    public static PIDSettings getPIDSettings() {
        return null;
    }

    /**
     * @return null for compilability
     */
    public static FeedForwardSettings getFFSettings() {
        return null;
    }
}
