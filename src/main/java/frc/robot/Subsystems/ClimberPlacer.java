package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    public static ClimberPlacer getLeftInstance() {
        return null;
    }

    public static ClimberPlacer getRightInstance() {
        return null;
    }

    public ClimberPlacer(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
    }

    public Supplier<Double> getDropSpeed() {
        return null;
    }
}
