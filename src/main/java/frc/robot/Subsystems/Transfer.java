package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

public class Transfer extends MotoredGenericSubsystem {
    private WPI_TalonSRX talon;
    private DigitalInput entranceSensor;
    private DigitalInput exitSensor;

    private static Transfer instance;

    private Transfer(WPI_TalonSRX talon) {
        super("Transfer", talon);
        this.talon = talon;
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
        this.exitSensor = new DigitalInput(RobotMap.DIO.TRANSFER_EXIT_LIMIT_SWITCH);
    }

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_TALON));
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
