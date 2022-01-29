package frc.robot;

public class RobotMap {
    public interface CAN{
        int WINCHER_VICTOR_RIGHT=-1;
        int WINCHER_VICTOR_LEFT=-1;
        int PLACER_TALON_RIGHT=-1;
        int PLACER_TALON_LEFT=-1;
    }

    public interface DIO{
        int WINCHER_HALL_EFFECT_LEFT=-1;
        int WINCHER_HALL_EFFECT_RIGHT=-1;
        int PLACER_LIMIT_RIGHT_UP=-1;
        int PLACER_LIMIT_RIGHT_DOWN=-1;
        int PLACER_LIMIT_LEFT_UP=-1;
        int PLACER_LIMIT_LEFT_DOWN=-1;
        int PLACER_LIMIT_HOOK_LEFT=-1;
        int PLACER_LIMIT_HOOK_RIGHT=-1;
    }
}
