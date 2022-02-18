package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;
import frc.robot.utils.BustedMotorControllerGroup;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    /**
     * The wheel moves 15.24 * PI (it's perimeter) each 360 ticks (in meters).
     */
    private static final double DISTANCE_PER_PULSE = 15.24 * Math.PI / 360.0 / 100;

    private static Drivetrain instance;

    private final Namespace encoderNamespace = rootNamespace.addChild("encoders");
    private final Namespace encodersPIDNamespace = encoderNamespace.addChild("encoders pid");
    private final Namespace gyroNamespace = rootNamespace.addChild("gyro");
    private final Namespace gyroPIDNamespace = gyroNamespace.addChild("gyro pid");
    private final Namespace cameraPIDNamespace = rootNamespace.addChild("camera pid");
    private final Namespace FeedForwardNamespace = rootNamespace.addChild("feed forward");

    /**
     * One side of the robot is faster than the other. To solve this we slow down one of the sides.
     */
    private static final RootNamespace corrections = new RootNamespace("drivetrain correction");
    private static final Supplier<Double> rightCorrection = corrections.addConstantDouble("right correction", 1);
    private static final Supplier<Double> leftCorrection = corrections.addConstantDouble("left correction", 1);

    private final PigeonWrapper pigeon;
    private final Encoder leftEncoder, rightEncoder;

    private final Supplier<Double> kPGyro = gyroPIDNamespace.addConstantDouble("kP", 0.017);
    private final Supplier<Double> kIGyro = gyroPIDNamespace.addConstantDouble("kI", 0.0028);
    private final Supplier<Double> kDGyro = gyroPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceGyro = gyroPIDNamespace.addConstantDouble("tolerance", 5);
    private final Supplier<Double> waitTimeGyro = gyroPIDNamespace.addConstantDouble("wait time", 0.5);
    private final PIDSettings pidSettingsGyro;

    private final Supplier<Double> kPEncoders = encodersPIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kIEncoders = encodersPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDEncoders = encodersPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceEncoders = encodersPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeEncoders = encodersPIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettingsEncoders;

    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceCamera = cameraPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeCamera = cameraPIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettingsCamera;

    private final Supplier<Double> kS = FeedForwardNamespace.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = FeedForwardNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kA = FeedForwardNamespace.addConstantDouble("kA", 0);
    private final FeedForwardSettings ffSettings;

    public static Drivetrain getInstance() {
        if (instance == null) {
            WPI_TalonSRX pigeonTalon = new WPI_TalonSRX(RobotMap.CAN.PIGEON_TALON);
            instance = new Drivetrain(new BustedMotorControllerGroup(
                    leftCorrection,
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_1),
                    pigeonTalon // @TODO: If you change the pigeon talon change this as well
            ),
                    new BustedMotorControllerGroup(
                            rightCorrection,
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_1),
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_2)
                    ),
                    pigeonTalon
            );
        }
        return instance;
    }

    private Drivetrain(MotorControllerGroup leftMotors, BustedMotorControllerGroup rightMotors, WPI_TalonSRX pigeonTalon) {
        super("drivetrain", leftMotors, rightMotors);
        this.pigeon = new PigeonWrapper(pigeonTalon);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.pidSettingsGyro = new PIDSettings(this.kPGyro, this.kIGyro, this.kDGyro, this.toleranceGyro,
                this.waitTimeGyro);
        this.pidSettingsEncoders = new PIDSettings(this.kPEncoders, this.kIEncoders, this.kDEncoders,
                this.toleranceEncoders, this.waitTimeEncoders);
        this.pidSettingsCamera = new PIDSettings(this.kPCamera, this.kICamera, this.kDCamera,
                this.toleranceCamera, this.waitTimeCamera);
        this.ffSettings = new FeedForwardSettings(this.kS, this.kV, this.kA);
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void periodic() {
        rootNamespace.update();
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public double getRightTicks() {
        return rightEncoder.get();
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getLeftTicks() {
        return leftEncoder.get();
    }

    public PIDSettings getGyroPIDSettings() {
        return pidSettingsGyro;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }

    /**
     * Initializes namespaces and adds sensor data to dashboard.
     */
    public void configureDashboard() {
        encoderNamespace.putNumber("left ticks", leftEncoder::get);
        encoderNamespace.putNumber("right ticks", rightEncoder::get);
        encoderNamespace.putNumber("left distance", leftEncoder::getDistance);
        encoderNamespace.putNumber("right distance", rightEncoder::getDistance);
        encoderNamespace.putData("reset encoders", new InstantCommand(this::resetEncoders) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
        gyroNamespace.putNumber("yaw", this::getYaw);
        gyroNamespace.putData(" pigeon", new InstantCommand(pigeon::reset) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
    }
}
