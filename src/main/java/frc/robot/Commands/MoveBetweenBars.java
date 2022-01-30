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

    private boolean leftPassedFirstStall = false;
    private boolean rightPassedFirstStall = false;
    private boolean leftStalled = false;
    private boolean rightStalled = false;

    public MoveBetweenBars(ClimberPlacer dynamicClimber) {
        super(dynamicClimber, MOVEMENT_SPEED);
        this.leftTalon = dynamicClimber.getLeftTalon();
        this.rightTalon = dynamicClimber.getRightTalon();
    }

    @Override
    public void execute() {
        if (leftTalon.getStatorCurrent() < STALL_CURRENT.get() && !leftPassedFirstStall) {
            leftTalon.set(MOVEMENT_SPEED.get());
            leftPassedFirstStall = true;
        } else if (leftTalon.getStatorCurrent() < STALL_CURRENT.get()){
            leftTalon.set(MOVEMENT_SPEED.get());
        } else {
            leftStalled = true;
        }
        if (rightTalon.getStatorCurrent() < STALL_CURRENT.get() && !rightPassedFirstStall) {
            rightTalon.set(MOVEMENT_SPEED.get());
            rightPassedFirstStall = true;
        } else if (rightTalon.get() < STALL_CURRENT.get()) {
            rightTalon.set(MOVEMENT_SPEED.get());
        } else {
            rightStalled = true;
        }
    }

    /**
     * @return true if both motors are stalling, meaning that they hit the third bar. False otherwise
     */
    @Override
    public boolean isFinished() {
        return leftStalled && rightStalled;
    }
}
