package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Controls the position of the {@code IntakeRoller}.
 *
 * @author Ofri Rosenbaum
 * @see MotoredGenericSubsystem
 */
public class IntakePlacer extends MotoredGenericSubsystem {

    /**
     * The speed in which this subsystem should move up.
     */
    public static final double UP_SPEED = 0.5;

    /**
     * The speed in which this subsystem should move down.
     */
    public static final double DOWN_SPEED = -0.3;

    private static IntakePlacer instance;

    /**
     * The upper limit of the subsystem. When it is pressed, the intake system is vertical.
     */
    private DigitalInput upperLimit;

    /**
     * The lower limit of the subsystem. When it is pressed, the intake system is horizontal.
     */
    private DigitalInput lowerLimit;

    public static IntakePlacer getInstance() {
        if (instance == null) {
            instance = new IntakePlacer();
        }
        return instance;
    }

    private IntakePlacer() {
        super(DOWN_SPEED, UP_SPEED, "intake placer", new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER));
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_LOWER_LIMIT);
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
        rootNamespace.putData("move intake down", new MoveGenericSubsystem(this, DOWN_SPEED));
        rootNamespace.putData("move intake up", new MoveGenericSubsystem(this, UP_SPEED));
    }

    public boolean isUp() {
        return upperLimit.get();
    }

    public boolean isDown() {
        return lowerLimit.get();
    }
}