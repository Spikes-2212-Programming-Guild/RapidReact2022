package frc.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the climber winch which controls the height of the telescopic arms.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    private Supplier<Double> DOWN_SPEED = rootNamespace.addConstantDouble("down speed", -0.25);
    private Supplier<Double> HOOKED_DOWN_SPEED = rootNamespace.addConstantDouble("hooked down speed", -0.6);
    private Supplier<Double> UP_SPEED = rootNamespace.addConstantDouble("up speed", 0.25);

    private static ClimberWinch instance;

    /**
     * Whether the hook is lashed onto the bar.
     */
    private final DigitalInput hookLimit;

    /**
     * Whether the arm reached its max height or minimum height.
     */
    private final DigitalInput hallEffect;

    /**
     * Which magnet the hall effect sensor is attached to.
     */
    private Level magnetLevel;

    private enum Level {UPPER, LOWER, MIDDLE}

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(new CANSparkMax(RobotMap.CAN.WINCH_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.WINCH_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return instance;
    }

    private ClimberWinch(CANSparkMax leftWinch, CANSparkMax rightWinch) {
        super("climber winch", leftWinch, rightWinch);
        this.magnetLevel = Level.LOWER;
        this.hallEffect = new DigitalInput(RobotMap.DIO.WINCH_HALL_EFFECT);
        this.hookLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_HOOK);
    }

    @Override
    public boolean canMove(double speed) {
        return !(magnetLevel == Level.UPPER && speed > 0 || magnetLevel == Level.LOWER && speed < 0);
    }

    @Override
    public void periodic() {
        super.periodic();
        double speed = getSpeed();
        if (hallEffect.get()) {
            if (speed > 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.UPPER;
            if (speed < 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.LOWER;
        } else
            magnetLevel = Level.MIDDLE;
    }

    public boolean isHooked() {
        return false;
    }

    public Supplier<Double> getUpSpeed() {
        return null;
    }

    public Supplier<Double> getDownSpeed() {
        return DOWN_SPEED;
    }

    public Supplier<Double> getHookedDownSpeed() {
        return HOOKED_DOWN_SPEED;
    }
}
