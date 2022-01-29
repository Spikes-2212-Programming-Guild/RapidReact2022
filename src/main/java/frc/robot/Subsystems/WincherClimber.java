package frc.robot.Subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;


public class WincherClimber extends MotoredGenericSubsystem {
    private WPI_VictorSPX rightWinch;
    private WPI_VictorSPX leftWinch;
    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    private static int magnets = 0;
    private static boolean counted = false;
    private static WincherClimber instance;
    private static DigitalInput HallEffect;

    public WincherClimber(WPI_VictorSPX rightWinch, WPI_VictorSPX leftWinch, DigitalInput HallEffect) {
        super(MIN_SPEED, MAX_SPEED, "wincherClimber", rightWinch, leftWinch);
        this.rightWinch = rightWinch;
        this.leftWinch = leftWinch;
        this.HallEffect = HallEffect;
        leftWinch.follow(rightWinch);
        rightWinch.setInverted(true);

    }

    public static WincherClimber getInstance() {
        if (instance == null) {
            instance = new WincherClimber(new WPI_VictorSPX(-1), new WPI_VictorSPX(-1),
                    new DigitalInput(-1));
        }
        return instance;
    }

    @Override
    public void apply(double speed) {
        super.apply(speed);
        if (counted && HallEffect.get())
            counted = false;;
    }

    @Override
    public boolean canMove(double speed) {
        if (!counted && HallEffect.get()) {
            if (speed > 0)
                magnets++;
            else {
                magnets--;
                counted = true;
            }
        }

        if (magnets == 2 && speed < 0)
            return true;
        if (magnets == 0 && speed > 0)
            return true;
        return false;

    }

    @Override
    public void stop() {
        rightWinch.stopMotor();

    }

}
