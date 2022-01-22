package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;

public class Dispenser extends MotoredGenericSubsystem {
    public static final double SPEED;
    public static final double DISPENSER_WARM_UP_TIME = 3;
    public static final double TRANSFER_PLUS_DISPENSE_TIME = 5;

    public static Dispenser getInstance(){}
}
