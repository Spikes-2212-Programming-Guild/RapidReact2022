package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.Supplier;

/**
 * Controls the climber winch which controls the height of the telescopic arms.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    private final Supplier<Double> UP_SPEED = rootNamespace.addConstantDouble("up speed", 0.6);
    private final Supplier<Double> DOWN_SPEED = rootNamespace.addConstantDouble("down speed", -0.25);
    private final Supplier<Double> HOOKED_DOWN_SPEED = rootNamespace.addConstantDouble("hooked down speed", -0.6);

    private static ClimberWinch instance;

    private ClimberWinch(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
    }

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(() -> -1.0, () -> 1.0, "movement speed", new WPI_TalonSRX(0));
        }
        return instance;
    }

    //@todo
    public boolean isHooked() {
        return false;
    }

    public Supplier<Double> getUpSpeed() {
        return UP_SPEED;
    }

    public Supplier<Double> getDownSpeed() {
        return DOWN_SPEED;
    }

    public Supplier<Double> getHookedDownSpeed() {
        return HOOKED_DOWN_SPEED;
    }
}
