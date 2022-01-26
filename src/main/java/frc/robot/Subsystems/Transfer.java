package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

public class Transfer extends MotoredGenericSubsystem {
    // a limit switch that is pressed when a cargo piece enters the system
    private DigitalInput entranceSensor;
    // a light sensor that sends a signal when a cargo piece exits the system
    private DigitalInput exitSensor;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_TALON));
        }
        return instance;
    }

    private Transfer(WPI_TalonSRX talon) {
        super("transfer", talon);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
        this.exitSensor = new DigitalInput(RobotMap.DIO.TRANSFER_EXIT_LIMIT_SWITCH);
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
