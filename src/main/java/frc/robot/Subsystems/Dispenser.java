package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.RobotMap;

/**
 * drops cargo into the lower hub
 */
public class Dispenser extends MotoredGenericSubsystem {

    private Encoder encoder;
    private Limelight limelight;

    private static Dispenser instance;

    public static Dispenser getInstance() {
        if (instance == null) {
            instance = new Dispenser(new WPI_TalonSRX(RobotMap.CAN.DISPENSER_TALON));
        }
        return instance;
    }

    private Dispenser(WPI_TalonSRX talon) {
        super("dispenser", talon);
        this.encoder = new Encoder(RobotMap.DIO.DISPENSER_ENCODER_POS, RobotMap.DIO.DISPENSER_ENCODER_NEG);
        this.limelight = new Limelight();
    }

    /**
     * add sensor data to dashboard
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putNumber("encoder", encoder::get);
    }
}
