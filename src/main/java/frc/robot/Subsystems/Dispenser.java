package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.RobotMap;

public class Dispenser extends MotoredGenericSubsystem {

    private WPI_TalonSRX talon;
    private Encoder encoder;

    private static Dispenser instance;

    private Dispenser(WPI_TalonSRX talon) {
        super("dispenser", talon);
        this.talon = talon;
        this.encoder = new Encoder(RobotMap.DIO.DISPENSER_ENCODER_POS, RobotMap.DIO.DISPENSER_ENCODER_NEG);
    }

    public static Dispenser getInstance() {
        if (instance == null) {
            instance = new Dispenser(new WPI_TalonSRX(RobotMap.CAN.DISPENSER_TALON));
        }
        return instance;
    }
}
