package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.commands.DropBothPlacers;
import frc.robot.RobotMap;
import frc.robot.commands.DropPlacer;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    public static final double MIN_SPEED = -0.6;
    public static final double MAX_SPEED = 0.6;

    private final Supplier<Double> stallCurrent = rootNamespace.addConstantDouble("stall current", 10);

    public final Supplier<Double> DROP_SPEED = rootNamespace.addConstantDouble("drop speed", 0.5);

    private final String side;
    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    private final WPI_TalonSRX talon;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer("left", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_LEFT),
                    RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_BACK);
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer("right", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_RIGHT),
                    RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_BACK);
        }
        return rightInstance;
    }

    private ClimberPlacer(String side, WPI_TalonSRX talon, int frontLimitPort, int backLimitPort) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", talon);
        this.side = side;
        this.talon = talon;
        this.frontLimit = new DigitalInput(frontLimitPort);
        this.backLimit = new DigitalInput(backLimitPort);
    }

    @Override
    public boolean canMove(double speed) {
        return !(frontLimit.get() && speed < 0) && !(backLimit.get() && speed > 0);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("drop " + side + " placer", new DropPlacer(this));
        rootNamespace.putData("drop both placers", new DropBothPlacers());
    }

    public boolean isStalling() {
        return Math.abs(talon.getStatorCurrent()) > stallCurrent.get();
    }

    public WPI_TalonSRX getTalon() {
        return talon;
    }

    public boolean getFrontLimit() {
        return frontLimit.get();
    }

    public boolean getBackLimit() {
        return backLimit.get();
    }
}
