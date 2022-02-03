package frc.robot;

public class RobotMap {

    public interface CAN {
        int DRIVETRAIN_LEFT_TALON_1 = -1;
        int DRIVETRAIN_LEFT_TALON_2 = -1;
        int DRIVETRAIN_RIGHT_TALON_1 = -1;
        int DRIVETRAIN_RIGHT_TALON_2 = -1;

        int DRIVETRAIN_PIGEON = -1;
    }

    public interface DIO {
        int DRIVETRAIN_LEFT_ENCODER_CHANNEL_A = -1;
        int DRIVETRAIN_LEFT_ENCODER_CHANNEL_B = -1;
        int DRIVETRAIN_RIGHT_ENCODER_CHANNEL_A = -1;
        int DRIVETRAIN_RIGHT_ENCODER_CHANNEL_B = -1;
    }
}
