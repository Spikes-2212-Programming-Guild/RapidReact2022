package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private final Supplier<Double> STALL_CURRENT = rootNamespace.addConstantDouble("stall current", 0);

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;

    private static ClimberPlacer rightInstance, leftInstance;

    private final Supplier<Double> MOVEMENT_SPEED = rootNamespace.addConstantDouble("movement speed", 0);

    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    private final WPI_TalonSRX placer;

    private boolean stalling = false;
    private boolean passedFirstStall = false;

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_RIGHT));
        }
        return rightInstance;
    }

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_LEFT));
        }
        return leftInstance;
    }

    private ClimberPlacer(WPI_TalonSRX placer) {
        super(MIN_SPEED, MAX_SPEED, "right climber placer", placer);
        this.placer = placer;
        this.frontLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_FRONT);
        this.backLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_BACK);
    }

    @Override
    protected void apply(double speed) {
        if (!passedFirstStall && placer.getStatorCurrent() > STALL_CURRENT.get())
            stalling = true;
        else if (!passedFirstStall && stalling && placer.getStatorCurrent() <= STALL_CURRENT.get()) {
            stalling = false;
            passedFirstStall = true;
        }
        else if (passedFirstStall && placer.getStatorCurrent() >= STALL_CURRENT.get())
            stalling = true;

        super.apply(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return !(ClimberWinch.getInstance().isHooked() || speed > 0 && frontLimit.get() || speed < 0 && backLimit.get()) &&
                !(passedFirstStall && stalling);
    }

    public WPI_TalonSRX getPlacer() {
        return placer;
    }

    public Supplier<Double> getUpSpeed() {
        return MOVEMENT_SPEED;
    }
}
