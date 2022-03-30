package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.command.drivetrains.commands.DriveTank;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.BustedMotorControllerGroup;
import com.spikes2212.util.Limelight;
import com.spikes2212.util.PigeonWrapper;
import com.spikes2212.util.TalonEncoder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

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
    private final Namespace limelightPIDNamespace = rootNamespace.addChild("limelight pid");
    private final Namespace FeedForwardNamespace = rootNamespace.addChild("feed forward");

    /**
     * One side of the robot is faster than the other. To solve this we slow down one of the sides.
     */
    private static final RootNamespace corrections = new RootNamespace("drivetrain correction");
    private static final Supplier<Double> rightCorrection = corrections.addConstantDouble("right correction", 0.95);
    private static final Supplier<Double> leftCorrection = corrections.addConstantDouble("left correction", 1);

    private final PigeonWrapper pigeon;
    private final WPI_TalonSRX leftTalon, rightTalon;
    private final TalonEncoder leftEncoder, rightEncoder;
    private final Limelight limelight;
    private final DifferentialDriveOdometry odometry;
    private final Field2d field2d;

    private final Supplier<Double> kPGyro = gyroPIDNamespace.addConstantDouble("kP", 0.017);
    private final Supplier<Double> kIGyro = gyroPIDNamespace.addConstantDouble("kI", 0.0028);
    private final Supplier<Double> kDGyro = gyroPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceGyro = gyroPIDNamespace.addConstantDouble("tolerance", 5);
    private final Supplier<Double> waitTimeGyro = gyroPIDNamespace.addConstantDouble("wait time", 0.1);
    private final PIDSettings pidSettingsGyro;

    private final Supplier<Double> kPEncoders = encodersPIDNamespace.addConstantDouble("kP", 0.1);
    private final Supplier<Double> kIEncoders = encodersPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDEncoders = encodersPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceEncoders = encodersPIDNamespace.addConstantDouble("tolerance", 0.25);
    private final Supplier<Double> waitTimeEncoders = encodersPIDNamespace.addConstantDouble("wait time", 1);
    private final PIDSettings pidSettingsEncoders;

    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("kP", 0.0065);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceCamera = cameraPIDNamespace.addConstantDouble("tolerance", 2);
    private final Supplier<Double> waitTimeCamera = cameraPIDNamespace.addConstantDouble("wait time", 999);
    private final PIDSettings pidSettingsCamera;

    private final Supplier<Double> kPLimelight = limelightPIDNamespace.addConstantDouble("kP", 0.0133);
    private final Supplier<Double> kILimelight = limelightPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDLimelight = limelightPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceLimelight = limelightPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeLimelight = limelightPIDNamespace.addConstantDouble("wait time", 1);
    private final PIDSettings pidSettingsLimelight;

    private final Supplier<Double> kS = FeedForwardNamespace.addConstantDouble("kS", 0.24);
    private final Supplier<Double> kV = FeedForwardNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kA = FeedForwardNamespace.addConstantDouble("kA", 0);
    private final FeedForwardSettings ffSettings;

    public static Drivetrain getInstance() {
        if (instance == null) {
            WPI_TalonSRX pigeonTalon = new WPI_TalonSRX(RobotMap.CAN.PIGEON_TALON);
            WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_1);
            instance = new Drivetrain(new BustedMotorControllerGroup(
                    leftCorrection,
                    new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_1),
                    pigeonTalon // @TODO: If you change the pigeon talon change this as well
            ),
                    new BustedMotorControllerGroup(
                            rightCorrection,
                            rightTalon,
                            new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_2)
                    ),
                    pigeonTalon,
                    pigeonTalon,
                    rightTalon,
                    new Limelight()
            );
        }
        return instance;
    }

    private Drivetrain(MotorControllerGroup leftMotors, BustedMotorControllerGroup rightMotors, WPI_TalonSRX pigeonTalon,
                       WPI_TalonSRX leftTalon, WPI_TalonSRX rightTalon, Limelight limelight) {
        super("drivetrain", leftMotors, rightMotors);
        this.pigeon = new PigeonWrapper(pigeonTalon);
        this.leftTalon = leftTalon;
        this.rightTalon = rightTalon;
        this.leftEncoder = new TalonEncoder(leftTalon, DISTANCE_PER_PULSE);
        this.rightEncoder = new TalonEncoder(rightTalon, DISTANCE_PER_PULSE);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.limelight = limelight;
        this.pidSettingsGyro = new PIDSettings(this.kPGyro, this.kIGyro, this.kDGyro, this.toleranceGyro,
                this.waitTimeGyro);
        this.pidSettingsEncoders = new PIDSettings(this.kPEncoders, this.kIEncoders, this.kDEncoders,
                this.toleranceEncoders, this.waitTimeEncoders);
        this.pidSettingsCamera = new PIDSettings(this.kPCamera, this.kICamera, this.kDCamera,
                this.toleranceCamera, this.waitTimeCamera);
        this.pidSettingsLimelight = new PIDSettings(this.kPLimelight, this.kILimelight, this.kDLimelight,
                this.toleranceLimelight, this.waitTimeLimelight);
        this.ffSettings = new FeedForwardSettings(this.kS, this.kV, this.kA);
        this.odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getYaw()));
        this.field2d = new Field2d();
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void resetPigeon() {
        pigeon.reset();
    }

    @Override
    public void periodic() {
        rootNamespace.update();
        field2d.setRobotPose(odometry.getPoseMeters());
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
    }

    public double getLeftDistance() {
        return leftEncoder.getPosition();
    }

    public double getRightDistance() {
        return rightEncoder.getDistancePerPulse();
    }

    public WPI_TalonSRX getLeftTalon() {
        return leftTalon;
    }

    public WPI_TalonSRX getRightTalon() {
        return rightTalon;
    }

    public PIDSettings getGyroPIDSettings() {
        return pidSettingsGyro;
    }

    public PIDSettings getLimelightPIDSettings() {
        return pidSettingsLimelight;
    }

    public PIDSettings getCameraPIDSettings() {
        return pidSettingsCamera;
    }

    public PIDSettings getEncoderPIDSettings() {
        return pidSettingsEncoders;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }

    public Limelight getLimelight() {
        return limelight;
    }

    public void setOdometry(double x, double y, double angle) {
        odometry.resetPosition(new Pose2d(x, y, new Rotation2d(angle)), new Rotation2d(angle));
    }

    /**
     * Initializes namespaces and adds sensor data to dashboard.
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putData("field 2d", field2d);
        encoderNamespace.putNumber("left distance", leftEncoder::getPosition);
        encoderNamespace.putNumber("right distance", rightEncoder::getPosition);
        encoderNamespace.putData("reset encoders", new InstantCommand(this::resetEncoders) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
        gyroNamespace.putNumber("yaw", this::getYaw);
        gyroNamespace.putData("reset pigeon", new InstantCommand(this::resetPigeon) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
        rootNamespace.putNumber("right talon current", rightTalon::getStatorCurrent);
        rootNamespace.putNumber("left talon current", leftTalon::getStatorCurrent);
        rootNamespace.putData("drive forward", new DriveTank(this, 1, 1));
    }
}
