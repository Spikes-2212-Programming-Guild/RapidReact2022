package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

public class IntakeToTransfer extends MotoredGenericSubsystem {
    private static IntakeToTransfer instance;

    private DigitalInput limit;
    private Transfer transfer;

    public static IntakeToTransfer getInstance() {
        if (instance == null) {
            instance = new IntakeToTransfer(new WPI_TalonSRX(RobotMap.CAN.INTAKE_TO_TRANSFER_TALON));
        }
        return instance;
    }

    private IntakeToTransfer(WPI_TalonSRX talon) {
        super("intake to transfer", talon);
        this.limit = new DigitalInput(RobotMap.DIO.INTAKE_TO_TRANSFER_LIMIT);
        this.transfer = Transfer.getInstance();
    }

    public boolean getLimit() {
        return limit.get();
    }

    @Override
    public boolean canMove(double speed) {
        return !(transfer.getStrapEntranceSensor() && limit.get());
    }
}
