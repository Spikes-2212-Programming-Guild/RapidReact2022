package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;
import frc.robot.utils.BustedMotorControllerGroup;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    // The wheel moves 20.32 * PI (it's perimeter) each 4096 ticks.
    private static final double DISTANCE_PER_PULSE = 20.32 * Math.PI / 4096;

    // One side of the robot is faster than the other. To solve this we slow down one of the sides.
    private static Supplier<Double> rightCorrection;

    private static Drivetrain instance;

    public RootNamespace rootNamespace;
    private Namespace encoderNamespace;
    private Namespace leftColorSensorNamespace;
    private Namespace rightColorSensorNamespace;
    private Namespace PIDNamespace;
    private Namespace FeedForwardNamespace;

    private final PigeonWrapper pigeon;
    private final Encoder leftEncoder, rightEncoder;
    private final ColorSensorV3 rightColorSensor, leftColorSensor;
    private final WPI_TalonSRX leftTalon1, leftTalon2, rightTalon1, rightTalon2;

    private PIDSettings pidSettings;
    private Supplier<Double> kP, kI, kD;
    private FeedForwardSettings ffSettings;
    private Supplier<Double> kV, kS, kA;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain(
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_1),
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_2),
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_1),
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_2)
            );
        }
        return instance;
    }

    private Drivetrain(WPI_TalonSRX leftTalon1, WPI_TalonSRX leftTalon2, WPI_TalonSRX rightTalon1, WPI_TalonSRX rightTalon2) {
        super(new MotorControllerGroup(leftTalon1, leftTalon2), new BustedMotorControllerGroup(rightCorrection, rightTalon1, rightTalon2));

        this.pigeon = new PigeonWrapper(RobotMap.CAN.DRIVETRAIN_PIGEON);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.leftTalon1 = leftTalon1;
        this.leftTalon2 = leftTalon2;
        this.rightTalon1 = rightTalon1;
        this.rightTalon2 = rightTalon2;
        this.rightColorSensor = new ColorSensorV3(I2C.Port.kOnboard);
        this.leftColorSensor = new ColorSensorV3(I2C.Port.kMXP);
        this.pidSettings = new PIDSettings(kP, kI, kD);
        this.ffSettings = new FeedForwardSettings(kS, kV, kA);
    }

    private void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void periodic() {
        rootNamespace.update();
    }

    public Color getLeftColor() {
        return leftColorSensor.getColor();
    }

    public Color getRightColor() {
        return rightColorSensor.getColor();
    }

    public double getYaw() {
        return pigeon.getYaw();
    }

    public double getRightTalon1Current() {
        return rightTalon1.getStatorCurrent();
    }

    public double getLeftTalon1Current() {
        return leftTalon1.getStatorCurrent();
    }

    /**
     * initialise namespaces and add sensor data to dashboard
     */
    public void configureDashboard() {
        rootNamespace = new RootNamespace("drivetrain");
        leftColorSensorNamespace = rootNamespace.addChild("left color sensor");
        rightColorSensorNamespace = rootNamespace.addChild("right color sensor");
        encoderNamespace = rootNamespace.addChild("encoders");

        rightCorrection = rootNamespace.addConstantDouble("right correction", 0);

        encoderNamespace.putNumber("left ticks", this.leftEncoder::get);
        encoderNamespace.putNumber("right ticks", this.rightEncoder::get);
        encoderNamespace.putNumber("left distance", this.leftEncoder::getDistance);
        encoderNamespace.putNumber("right distance", this.rightEncoder::getDistance);
        encoderNamespace.putData("reset encoders", new InstantCommand(this::resetEncoders) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });

        leftColorSensorNamespace.putNumber("left color sensor red", () -> this.getLeftColor().red);
        leftColorSensorNamespace.putNumber("left color sensor blue", () -> this.getLeftColor().blue);
        leftColorSensorNamespace.putNumber("left color sensor green", () -> this.getLeftColor().green);

        rightColorSensorNamespace.putNumber("right color sensor red", () -> this.getRightColor().red);
        rightColorSensorNamespace.putNumber("right color sensor blue", () -> this.getRightColor().blue);
        rightColorSensorNamespace.putNumber("right color sensor green", () -> this.getRightColor().green);

        kP = PIDNamespace.addConstantDouble("kP", 0);
        kI = PIDNamespace.addConstantDouble("kI", 0);
        kD = PIDNamespace.addConstantDouble("kD", 0);

        kV = FeedForwardNamespace.addConstantDouble("kV", 0);
        kS = FeedForwardNamespace.addConstantDouble("kS", 0);
        kA = FeedForwardNamespace.addConstantDouble("kA", 0);
    }
}
