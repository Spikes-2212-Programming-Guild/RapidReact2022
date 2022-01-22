package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Transfer extends MotoredGenericSubsystem {

    public static final double SPEED = 0.6;
    public static final double RETURN_CARGO_TIME = 1;

    /**
     * The limit at the bottom of this subsystem. When it is pressed, there is a cargo at the start of the subsystem.
     */
    private DigitalInput startLimit;

    /**
     * The limit at the top of this subsystem. When it is pressed, there is a cargo at the top of the subsystem.
     */
    private DigitalInput topLimit;

    private Transfer(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, String namespaceName, MotorController... motorControllers) {
        super(minSpeed, maxSpeed, namespaceName, motorControllers);
        startLimit = new DigitalInput(RobotMap.DIO.TRANSFER_START_LIMIT);
        topLimit = new DigitalInput(RobotMap.DIO.TRANSFER_TOP_LIMIT);
    }

    public static Transfer getInstance(){
        return null;
    }

    public boolean isStartPressed(){
        return startLimit.get();
    }

    public boolean isTopPressed(){
        return topLimit.get();
    }

    @Override
    public boolean canMove(double speed) {
        return !isTopPressed();
    }
}
