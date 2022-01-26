package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.RobotMap;
import frc.robot.utils.BustedMotorControllerGroup;

public class Drivetrain extends TankDrivetrain {
    private static final double RIGHT_CORRECTION = 0.7;

    private static Drivetrain drivetrain;

    private final RootNamespace root = new RootNamespace("drivetrain");

    private PigeonWrapper pigeon;
    private Encoder leftEncoder, rightEncoder;
    private ColorSensorV3 rightColorSensor, leftColorSensor;

    public static Drivetrain getInstance() {
        if (drivetrain == null) {
            drivetrain = new Drivetrain(new MotorControllerGroup(
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_1),
                            new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_2)
                    ),
                    new BustedMotorControllerGroup(
                            RIGHT_CORRECTION,
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
        this.leftEncoder.setDistancePerPulse(20.32 / 4096.0);
        this.rightEncoder.setDistancePerPulse(20.32 / 4096.0);
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
        root.putNumber("left encoder ticks", this.leftEncoder::get);
        root.putNumber("right encoder ticks", this.rightEncoder::get);
        root.putNumber("left encoder distance", this.leftEncoder::getDistance);
        root.putNumber("right encoder distance", this.rightEncoder::getDistance);

        root.putNumber("left color sensor red", () -> this.getLeftColor().red);
        root.putNumber("left color sensor blue", () -> this.getLeftColor().blue);
        root.putNumber("left color sensor green", () -> this.getLeftColor().green);

        root.putNumber("right color sensor red", () -> this.getRightColor().red);
        root.putNumber("right color sensor blue", () -> this.getRightColor().blue);
        root.putNumber("right color sensor green", () -> this.getRightColor().green);
    }
}
