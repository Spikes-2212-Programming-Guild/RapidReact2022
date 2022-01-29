package frc.robot;

public class RobotMap {
    public interface CAN {
        int DRIVETRAIN_LEFT_VICTOR_1 = 1;
        int DRIVETRAIN_LEFT_VICTOR_2 = 3;
        int DRIVETRAIN_RIGHT_VICTOR_1 = 2;
        int DRIVETRAIN_RIGHT_VICTOR_2 = 4;
        int DRIVETRAIN_PIGEON = -1;
    }

    public interface DIO {
        int DRIVETRAIN_LEFT_ENCODER_POS = -1;
        int DRIVETRAIN_LEFT_ENCODER_NEG = -1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = -1;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = -1;
    }
}