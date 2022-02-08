package frc.robot;

public class RobotMap {

    public interface CAN {

        int CLIMBER_WINCH_SPARKMAX_1 = -1;
        int CLIMBER_WINCH_SPARKMAX_2 = -1;
        int CLIMBER_PLACER_TALON_RIGHT = -1;
        int CLIMBER_PLACER_TALON_LEFT = -1;
    }

    public interface DIO {

        int CLIMBER_WINCH_HALL_EFFECT = -1;
        int CLIMBER_PLACER_LEFT_LIMIT_FRONT = -1;
        int CLIMBER_PLACER_LEFT_LIMIT_BACK = -1;
        int CLIMBER_PLACER_RIGHT_LIMIT_FRONT = -1;
        int CLIMBER_PLACER_RIGHT_LIMIT_BACK = -1;
        int CLIMBER_PLACER_HOOK_LIMIT = -1;
    }
}
