package frc.robot.Subsystems;


import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.util.Limelight;

public class Transfer extends MotoredGenericSubsystem {
    public Limelight limelight;

    private static Transfer instance;


    public static Transfer getInstance() {
        if (instance == null)
            instance = new Transfer();
        return instance;
    }

    private Transfer() {
    }

    public Limelight getLimelight() {return limelight;}
}

