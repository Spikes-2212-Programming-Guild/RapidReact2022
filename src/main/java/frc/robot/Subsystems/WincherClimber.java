package frc.robot.Subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;


public class WincherClimber extends MotoredGenericSubsystem {
    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    private Level magnetLevel = Level.LOWER;
    private static WincherClimber instance;
    private final DigitalInput HallEffect;

    enum Level {UPPER, LOWER, MIDDLE}

    public WincherClimber(WPI_VictorSPX rightWinch, WPI_VictorSPX leftWinch, DigitalInput HallEffect) {
        super(MIN_SPEED, MAX_SPEED, "wincherClimber", rightWinch, leftWinch);
        this.HallEffect = HallEffect;
    }

    public static WincherClimber getInstance() {
        if (instance == null) {
            instance = new WincherClimber(new WPI_VictorSPX(-1), new WPI_VictorSPX(-1),
                    new DigitalInput(-1));
        }
        return instance;
    }


    @Override
    public boolean canMove(double speed) {
        if (HallEffect.get()) {
            if (speed > 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.UPPER;
            if (speed < 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.LOWER;
        } else
            magnetLevel = Level.MIDDLE;
        if (magnetLevel == Level.UPPER && speed > 0 || magnetLevel == Level.LOWER && speed < 0)
            return false;
        return true;
    }

}
