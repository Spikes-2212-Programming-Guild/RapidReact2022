package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Drivetrain extends OdometryDrivetrain2 {

    private static final RootNamespace rootNamespace = new RootNamespace("Drivetrain");

    private final Supplier<Double> kP = rootNamespace.addConstantDouble("kP", 0);

    private final Supplier<Double> kI = rootNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = rootNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> TOLERANCE = rootNamespace.addConstantDouble("TOLERANCE", 0);
    private final Supplier<Double> WAIT_TIME = rootNamespace.addConstantDouble("WAIT_TIME", 0);

    private final Supplier<Double> kS = rootNamespace.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = rootNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kA = rootNamespace.addConstantDouble("kA", 0);
    private final Supplier<Double> kG = rootNamespace.addConstantDouble("kG", 0);

    private final PIDSettings pidSettings;
    private final FeedForwardSettings ffSettings;

    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    private final PigeonWrapper gyro;
    private final DifferentialDriveOdometry odometry;
    private final Field2d field2d;

    private static Drivetrain instance;

    private Drivetrain(MotorControllerGroup leftSCG, MotorControllerGroup rightSCG,
                       Encoder leftEncoder, Encoder rightEncoder, PigeonWrapper gyro) {
        super(leftSCG, rightSCG);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.gyro = gyro;

        this.pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);
        this.ffSettings = new FeedForwardSettings(kS, kV, kA, kG);

        this.leftEncoder.reset();
        this.rightEncoder.reset();
        this.gyro.reset();

        this.odometry = new DifferentialDriveOdometry(rotFromAngle());

        this.field2d = new Field2d();
        rootNamespace.putData("Field 2d", field2d);
        rootNamespace.putNumber("gyro value", gyro::getYaw);
        rootNamespace.putNumber("left encoder", leftEncoder::getDistance);
        rootNamespace.putNumber("right encoder", rightEncoder::getDistance);
        rootNamespace.putNumber("x", () -> odometry.getPoseMeters().getX());
        rootNamespace.putNumber("y", () -> odometry.getPoseMeters().getY());

    }

    @Override
    public void periodic() {
        odometry.update(rotFromAngle(), leftEncoder.getDistance(), rightEncoder.getDistance());
        field2d.setRobotPose(odometry.getPoseMeters());
        rootNamespace.update();

    }

    @Override
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    @Override
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
    }

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain(
                    new MotorControllerGroup(
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_1),
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_2)
                    ),
                    new MotorControllerGroup(
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_1),
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_2)
                    ),
                    new Encoder(
                            RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_CHANNEL_A,
                            RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_CHANNEL_A
                    ),
                    new Encoder(
                            RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_CHANNEL_A,
                            RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_CHANNEL_B
                    ),
                    new PigeonWrapper(RobotMap.CAN.DRIVETRAIN_PIGEON)
            );

        }

        return instance;
    }

    @Override
    public PIDSettings getPIDSettings() {
        return this.pidSettings;
    }

    @Override
    public FeedForwardSettings getFFSettings() {
        return this.ffSettings;
    }

    @Override
    public void resetOdometry(Pose2d pose) {
        leftEncoder.reset();
        rightEncoder.reset();
        gyro.reset();
        odometry.resetPosition(pose, rotFromAngle());
    }

    public Rotation2d rotFromAngle() {
        return Rotation2d.fromDegrees(gyro.getYaw());
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftController.setVoltage(leftVolts);
        rightController.setVoltage(rightVolts);
    }

    public double getWidth() {
        return 0.65;
    }
}
