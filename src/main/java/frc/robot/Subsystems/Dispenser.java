package frc.robot.Subsystems;


import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Dispenser extends SubsystemBase {
    public Limelight limelight;

    private final static Dispenser INSTANCE = new Dispenser();

    public static Dispenser getInstance() {
        return INSTANCE;
    }

    private Dispenser() {
    }
}

