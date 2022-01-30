package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Controls the position of the climber's.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;

    private static ClimberPlacer instance;

    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    public final DigitalInput hookLimit;
    private static WPI_TalonSRX leftPlacer;
    private static WPI_TalonSRX rightPlacer;

    public static ClimberPlacer getInstance() {
        if (instance == null) {
            leftPlacer = new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_LEFT);
            rightPlacer = new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_RIGHT);
            instance = new ClimberPlacer(leftPlacer, rightPlacer);
        }
        return instance;
    }

    public ClimberPlacer(WPI_TalonSRX leftPlacer, WPI_TalonSRX rightPlacer) {
        super(MIN_SPEED, MAX_SPEED, "climber placer", leftPlacer, rightPlacer);
        this.frontLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_FRONT);
        this.backLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_BACK);
        this.hookLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_HOOK);
    }

    @Override
    public boolean canMove(double speed) {
        return !(hookLimit.get() || speed > 0 && frontLimit.get() || speed < 0 && backLimit.get());
    }

    public WPI_TalonSRX getLeftTalon() {
        return leftPlacer;
    }

    public WPI_TalonSRX getRightTalon() {
        return rightPlacer;
    }
}
