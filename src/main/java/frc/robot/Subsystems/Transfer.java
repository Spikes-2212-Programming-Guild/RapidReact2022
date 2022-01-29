package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Transports cargo from the bottom to the top of the robot
 */
public class Transfer extends MotoredGenericSubsystem {

    /**
     * A light sensor that sends a signal while a cargo is held at the bottom of the timing straps
     */
    private final DigitalInput entranceSensor;

    private final Limelight limelight;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_1),
                    new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_2)
            );
        }
        return instance;
    }

    private Transfer(WPI_TalonSRX talon1, WPI_TalonSRX talon2) {
        super("transfer", talon1, talon2);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
        this.limelight = new Limelight();
    }

    public Limelight getLimelight() {
        return limelight;
    }

    public boolean getStrapEntranceSensor() { return entranceSensor.get(); }
}
