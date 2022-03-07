package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the telescopic arms that are responsible for climbing.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    public static final double UP_SPEED = 0.25;
    public static final double DOWN_SPEED = -0.3;

    public final Supplier<Double> ENCODER_UP_LIMIT =
            rootNamespace.addConstantDouble("encoder up limit", 120);
    public final Supplier<Double> ENCODER_DOWN_LIMIT =
            rootNamespace.addConstantDouble("encoder down limit", 1);

    private static ClimberWinch instance;

    private final RelativeEncoder encoder;

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
        encoder = rightWinch.getEncoder();
    }

    @Override
    public boolean canMove(double speed) {
        return (speed > 0 && encoder.getPosition() < ENCODER_UP_LIMIT.get()) ||
                (speed < 0 && encoder.getPosition() > ENCODER_DOWN_LIMIT.get());
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("close telescopic", new MoveGenericSubsystem(this, DOWN_SPEED));
        rootNamespace.putData("open telescopic", new MoveGenericSubsystem(this, UP_SPEED));
        rootNamespace.putData("reset encoder", new InstantCommand(this::resetEncoder));
        rootNamespace.putNumber("encoder position", encoder::getPosition);
    }
}