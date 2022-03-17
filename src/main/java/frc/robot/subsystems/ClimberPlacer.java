package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import frc.robot.RobotMap;
import frc.robot.commands.climbing.MovePlacerToNextBar;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    public static final double MIN_SPEED = -0.6;
    public static final double MAX_SPEED = 0.6;

    public final Supplier<Double> ENCODER_DOWN_POSITION =
            rootNamespace.addConstantDouble("encoder down position", 0);
    public final Supplier<Double> ENCODER_DOWN_TOLERANCE =
            rootNamespace.addConstantDouble("encoder down tolerance", 0);
    public final Supplier<Double> ENCODER_TO_NEXT_BAR_POSITION =
            rootNamespace.addConstantDouble("encoder to next bar position", 0);
    public final Supplier<Double> ENCODER_TO_NEXT_BAR_TOLERANCE =
            rootNamespace.addConstantDouble("encoder to next bar tolerance", 0);

    public final Supplier<Double> RAISE_SPEED = rootNamespace.addConstantDouble("raise speed", 0.25);

    private final String side;
    private final CANSparkMax sparkMax;
    private final RelativeEncoder encoder;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer("left", new CANSparkMax(RobotMap.CAN.CLIMBER_PLACER_SPARK_MAX_LEFT,
                    CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer("right", new CANSparkMax(RobotMap.CAN.CLIMBER_PLACER_SPARK_MAX_RIGHT,
                    CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return rightInstance;
    }

    private ClimberPlacer(String side, CANSparkMax sparkMax) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", sparkMax);
        this.side = side;
        this.sparkMax = sparkMax;
        this.encoder = sparkMax.getEncoder();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("drop " + side + " placer", new MovePlacerToNextBar(this));
    }

    public boolean hasHitNextBar() {
        return Math.abs(ENCODER_TO_NEXT_BAR_POSITION.get() - encoder.getPosition()) <=
                ENCODER_TO_NEXT_BAR_TOLERANCE.get();
    }

    public boolean isDown() {
        return Math.abs(ENCODER_DOWN_POSITION.get() - encoder.getPosition()) <= ENCODER_DOWN_TOLERANCE.get();
    }

    public void setIdleMode(IdleMode idleMode) {
        sparkMax.setIdleMode(idleMode);
    }
}
