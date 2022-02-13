package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_TALON_1 = -1;
        int DRIVETRAIN_LEFT_TALON_2 = -1;
        int DRIVETRAIN_RIGHT_TALON_1 = -1;
        int DRIVETRAIN_RIGHT_TALON_2 = -1;
        int PIGEON_TALON = DRIVETRAIN_LEFT_TALON_1;
        int INTAKE_PLACER_VICTOR = -1;
        int INTAKE_ROLLER_VICTOR = -1;
        int INTAKE_TO_TRANSFER_TALON = -1;
        int TRANSFER_STRAP_TALON_1 = -1;
        int TRANSFER_STRAP_TALON_2 = -1;
    }

    public interface DIO {

        int DRIVETRAIN_LEFT_ENCODER_POS = -1;
        int DRIVETRAIN_LEFT_ENCODER_NEG = -1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = -1;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = -1;
        int INTAKE_PLACER_UPPER_LIMIT = -1;
        int INTAKE_PLACER_LOWER_LIMIT = -1;
        int TRANSFER_ENTRANCE_LIGHT_SENSOR = -1;
        int INTAKE_TO_TRANSFER_LIMIT = -1;
    }
}
