package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import frc.robot.RobotMap;

public class IntakeToTransfer extends MotoredGenericSubsystem {
    private static IntakeToTransfer instance;

    public static IntakeToTransfer getInstance() {
        if (instance == null) {
            instance = new IntakeToTransfer(new WPI_TalonSRX(RobotMap.CAN.INTAKE_TO_TRANSFER_TALON));
        }
        return instance;
    }

    public IntakeToTransfer(WPI_TalonSRX talon) {
        super("intake to transfer", talon);
    }
}
