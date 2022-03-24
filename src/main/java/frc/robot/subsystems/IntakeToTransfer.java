package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Uses a wheel located between the transfer and intake subsystem to hold/move cargo pieces.
 */
public class IntakeToTransfer extends MotoredGenericSubsystem {

    public static final double SPEED = 0.8;

    private static IntakeToTransfer instance;

    private final DigitalInput limit;

    public static IntakeToTransfer getInstance() {
        if (instance == null)
            instance = new IntakeToTransfer(new WPI_VictorSPX(RobotMap.CAN.INTAKE_TO_TRANSFER_VICTOR));

        return instance;
    }

    private IntakeToTransfer(WPI_VictorSPX victor) {
        super("intake to transfer", victor);
        this.limit = new BustedDigitalInput(RobotMap.DIO.INTAKE_TO_TRANSFER_LIMIT);
    }

    public boolean getLimit() {
        return limit.get();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("intake to transfer", new MoveGenericSubsystem(this, SPEED));
        rootNamespace.putBoolean("limit switch", this::getLimit);
    }
}
