package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
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

    private static final RootNamespace rootNamespace = new RootNamespace("drivetrain");
    private static final Namespace encoderNamespace = rootNamespace.addChild("encoders");
    private static final Namespace encodersPIDNamespace = encoderNamespace.addChild("encoders pid");
    private static final Namespace gyroNamespace = rootNamespace.addChild("gyro");
    private static final Namespace gyroPIDNamespace = gyroNamespace.addChild("gyro pid");
    private static final Namespace cameraPIDNamespace = rootNamespace.addChild("camera pid");
    private static final Namespace FeedForwardNamespace = rootNamespace.addChild("feed forward");

    /**
     * One side of the robot is faster than the other. To solve this we slow down one of the sides.
     */
    private static final Supplier<Double> rightCorrection = rootNamespace.addConstantDouble("right correction", 1);
    private static final Supplier<Double> leftCorrection = rootNamespace.addConstantDouble("left correction", 1);

    //    private final PigeonWrapper pigeon;
    private final Encoder leftEncoder, rightEncoder;

    private final Supplier<Double> kPGyro = gyroPIDNamespace.addConstantDouble("gyro kP", 0);
    private final Supplier<Double> kIGyro = gyroPIDNamespace.addConstantDouble("gyro kI", 0);
    private final Supplier<Double> kDGyro = gyroPIDNamespace.addConstantDouble("gyro kD", 0);
    private final Supplier<Double> ToleranceGyro = gyroPIDNamespace.addConstantDouble("gyro tolerance", 0);
    private final Supplier<Double> WaitTimeGyro = gyroPIDNamespace.addConstantDouble("gyro wait time", 0);
    private final PIDSettings pidSettingsGyro;

    private final Supplier<Double> kPEncoders = encodersPIDNamespace.addConstantDouble("drivetrain kP", 0);
    private final Supplier<Double> kIEncoders = encodersPIDNamespace.addConstantDouble("drivetrain kI", 0);
    private final Supplier<Double> kDEncoders = encodersPIDNamespace.addConstantDouble("drivetrain kD", 0);
    private final Supplier<Double> ToleranceEncoders = encodersPIDNamespace.addConstantDouble("drivetrain tolerance", 0);
    private final Supplier<Double> WaitTimeEncoders = encodersPIDNamespace.addConstantDouble("drivetrain wait time", 0);
    private final PIDSettings pidSettingsEncoders;

    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("camera kP", 0);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("camera kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("camera kD", 0);
    private final Supplier<Double> ToleranceCamera = cameraPIDNamespace.addConstantDouble("camera tolerance", 0);
    private final Supplier<Double> WaitTimeCamera = cameraPIDNamespace.addConstantDouble("camera wait time", 0);
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
                    pigeonTalon, // @TODO: If you change the pigeon talon change this as well
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_2)
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
        super(leftMotors, rightMotors);
//        this.pigeon = new PigeonWrapper(pigeonTalon);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.pidSettingsGyro = new PIDSettings(this.kPGyro, this.kIGyro, this.kDGyro, this.ToleranceGyro,
                this.WaitTimeGyro);
        this.pidSettingsEncoders = new PIDSettings(this.kPEncoders, this.kIEncoders, this.kDEncoders,
                this.ToleranceEncoders, this.WaitTimeEncoders);
        this.pidSettingsCamera = new PIDSettings(this.kPCamera, this.kICamera, this.kDCamera,
                this.ToleranceCamera, this.WaitTimeCamera);
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
//        double yaw = pigeon.getYaw() % 360;
//        if (yaw > 180) yaw -= 360;
//        if (yaw < -180) yaw += 360;
//        return yaw;
        return 0;
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
//        gyroNamespace.putData("reset pigeon", new InstantCommand(pigeon::reset) {
//            @Override
//            public boolean runsWhenDisabled() {
//                return true;
//            }
//        });
    }
}
