package frc.robot;

public class RobotMap {

    public interface CAN {

        int INTAKE_ROLLER_TALON = 5;
        int INTAKE_PLACER_VICTOR = 6;
        int INTAKE_TO_TRANSFER_VICTOR = 7;
        int TRANSFER_STRAP_VICTOR_1 = 8;
        int TRANSFER_STRAP_VICTOR_2 = 9;
    }

    public interface DIO {

        int INTAKE_PLACER_UPPER_LIMIT = 0;
        int INTAKE_PLACER_LOWER_LIMIT = 1;
        int TRANSFER_ENTRANCE_LIGHT_SENSOR = 2;
        int INTAKE_TO_TRANSFER_LIMIT = 3;
    }
}
