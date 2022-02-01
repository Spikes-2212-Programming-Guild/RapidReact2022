package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;

public class IntakeToTransfer extends MotoredGenericSubsystem {

    public static final double SPEED = 0.5;

    public IntakeToTransfer() {
        super("intake to transfer");
    }

    public static IntakeToTransfer getInstance() {
        return null;
    }

}
