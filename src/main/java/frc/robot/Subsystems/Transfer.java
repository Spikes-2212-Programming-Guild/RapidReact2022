package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Transfer extends MotoredGenericSubsystem {

    public static final double SPEED = 0.6;

    private DigitalInput startLimit;

    private Transfer(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
        startLimit = new DigitalInput(RobotMap.DIO.TRANSFER_START_LIMIT);
    }

    public static Transfer getInstance() {
        return null;
    }

    public boolean isStartPressed() {
        return startLimit.get();
    }
}
