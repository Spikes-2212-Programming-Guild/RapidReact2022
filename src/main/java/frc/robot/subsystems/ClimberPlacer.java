package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import frc.robot.RobotMap;
import frc.robot.commands.MovePlacerToNextBar;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    public static final double MIN_SPEED = -0.6;
    public static final double MAX_SPEED = 0.6;

    private final Supplier<Double> STALL_CURRENT = rootNamespace.addConstantDouble("stall current", 10);

    public final Supplier<Double> RAISE_SPEED = rootNamespace.addConstantDouble("raise speed", 0.25);

    private final String side;
    private final WPI_TalonSRX talon;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer("left", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_LEFT));
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer("right", new WPI_TalonSRX(RobotMap.CAN.CLIMBER_PLACER_TALON_RIGHT));
        }
        return rightInstance;
    }

    private ClimberPlacer(String side, WPI_TalonSRX talon) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", talon);
        this.side = side;
        this.talon = talon;
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("drop " + side + " placer", new MovePlacerToNextBar(this));
        rootNamespace.putNumber(side + " stator current", talon::getStatorCurrent);
    }

    public boolean isStalling() {
        return Math.abs(talon.getStatorCurrent()) > STALL_CURRENT.get();
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        talon.setNeutralMode(neutralMode);
    }
}
