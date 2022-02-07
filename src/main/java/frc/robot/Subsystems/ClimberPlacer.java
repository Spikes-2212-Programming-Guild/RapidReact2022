package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    private static ClimberPlacer rightInstance, leftInstance;

    private final Supplier<Double> DROP_SPEED = rootNamespace.addConstantDouble("drop speed", 0);

    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    private final WPI_TalonSRX placer;

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_RIGHT), "right",
                    RobotMap.DIO.PLACER_RIGHT_LIMIT_FRONT, RobotMap.DIO.PLACER_RIGHT_LIMIT_BACK);
        }
        return rightInstance;
    }

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_LEFT), "left",
                    RobotMap.DIO.PLACER_LEFT_LIMIT_FRONT, RobotMap.DIO.PLACER_LEFT_LIMIT_BACK);
        }
        return leftInstance;
    }

    private ClimberPlacer(WPI_TalonSRX placer, String side, int frontLimitPort, int backLimitPort) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", placer);
        this.placer = placer;
        this.frontLimit = new DigitalInput(frontLimitPort);
        this.backLimit = new DigitalInput(backLimitPort);
    }

    public WPI_TalonSRX getPlacer() {
        return placer;
    }

    public Supplier<Double> getDropSpeed() {
        return DROP_SPEED;
    }

    public boolean getFrontLimit() {
        return frontLimit.get();
    }

    public boolean getBackLimit() {
        return backLimit.get();
    }
}
