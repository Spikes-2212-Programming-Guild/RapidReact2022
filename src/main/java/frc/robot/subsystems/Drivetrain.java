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
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    private static Drivetrain instance;

    private final Namespace gyroNamespace = rootNamespace.addChild("gyro");
    private final Namespace gyroPIDNamespace = gyroNamespace.addChild("gyro pid");
    private final Namespace cameraPIDNamespace = rootNamespace.addChild("camera pid");
    private final Namespace FeedForwardNamespace = rootNamespace.addChild("feed forward");

    /**
     * One side of the robot is faster than the other. To solve this we slow down one of the sides.
     */
    private static final RootNamespace corrections = new RootNamespace("drivetrain correction");
    private static final Supplier<Double> rightCorrection = corrections.addConstantDouble("right correction", 0.95);
    private static final Supplier<Double> leftCorrection = corrections.addConstantDouble("left correction", 1);

    private final PigeonWrapper pigeon;
    private final WPI_TalonSRX leftTalon, rightTalon;
    private final Limelight limelight;

    private final Supplier<Double> kPGyro = gyroPIDNamespace.addConstantDouble("kP", 0.017);
    private final Supplier<Double> kIGyro = gyroPIDNamespace.addConstantDouble("kI", 0.0028);
    private final Supplier<Double> kDGyro = gyroPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceGyro = gyroPIDNamespace.addConstantDouble("tolerance", 5);
    private final Supplier<Double> waitTimeGyro = gyroPIDNamespace.addConstantDouble("wait time", 0.1);
    private final PIDSettings pidSettingsGyro;

    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("kP", 0.0065);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceCamera = cameraPIDNamespace.addConstantDouble("tolerance", 2);
    private final Supplier<Double> waitTimeCamera = cameraPIDNamespace.addConstantDouble("wait time", 999);
    private final PIDSettings pidSettingsCamera;

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

    private Drivetrain(MotorControllerGroup leftMotors, MotorControllerGroup rightMotors, WPI_TalonSRX pigeonTalon,
                       WPI_TalonSRX leftTalon, WPI_TalonSRX rightTalon, Limelight limelight) {
        super("drivetrain", leftMotors, rightMotors);
        this.pigeon = new PigeonWrapper(pigeonTalon);
        this.leftTalon = leftTalon;
        this.rightTalon = rightTalon;
        this.limelight = limelight;
        this.pidSettingsGyro = new PIDSettings(this.kPGyro, this.kIGyro, this.kDGyro, this.toleranceGyro,
                this.waitTimeGyro);
        this.pidSettingsCamera = new PIDSettings(this.kPCamera, this.kICamera, this.kDCamera,
                this.toleranceCamera, this.waitTimeCamera);
        this.ffSettings = new FeedForwardSettings(this.kS, this.kV, this.kA);
    }

    public void resetPigeon() {
        pigeon.reset();
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
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

    public PIDSettings getCameraPIDSettings() {
        return pidSettingsCamera;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }

    public Limelight getLimelight() {
        return limelight;
    }

    /**
     * Initializes namespaces and adds sensor data to dashboard.
     */
    @Override
    public void configureDashboard() {
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
