package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * transports cargo from the bottom to the top of the robot
 */
public class Transfer extends MotoredGenericSubsystem {
    // a limit switch that is pressed when a cargo piece enters the system
    private final DigitalInput entranceSensor;
    // a light sensor that sends a signal when a cargo piece exits the system
    private final DigitalInput exitSensor;
    private final Limelight limelight;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_TALON_1), new WPI_TalonSRX(RobotMap.CAN.TRANSFER_TALON_2));
        }
        return instance;
    }

    private Transfer(WPI_TalonSRX talon1, WPI_TalonSRX talon2) {
        super("transfer", talon1, talon2);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
        this.exitSensor = new DigitalInput(RobotMap.DIO.TRANSFER_EXIT_LIMIT_SWITCH);
        this.limelight = new Limelight();
    }

    public Limelight getLimelight() {
        return limelight;
    }

    /**
     * add sensor data to dashboard
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putBoolean("entrance sensor", entranceSensor.get());
        rootNamespace.putBoolean("exit sensor", exitSensor.get());
    }
}
