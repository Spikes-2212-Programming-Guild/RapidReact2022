package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.Commands.Climb;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private final Supplier<Double> MOVEMENT_SPEED = rootNamespace.addConstantDouble("movement speed", 0);

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer(() -> 0.0, () -> 0.0, "", (MotorController) null);
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer(() -> 0.0, () -> 0.0, "", (MotorController) null);
        }
        return rightInstance;
    }

    public ClimberPlacer(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
    }

    public Supplier<Double> getUpSpeed() {
        return MOVEMENT_SPEED;
    }
}
