package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Commands.DropBothPlacers;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;

    private final Supplier<Double> stallCurrent = rootNamespace.addConstantDouble("stall current", 10);

    private final Supplier<Double> dropSpeed = rootNamespace.addConstantDouble("drop speed", 0.5);

    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    private final WPI_TalonSRX talon;

    private final Supplier<Boolean> hookLimitSupplier;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer("left", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_LEFT),
                    RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_BACK,
                    ClimberWinch.getInstance()::isLeftHooked);
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer("right", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_RIGHT),
                    RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_BACK,
                    ClimberWinch.getInstance()::isRightHooked);
        }
        return rightInstance;
    }

    private ClimberPlacer(String side, WPI_TalonSRX talon, int frontLimitPort, int backLimitPort, Supplier<Boolean> hookLimitSupplier) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", talon);
        this.talon = talon;
        this.frontLimit = new DigitalInput(frontLimitPort);
        this.backLimit = new DigitalInput(backLimitPort);
        this.hookLimitSupplier = hookLimitSupplier;
    }

    @Override
    public boolean canMove(double speed) {
        return !(frontLimit.get() && speed < 0) && !(backLimit.get() && speed > 0) &&
                !hookLimitSupplier.get();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("DropBothPlacers", new DropBothPlacers());
    }

    public boolean isStalling() {
        return talon.getStatorCurrent() > stallCurrent.get();
    }

    public WPI_TalonSRX getTalon() {
        return talon;
    }

    public Supplier<Double> getDropSpeed() {
        return dropSpeed;
    }

    public boolean getFrontLimit() {
        return frontLimit.get();
    }

    public boolean getBackLimit() {
        return backLimit.get();
    }
}
