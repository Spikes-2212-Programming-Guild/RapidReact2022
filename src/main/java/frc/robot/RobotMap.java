package frc.robot;

public class RobotMap {

    public interface CAN {

        int CLIMBER_WINCH_SPARK_MAX_1 = 2;
        int CLIMBER_WINCH_SPARK_MAX_2 = 12;

        int CLIMBER_PLACER_TALON_LEFT = 0;
        int CLIMBER_PLACER_TALON_RIGHT = 1;
    }

    public interface DIO {

        int CLIMBER_WINCH_HALL_EFFECT = 10;

        int CLIMBER_PLACER_LEFT_LIMIT_FRONT = 4;
        int CLIMBER_PLACER_LEFT_LIMIT_BACK = 5;
        int CLIMBER_PLACER_RIGHT_LIMIT_FRONT = 6;
        int CLIMBER_PLACER_RIGHT_LIMIT_BACK = 7;
        int CLIMBER_PLACER_LEFT_HOOK_LIMIT = 8;
        int CLIMBER_PLACER_RIGHT_HOOK_LIMIT = 9;
    }
}
