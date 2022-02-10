package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.Supplier;

public class IntakeToTransfer extends MotoredGenericSubsystem {

    public static final double SPEED = 0.7;

    private IntakeToTransfer(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
    }

    public static IntakeToTransfer getInstance(){
        return null;
    }

    //@todo
    public boolean getLimit(){
        return false;
    }
}
