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

    private final Supplier<Double> dropSpeed = rootNamespace.addConstantDouble("drop speed", 0);

    private final DigitalInput frontLimit;
    private final DigitalInput backLimit;
    private final WPI_TalonSRX talon;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_LEFT), "left",
                    RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_LEFT_LIMIT_BACK);
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_RIGHT), "right",
                    RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_FRONT, RobotMap.DIO.CLIMBER_PLACER_RIGHT_LIMIT_BACK);
        }
        return rightInstance;
    }

    private ClimberPlacer(WPI_TalonSRX talon, String side, int frontLimitPort, int backLimitPort) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", talon);
        this.talon = talon;
        this.frontLimit = new DigitalInput(frontLimitPort);
        this.backLimit = new DigitalInput(backLimitPort);
    }

    @Override
    public boolean canMove(double speed) {
        return !(frontLimit.get() && speed < 0) && !(backLimit.get() && speed > 0) && !ClimberWinch.getInstance().isHooked();
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
