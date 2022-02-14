package frc.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Commands.CloseTelescopic;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the climber winch which controls the height of the telescopic arms.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    private final Supplier<Double> downSpeed = rootNamespace.addConstantDouble("down speed", -0.25);
    private final Supplier<Double> hookedDownSpeed = rootNamespace.addConstantDouble("hooked down speed", -0.6);
    private final Supplier<Double> upSpeed = rootNamespace.addConstantDouble("up speed", 0.4);

    private static ClimberWinch instance;

    /**
     * Whether the hook is lashed onto the bar.
     */
    private final DigitalInput leftHookLimit, rightHookLimit;

    /**
     * Whether the arm reached its max height or minimum height.
     */
    private final DigitalInput hallEffect;

    /**
     * Which magnet the hall effect sensor is attached to.
     */
    private Level magnetLevel;

    private enum Level {UPPER, MIDDLE, LOWER;}

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(new CANSparkMax(RobotMap.CAN.CLIMBER_WINCH_SPARK_MAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.CLIMBER_WINCH_SPARK_MAX_2, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return instance;
    }

    private ClimberWinch(CANSparkMax leftWinch, CANSparkMax rightWinch) {
        super("climber winch", leftWinch, rightWinch);
        this.magnetLevel = Level.LOWER;
        this.hallEffect = new DigitalInput(RobotMap.DIO.CLIMBER_WINCH_HALL_EFFECT);
        this.leftHookLimit = new DigitalInput(RobotMap.DIO.CLIMBER_PLACER_LEFT_HOOK_LIMIT);
        this.rightHookLimit = new DigitalInput(RobotMap.DIO.CLIMBER_PLACER_RIGHT_HOOK_LIMIT);
    }

    @Override
    public boolean canMove(double speed) {
        return !(magnetLevel == Level.UPPER && speed > 0) && !(magnetLevel == Level.LOWER && speed < 0);
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    @Override
    protected void apply(double speed) {
        super.apply(speed);
        if (!hallEffect.get()) {
            if ((speed > 0 && magnetLevel == Level.MIDDLE) || magnetLevel == Level.UPPER)
                magnetLevel = Level.UPPER;
            if ((speed < 0 && magnetLevel == Level.MIDDLE) || magnetLevel == Level.LOWER)
                magnetLevel = Level.LOWER;
        } else
            magnetLevel = Level.MIDDLE;
    }

    public boolean isLeftHooked() {
        return leftHookLimit.get();
    }

    public boolean isRightHooked() {
        return rightHookLimit.get();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("Close Telescopic", new CloseTelescopic());
        rootNamespace.putData("Open Telescopic", new MoveGenericSubsystem(this, upSpeed));
        rootNamespace.putString("Status", () -> magnetLevel.name());
    }

    public Supplier<Double> getUpSpeed() {
        return upSpeed;
    }

    public Supplier<Double> getDownSpeed() {
        return downSpeed;
    }

    public Supplier<Double> getHookedDownSpeed() {
        return hookedDownSpeed;
    }
}
