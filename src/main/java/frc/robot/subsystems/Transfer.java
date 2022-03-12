package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.BustedMotorControllerGroup;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Transports cargo from the bottom to the top of the robot.
 */
public class Transfer extends MotoredGenericSubsystem {

    private static final RootNamespace corrections = new RootNamespace("corrections");

    public static final Supplier<Double> CORRECTION_1 = corrections.addConstantDouble("correction 1", 1);
    public static final Supplier<Double> CORRECTION_2 = corrections.addConstantDouble("correction 2", 1);

    public final Supplier<Double> MOVE_SPEED = rootNamespace.addConstantDouble("move speed", 0.3);
    public final Supplier<Double> FIRST_CARGO_RELEASE_SPEED = rootNamespace.addConstantDouble("release speed", 0.65);
    public final Supplier<Double> SECOND_CARGO_RELEASE_SPEED = rootNamespace.addConstantDouble("second cargo release speed", 0.45);

    /**
     * A light sensor that sends a signal while a cargo is held at the bottom of the timing straps.
     */
    private final DigitalInput entranceSensor;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(
                    new BustedMotorControllerGroup(CORRECTION_1, new WPI_VictorSPX(RobotMap.CAN.TRANSFER_STRAP_VICTOR_1)),
                    new BustedMotorControllerGroup(CORRECTION_2, new WPI_VictorSPX(RobotMap.CAN.TRANSFER_STRAP_VICTOR_2)
                    )
            );
        }
        return instance;
    }

    private Transfer(MotorController motor1, MotorController motor2) {
        super("transfer", motor1, motor2);
        this.entranceSensor = new DigitalInput(RobotMap.DIO.TRANSFER_ENTRANCE_LIGHT_SENSOR);
    }

    public boolean getEntranceSensor() {
        return !entranceSensor.get();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("transfer", new MoveGenericSubsystem(this, MOVE_SPEED));
        rootNamespace.putBoolean("light sensor", this::getEntranceSensor);
    }
}
