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
    private final RootNamespace rootNamespace = new RootNamespace("transfer");
    private final Supplier<Double> entranceMotorSpeed = rootNamespace.addConstantDouble("entrance motor speed", 0);
    private final Supplier<Double> strapSpeed = rootNamespace.addConstantDouble("strap speed", 0);

    // a limit switch that is pressed when a cargo piece is held in the entrance motor
    private final DigitalInput entranceMotorSensor;
    // a light sensor that sends a signal while a cargo is held at the bottom of the timing straps
    private final DigitalInput strapEntranceSensor;

    private final Limelight limelight;
    private final MotorControllerGroup strapMotors;
    private final WPI_TalonSRX entranceTalon;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_TalonSRX(RobotMap.CAN.TRANSFER_ENTRANCE_MOTOR),
                    new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_1),
                    new WPI_TalonSRX(RobotMap.CAN.TRANSFER_STRAP_TALON_2)
            );
        }
        return instance;
    }

    private Transfer(WPI_TalonSRX entranceTalon, WPI_TalonSRX strapTalon1, WPI_TalonSRX strapTalon2) {
        strapTalon1.setInverted(true);
        this.strapMotors = new MotorControllerGroup(strapTalon1, strapTalon2);
        this.entranceTalon = entranceTalon;
        this.entranceMotorSensor = new DigitalInput(RobotMap.DIO.TRANSFER_WHEEL_ENTRANCE_LIMIT_SWITCH);
        this.strapEntranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_STRAP_LIGHT_SENSOR);
        this.limelight = new Limelight();
    }

    @Override
    protected void apply(double speed) {
        if (!entranceMotorSensor.get() && strapEntranceSensor.get()) {
            entranceTalon.set(entranceMotorSpeed.get());
        } else {
            strapMotors.set(strapSpeed.get());
            entranceTalon.set(entranceMotorSpeed.get());
        }
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        entranceTalon.stopMotor();
        strapMotors.stopMotor();
    }

    @Override
    public void periodic() {
        rootNamespace.update();
    }

    public Limelight getLimelight() {
        return limelight;
    }

    public boolean getEntranceMotorLimit() {
        return entranceMotorSensor.get();
    }

    /**
     * add sensor data to dashboard
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putBoolean("motor entrance sensor", entranceMotorSensor.get());
        rootNamespace.putBoolean("strap entrance sensor", strapEntranceSensor.get());
    }
}
