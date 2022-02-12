package frc.robot;

public class RobotMap {

    public interface CAN {

        int INTAKE_PLACER_VICTOR = -1;
        int INTAKE_ROLLER_VICTOR = -1;
        int INTAKE_TO_TRANSFER_TALON = -1;
        int TRANSFER_STRAP_TALON_1 = -1;
        int TRANSFER_STRAP_TALON_2 = -1;
    }

    public interface DIO {

        int INTAKE_PLACER_UPPER_LIMIT = -1;
        int INTAKE_PLACER_LOWER_LIMIT = -1;
        int TRANSFER_ENTRANCE_LIGHT_SENSOR = -1;
        int INTAKE_TO_TRANSFER_LIMIT = -1;
    }
}
