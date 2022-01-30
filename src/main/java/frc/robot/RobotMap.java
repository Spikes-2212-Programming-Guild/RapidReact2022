package frc.robot;

public class RobotMap {

    public interface CAN {

        int WINCH_VICTOR_RIGHT = -1;
        int WINCH_VICTOR_LEFT = -1;
        int PLACER_TALON_RIGHT = -1;
        int PLACER_TALON_LEFT = -1;
    }

    public interface DIO {

        int WINCH_HALL_EFFECT = -1;
        int PLACER_LIMIT_UP = -1;
        int PLACER_LIMIT_DOWN = -1;
        int PLACER_LIMIT_HOOK = -1;
    }
}
