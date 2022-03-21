package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the climber.
 */
public class ClimberPlacer extends MotoredGenericSubsystem {

    private static final RootNamespace root = new RootNamespace("climber placers");

    public static final double MIN_SPEED = -0.05;
    public static final double MAX_SPEED = 0.05;

    public final Supplier<Double> RAISE_SPEED = root.addConstantDouble("raise speed", 0.25);

    //@todo calibrate
    private static final Namespace encoders = root.addChild("encoder");
    public static final Supplier<Double> ENCODER_DOWN_POSITION =
            encoders.addConstantDouble("encoder down position", 0);
    public static final Supplier<Double> ENCODER_DOWN_TOLERANCE =
            encoders.addConstantDouble("encoder down tolerance", 0);
    public static final Supplier<Double> ENCODER_TO_NEXT_BAR_POSITION =
            encoders.addConstantDouble("encoder to next bar position", 0);
    public static final Supplier<Double> ENCODER_TO_NEXT_BAR_TOLERANCE =
            encoders.addConstantDouble("encoder to next bar tolerance", 0);

    //@todo calibrate
    private static final Namespace pidNamespace = root.addChild("pid");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private static final Supplier<Double> TOLERANCE = pidNamespace.addConstantDouble("tolerance", 0);
    private static final Supplier<Double> WAIT_TIME = pidNamespace.addConstantDouble("wait time", 1);
    public static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);

    private final String side;
    private final CANSparkMax sparkMax;
    private final RelativeEncoder encoder;
    private final DigitalInput limit;

    private static ClimberPlacer leftInstance, rightInstance;

    public static ClimberPlacer getLeftInstance() {
        if (leftInstance == null) {
            leftInstance = new ClimberPlacer("left", new CANSparkMax(RobotMap.CAN.CLIMBER_PLACER_SPARK_MAX_LEFT,
                    CANSparkMaxLowLevel.MotorType.kBrushless), new DigitalInput(RobotMap.DIO.CLIMBER_PLACER_LIMIT_LEFT));
        }
        return leftInstance;
    }

    public static ClimberPlacer getRightInstance() {
        if (rightInstance == null) {
            rightInstance = new ClimberPlacer("right", new CANSparkMax(RobotMap.CAN.CLIMBER_PLACER_SPARK_MAX_RIGHT,
                    CANSparkMaxLowLevel.MotorType.kBrushless), new DigitalInput(RobotMap.DIO.CLIMBER_PLACER_LIMIT_RIGHT));
        }
        return rightInstance;
    }

    private ClimberPlacer(String side, CANSparkMax sparkMax, DigitalInput limit) {
        super(MIN_SPEED, MAX_SPEED, side + " climber placer", sparkMax);
        this.side = side;
        this.sparkMax = sparkMax;
        this.encoder = sparkMax.getEncoder();
        this.limit = limit;
    }

    public boolean hasHitNextBar() {
        return Math.abs(ENCODER_TO_NEXT_BAR_POSITION.get() - encoder.getPosition()) <=
                ENCODER_TO_NEXT_BAR_TOLERANCE.get();
    }

    public boolean isDown() {
        return Math.abs(ENCODER_DOWN_POSITION.get() - encoder.getPosition()) <= ENCODER_DOWN_TOLERANCE.get();
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    public void resetEncoders() {
        encoder.setPosition(0);
    }

    public void setIdleMode(IdleMode idleMode) {
        sparkMax.setIdleMode(idleMode);
    }

    public boolean getLimit() {
        return limit.get();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("move " + side + " placer forever", new MoveGenericSubsystem(this, MAX_SPEED) {
            @Override
            public boolean isFinished() {
                return false;
            }
        });
        rootNamespace.putData("move " + side + " minus placer forever", new MoveGenericSubsystem(this, -MAX_SPEED) {
            @Override
            public boolean isFinished() {
                return false;
            }
        });

        rootNamespace.putNumber("encoder position " + side, this::getEncoderPosition);
    }
}
