package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Controls the position of the {@code IntakeRoller}.
 *
 * @see MotoredGenericSubsystem
 */
public class IntakePlacer extends MotoredGenericSubsystem {

    public static final double MAX_SPEED = 0.5;
    public static final double MIN_SPEED = -0.3;
    public static final double IDLE_SPEED = 0.2;

    private static IntakePlacer instance;

    /**
     * The upper limit of the subsystem. When it is pressed, the intake system is vertical.
     */
    private final DigitalInput upperLimit;
    private boolean shouldBeUp;

    /**
     * The lower limit of the subsystem. When it is pressed, the intake system is horizontal.
     */
    private final DigitalInput lowerLimit;

    public static IntakePlacer getInstance() {
        if (instance == null) {
            instance = new IntakePlacer();
        }
        return instance;
    }

    private IntakePlacer() {
        super(MIN_SPEED, MAX_SPEED, "intake placer", new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER_VICTOR));
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_LOWER_LIMIT);
        this.shouldBeUp = true;
    }

    @Override
    protected void apply(double speed) {
        shouldBeUp = speed != 0 ? speed > 0 : shouldBeUp;
        super.apply(speed);
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
        return !(isDown() && speed < 0) && !(isUp() && speed > 0);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("move intake down", new MoveGenericSubsystem(this, MIN_SPEED));
        rootNamespace.putData("move intake up", new MoveGenericSubsystem(this, MAX_SPEED));
    }

    public boolean getShouldBeUp() {
        return shouldBeUp;
    }

    public boolean isUp() {
        return upperLimit.get();
    }

    public boolean isDown() {
        return lowerLimit.get();
    }
}
