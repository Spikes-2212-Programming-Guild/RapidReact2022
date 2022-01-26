package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.dashboard.ChildNamespace;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.RobotMap;
import frc.robot.utils.BustedMotorControllerGroup;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {
    private static final double DISTANCE_PER_PULSE = 20.32 / 4096;

    private static Supplier<Double> rightCorrection;

    private static Drivetrain drivetrain;

    private RootNamespace root;
    private ChildNamespace encoderNamespace;
    private ChildNamespace leftColorSensorNamespace;
    private ChildNamespace rightColorSensorNamespace;

    private final PigeonWrapper pigeon;
    private final Encoder leftEncoder, rightEncoder;
    private final ColorSensorV3 rightColorSensor, leftColorSensor;

    public static Drivetrain getInstance() {
        if (drivetrain == null) {
            drivetrain = new Drivetrain(new MotorControllerGroup(
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_1),
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_2)
                    ),
                    new BustedMotorControllerGroup(
                            rightCorrection,
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_1),
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_2)
                    )
            );
        }
        return drivetrain;
    }

    private Drivetrain(MotorControllerGroup leftMotors, BustedMotorControllerGroup rightMotors) {
        super(leftMotors, rightMotors);

        this.pigeon = new PigeonWrapper(RobotMap.CAN.DRIVETRAIN_PIGEON);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.rightColorSensor = new ColorSensorV3(I2C.Port.kOnboard);
        this.leftColorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }

    @Override
    public void periodic() {
        root.update();
    }

    public Color getLeftColor() {
        return leftColorSensor.getColor();
    }

    public Color getRightColor() {
        return rightColorSensor.getColor();
    }

    public void configureDashboard() {
        root = new RootNamespace("drivetrain");
        leftColorSensorNamespace = (ChildNamespace) root.addChild("left color sensor");
        rightColorSensorNamespace = (ChildNamespace) root.addChild("right color sensor");
        encoderNamespace = (ChildNamespace) root.addChild("encoders");

        rightCorrection = root.addConstantDouble("right correction", 0);

        encoderNamespace.putNumber("left ticks", this.leftEncoder::get);
        encoderNamespace.putNumber("right ticks", this.rightEncoder::get);
        encoderNamespace.putNumber("left distance", this.leftEncoder::getDistance);
        encoderNamespace.putNumber("right distance", this.rightEncoder::getDistance);

        leftColorSensorNamespace.putNumber("left color sensor red", () -> this.getLeftColor().red);
        leftColorSensorNamespace.putNumber("left color sensor blue", () -> this.getLeftColor().blue);
        leftColorSensorNamespace.putNumber("left color sensor green", () -> this.getLeftColor().green);

        rightColorSensorNamespace.putNumber("right color sensor red", () -> this.getRightColor().red);
        rightColorSensorNamespace.putNumber("right color sensor blue", () -> this.getRightColor().blue);
        rightColorSensorNamespace.putNumber("right color sensor green", () -> this.getRightColor().green);
    }
}
