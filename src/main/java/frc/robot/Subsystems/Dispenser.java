package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;

public class Dispenser extends MotoredGenericSubsystem {
    public static final double TARGET_SPEED;
    public static final double TRANSFER_PLUS_DISPENSE_TIME;

    private PIDSettings pidSettings;
    private FeedForwardSettings feedForwardSettings;

    public static Dispenser getInstance(){}

    @Override
    public double getSpeed(){}

    public PIDSettings getPIDSettings() {
        return pidSettings;
    }

    public FeedForwardSettings getFeedForwardSettings() {
        return feedForwardSettings;
    }

}
