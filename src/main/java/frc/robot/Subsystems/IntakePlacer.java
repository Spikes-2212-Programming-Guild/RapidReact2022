package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the {@code IntakeRoller}.
 *
 * @author Ofri Rosenbaum
 * @see MotoredGenericSubsystem
 */
public class IntakePlacer extends MotoredGenericSubsystem {

    public static final double POTENTIOMETER_DOWN_SETPOINT = 90;
    public static final double POTENTIOMETER_UP_SETPOINT = 0;
    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.3;

    /**
     * The potentiometer's starting value.
     */
    private static final int POTENTIOMETER_STARTING_POINT = 0;

    /**
     * The potentiometer's full range of motion in degrees.
     */
    private static final int POTENTIOMETER_RANGE_VALUE = 90;

    private Namespace PID = rootNamespace.addChild("PID");
    private Supplier<Double> waitTime = PID.addConstantDouble("wait time", 0);
    private Supplier<Double> tolerance = PID.addConstantDouble("tolerance", 0);
    public PIDSettings pidSettings = new PIDSettings(() -> 0.0, tolerance, waitTime);
    private Supplier<Double> kS = PID.addConstantDouble("kS", 0);
    private Supplier<Double> kV = PID.addConstantDouble("kV", 0);
    public FeedForwardSettings feedForwardSettings = new FeedForwardSettings(kS, kV, () -> 0.0);

    private static IntakePlacer instance;

    /**
     * The upper limit of the subsystem. When it is pressed, the intake system is vertical.
     */
    private DigitalInput upperLimit;

    /**
     * The lower limit of the subsystem. When it is pressed, the intake system is horizontal.
     */
    private DigitalInput lowerLimit;

    /**
     * The subsystem's potentiometer. Used to measure in what angle the subsystem is.
     */
    private AnalogPotentiometer potentiometer;

    public static IntakePlacer getInstance() {
        if (instance == null) {
            instance = new IntakePlacer();
        }
        return instance;
    }

    private IntakePlacer() {
        super(MIN_SPEED, MAX_SPEED, "intake placer", new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER));
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_LOWER_LIMIT);
        potentiometer = new AnalogPotentiometer(RobotMap.ANALOG_IN.INTAKE_POTENTIOMETER, POTENTIOMETER_RANGE_VALUE,
                POTENTIOMETER_STARTING_POINT);
    }

    /**
     * Whether the intake subsystem can move up/down.
     * <p>For positive values it checks if the subsystem can move up, while for negative values
     * it checks if it can move down.</p>
     *
     * @return whether the intake subsystem can move up/down.
     */
    @Override
    public boolean canMove(double speed) {
        if (speed > 0) {
            return !upperLimit.get();
        }
        if (speed < 0) {
            return !lowerLimit.get();
        }
        return false;
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("move intake down", new MoveGenericSubsystem(this, MIN_SPEED));
        rootNamespace.putData("move intake up", new MoveGenericSubsystem(this, MAX_SPEED));
    }

    public double getPotentiometerAngle() {
        return potentiometer.get();
    }

    public boolean isUp() {
        return upperLimit.get();
    }

    public boolean isDown() {
        return lowerLimit.get();
    }
}
