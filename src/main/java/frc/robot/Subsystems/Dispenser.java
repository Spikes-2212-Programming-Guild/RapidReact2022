package frc.robot.Subsystems;


import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.util.Limelight;

public class Dispenser extends MotoredGenericSubsystem {
    public Limelight limelight;

    private static Dispenser instance;

    public static Dispenser getInstance() {
        if (instance == null)
            instance = new Dispenser();
        return instance;
    }

    private Dispenser() {
    }
}

