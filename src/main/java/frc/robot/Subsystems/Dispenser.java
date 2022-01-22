package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Dispenser extends GenericSubsystem {
    public static final double SPEED;
    public static final double DISPENSER_WARM_UP_TIME = 3;
    public static final double TRANSFER_PLUS_DISPENSE_TIME = 5;

    public static Dispenser getInstance(){}
}
