package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RobotMap;

public class Drivetrain extends TankDrivetrain {
    private static Drivetrain drivetrain;

    private ADXRS450_Gyro gyro;
    private Encoder leftEncoder, rightEncoder;

    private Drivetrain() {
        super(new MotorControllerGroup(new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_1), new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR_2)),
                new MotorControllerGroup(new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_1), new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR_2)));

        this.gyro = new ADXRS450_Gyro();
        this.leftEncoder = new Encoder(RobotMap.CAN.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.CAN.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.CAN.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.CAN.DRIVETRAIN_RIGHT_ENCODER_NEG);
    }

    public static Drivetrain getInstance() {
        if (drivetrain == null) {
            drivetrain = new Drivetrain();
        }

        return drivetrain;
    }
}
