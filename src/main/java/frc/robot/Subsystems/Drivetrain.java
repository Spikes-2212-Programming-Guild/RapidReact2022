package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.utils.BustedMotorControllerGroup;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    /**
     * The wheel moves 20.32 * PI (it's perimeter) each 360 ticks.
     * In meters.
     */
    private static final double DISTANCE_PER_PULSE = 20.32 * Math.PI / 360.0 / 100;

    private static Drivetrain instance;

    private static final RootNamespace rootNamespace = new RootNamespace("drivetrain");
    private static final Namespace encoderNamespace = rootNamespace.addChild("encoders");
    private static final Namespace pigeonNamespace = rootNamespace.addChild("pigeon");
    private static final Namespace PIDNamespace = rootNamespace.addChild("PID");
    private static final Namespace FeedForwardNamespace = rootNamespace.addChild("feed forward");

    /**
     * One side of the robot is faster than the other. To solve this we slow down one of the sides.
     */
    private static final Supplier<Double> rightCorrection = rootNamespace.addConstantDouble("right correction", 1);
    private static final Supplier<Double> leftCorrection = rootNamespace.addConstantDouble("left correction", 1);

    private final PigeonWrapper pigeon;
    private final Encoder leftEncoder, rightEncoder;

    private final Supplier<Double> kP = PIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = PIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = PIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> tolerance = PIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTime = PIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettings;

    private final Supplier<Double> kV = FeedForwardNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kS = FeedForwardNamespace.addConstantDouble("kS", 0);
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
        this.pigeon = new PigeonWrapper(pigeonTalon);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.pidSettings = new PIDSettings(this.kP, this.kI, this.kD, this.tolerance, this.waitTime);
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
        return pigeon.getYaw() % 360;
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

        pigeonNamespace.putNumber("yaw", this::getYaw);
        pigeonNamespace.putData("reset pigeon", new InstantCommand(pigeon::reset) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
    }
}
