package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

public class Transfer extends GenericSubsystem {
    WPI_TalonSRX talon;
    DigitalInput entranceSensor;
    DigitalInput exitSensor;

    private static Transfer instance;

    private Transfer() {
        super();
        this.talon = new WPI_TalonSRX(RobotMap.CAN.TRANSFER_TIMING_BELT_TALON);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
        this.exitSensor = new DigitalInput(RobotMap.DIO.TRANSFER_EXIT_LIMIT_SWITCH);
    }

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer();
        }

        return instance;
    }

    @Override
    protected void apply(double speed) {
        talon.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        talon.stopMotor();
    }
}
