package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * transports cargo from the bottom to the top of the robot
 */
public class Transfer extends GenericSubsystem {
    // a light sensor that sends a signal while a cargo is held at the bottom of the timing straps
    private final DigitalInput entranceSensor;

    private final Limelight limelight;
    private final MotorControllerGroup strapMotors;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_1),
                    new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_2)
            );
        }
        return instance;
    }

    private Transfer(WPI_TalonSRX strapTalon1, WPI_TalonSRX strapTalon2) {
        this.strapMotors = new MotorControllerGroup(strapTalon1, strapTalon2);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_STRAP_LIGHT_SENSOR);
        this.limelight = new Limelight();
    }

    @Override
    protected void apply(double speed) {
        strapMotors.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        strapMotors.stopMotor();
    }

    public Limelight getLimelight() {
        return limelight;
    }

    public boolean getStrapEntranceLimit() { return entranceSensor.get(); }
}
