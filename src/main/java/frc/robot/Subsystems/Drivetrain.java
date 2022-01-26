package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RobotMap;

public class Drivetrain extends TankDrivetrain {
    private static Drivetrain drivetrain;

    private PigeonWrapper pigeon;
    private Encoder leftEncoder, rightEncoder;
    private ColorSensorV3 rightColorSensor, leftColorSensor;

    public static Drivetrain getInstance() {
        if (drivetrain == null) {
            drivetrain = new Drivetrain();
        }
        return drivetrain;
    }

    private Drivetrain() {
        super(new MotorControllerGroup(new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_1), new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_2)),
                new MotorControllerGroup(new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_1), new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_2)));

        this.pigeon = new PigeonWrapper(RobotMap.CAN.DRIVETRAIN_PIGEON);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.rightColorSensor = new ColorSensorV3(I2C.Port.kOnboard);
        this.leftColorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }
}
