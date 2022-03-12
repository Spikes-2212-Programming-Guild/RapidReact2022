package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_TALON_1 = 1;
        int DRIVETRAIN_LEFT_TALON_2 = 3;
        int DRIVETRAIN_RIGHT_TALON_1 = 2;
        int DRIVETRAIN_RIGHT_TALON_2 = 4;
        int PIGEON_TALON = DRIVETRAIN_LEFT_TALON_2;

        int INTAKE_ROLLER_VICTOR = 5;

        int INTAKE_PLACER_VICTOR = 6;

        int INTAKE_TO_TRANSFER_VICTOR = 7;

        int TRANSFER_STRAP_VICTOR_1 = 8;
        int TRANSFER_STRAP_VICTOR_2 = 9;

        int CLIMBER_WINCH_SPARK_MAX_1 = 12;
        int CLIMBER_WINCH_SPARK_MAX_2 = 11;
    }

    public interface DIO {

        int DRIVETRAIN_LEFT_ENCODER_POS = 4;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 5;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 6;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 7;

        int INTAKE_PLACER_UPPER_LIMIT = 0;
        int INTAKE_PLACER_LOWER_LIMIT = 1;

        int TRANSFER_ENTRANCE_LIGHT_SENSOR = 2;

        int INTAKE_TO_TRANSFER_LIMIT = 3;
    }

    public interface PWM {

        int INTAKE_PLACER_SERVO = 5;
    }
}
