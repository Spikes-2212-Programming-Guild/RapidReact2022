package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.RobotMap;

public class Dispenser extends MotoredGenericSubsystem {

    private static final double DISTANCE_PER_PULSE = 20.32 / 4096.0;

    private Encoder encoder;

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
        this.encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putNumber("encoder ticks", encoder::get);
        rootNamespace.putNumber("encoder distance", encoder::getDistance);
    }
}
