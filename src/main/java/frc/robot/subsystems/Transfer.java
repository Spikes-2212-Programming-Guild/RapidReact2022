package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Transports cargo from the bottom to the top of the robot.
 */
public class Transfer extends MotoredGenericSubsystem {

    public final Supplier<Double> MOVE_SPEED = rootNamespace.addConstantDouble("move speed", -0.4);
    public final Supplier<Double> FIRST_CARGO_RELEASE_SPEED = rootNamespace.addConstantDouble("release speed", -0.8);
    public final Supplier<Double> SECOND_CARGO_RELEASE_SPEED = rootNamespace.addConstantDouble("second cargo release speed", -0.6);

    /**
     * A light sensor that sends a signal while a cargo is held at the bottom of the timing straps.
     */
    private final DigitalInput entranceSensor;

    private static Transfer instance;

    public static Transfer getInstance() {
        if (instance == null) {
            instance = new Transfer(new WPI_VictorSPX(RobotMap.CAN.TRANSFER_STRAP_VICTOR_1),
                    new WPI_VictorSPX(RobotMap.CAN.TRANSFER_STRAP_VICTOR_2)
            );
        }
        return instance;
    }

    private Transfer(WPI_VictorSPX victor1, WPI_VictorSPX victor2) {
        super("transfer", victor1, victor2);
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
