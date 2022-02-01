package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;

public class IntakeToTransfer extends MotoredGenericSubsystem {

    /**
     * arbitrary speed value for compilability
     */
    public static final double SPEED = 0.5;

    /**
     * arbitrary constractor for compilability
     */
    public IntakeToTransfer() {
        super("intake to transfer");
    }

    /**
     * @return null for compilability
     */
    public static IntakeToTransfer getInstance() {
        return null;
    }

    /**
     * @return false for compilability
     */
    public boolean getLimit() {
        return false;
    }

}
