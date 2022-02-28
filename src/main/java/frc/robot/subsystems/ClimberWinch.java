package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the climber winch which controls the height of the telescopic arms.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    public static final double UP_SPEED = 0.25;
    public static final double DOWN_SPEED = -0.3;

    public final Supplier<Double> ENCODER_UP_POS = rootNamespace.addConstantDouble("encoder up position", 140);
    public final Supplier<Double> ENCODER_DOWN_TOLERANCE = rootNamespace.addConstantDouble("encoder down tolerance", 10);

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
        super(DOWN_SPEED, UP_SPEED, "climber winch", leftWinch, rightWinch);
        sparkMax = rightWinch;
    }

    @Override
    public boolean canMove(double speed) {
        return (speed > 0 && sparkMax.getEncoder().getPosition() < ENCODER_UP_POS.get()) ||
                (speed < 0 && sparkMax.getEncoder().getPosition() > ENCODER_DOWN_TOLERANCE.get());
    }

    public void resetEncoder() {
        sparkMax.getEncoder().setPosition(0);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("close telescopic", new MoveGenericSubsystem(this, DOWN_SPEED));
        rootNamespace.putData("open telescopic", new MoveGenericSubsystem(this, UP_SPEED));
        rootNamespace.putData("reset encoder", new InstantCommand(this::resetEncoder));
        rootNamespace.putNumber("encoder", sparkMax.getEncoder()::getPosition);
    }
}
