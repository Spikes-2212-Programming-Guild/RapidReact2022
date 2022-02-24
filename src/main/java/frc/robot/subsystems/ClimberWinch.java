package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the climber winch which controls the height of the telescopic arms.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    public final Supplier<Double> DOWN_SPEED = rootNamespace.addConstantDouble("down speed", -0.5);
    public final Supplier<Double> UP_SPEED = rootNamespace.addConstantDouble("up speed", 0.25);
    public final Supplier<Double> ENCODER_UP_POS = rootNamespace.addConstantDouble("encoder up position", 360 * 5);

    private static ClimberWinch instance;

    private final CANSparkMax sparkMax;

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(new CANSparkMax
                    (RobotMap.CAN.CLIMBER_WINCH_SPARK_MAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.CLIMBER_WINCH_SPARK_MAX_2, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return instance;
    }

    private ClimberWinch(CANSparkMax leftWinch, CANSparkMax rightWinch) {
        super("climber winch", leftWinch, rightWinch);
        sparkMax = rightWinch;
    }

    @Override
    public boolean canMove(double speed) {
        return (sparkMax.getEncoder().getPosition() < ENCODER_UP_POS.get() && speed > 0) ||
                (sparkMax.getEncoder().getPosition() > 10 && speed < 0);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("Close Telescopic", new MoveGenericSubsystem(this, DOWN_SPEED));
        rootNamespace.putData("Open Telescopic", new MoveGenericSubsystem(this, UP_SPEED));
    }
}
