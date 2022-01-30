package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.Subsystems.ClimberPlacer;

import java.util.function.Supplier;

/**
 * Moves the dynamic climbers downward towards the ground until they hit the third bar.
 */
public class MoveBetweenBars extends MoveGenericSubsystem {

    private static final RootNamespace rootNamespace = new RootNamespace("move between bars");

    // @TODO: when mechanical decides what motor is going to be used, Add an automatic calculation for the Stall current.
    /**
     * A value between the running current and the stalling current of the motors.
     */
    private static final Supplier<Double> STALL_CURRENT = rootNamespace.addConstantDouble("stall current", 0);
    private static final Supplier<Double> MOVEMENT_SPEED = rootNamespace.addConstantDouble("movement speed", 0);

    private final WPI_TalonSRX leftTalon;
    private final WPI_TalonSRX rightTalon;

    public MoveBetweenBars(ClimberPlacer dynamicClimber) {
        super(dynamicClimber, MOVEMENT_SPEED);
        this.leftTalon = dynamicClimber.getLeftTalon();
        this.rightTalon = dynamicClimber.getRightTalon();
    }

    /**
     * @return true if both motors are stalling, meaning that they hit the third bar. False otherwise
     */
    @Override
    public boolean isFinished() {
        return leftTalon.getStatorCurrent() > STALL_CURRENT.get() && rightTalon.getStatorCurrent() > STALL_CURRENT.get();
    }
}
