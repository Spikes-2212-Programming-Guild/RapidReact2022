package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.Limelight;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Transports cargo from the bottom to the top of the robot.
 */
public class Transfer extends MotoredGenericSubsystem {

    private final Supplier<Double> speed = rootNamespace.addConstantDouble("speed", -0.5);
    private final Supplier<Double> transferMoveTimeout = rootNamespace.addConstantDouble("transfer move timeout", 0.5);

    /**
     * A light sensor that sends a signal while a cargo is held at the bottom of the timing straps.
     */
    private final DigitalInput entranceSensor;

    private final Limelight limelight;

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
        this.limelight = new Limelight();
    }

    public Limelight getLimelight() {
        return limelight;
    }

    public boolean getStrapEntranceSensor() {
        return entranceSensor.get();
    }

    public double getTransferMoveTimeout() {
        return transferMoveTimeout.get();
    }

    public Supplier<Double> getTransferSpeed(){
        return speed;
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("transfer", new MoveGenericSubsystem(this, speed.get()));
    }
}
