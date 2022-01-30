package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.Supplier;

public class ClimberWinch extends MotoredGenericSubsystem {

    private Supplier<Double> UP_SPEED = rootNamespace.addConstantDouble("up speed", 0.6);
    private Supplier<Double> DOWN_SPEED = rootNamespace.addConstantDouble("down speed", -0.4);
    private Supplier<Double> HOOKED_DOWN_SPEED = rootNamespace.addConstantDouble("hooked down speed", -0.6);

    private ClimberWinch(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
    }

    public static ClimberWinch getInstance() {
        return null;
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
