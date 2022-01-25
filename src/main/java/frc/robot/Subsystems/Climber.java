package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;


public class Climber extends MotoredGenericSubsystem {
    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = -0.6;

    private static Climber instance;
    private static boolean counted = false;
    private static int magnets = 0;
    private static DigitalInput digitalInput;


    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber(new WPI_VictorSPX(-1), new WPI_VictorSPX(-1), new DigitalInput(-1));
        }
        return instance;
    }

    public Climber(WPI_VictorSPX rightVictor, WPI_VictorSPX leftVictor, DigitalInput digitalInput) {
        super(MIN_SPEED, MAX_SPEED, "climber", rightVictor, leftVictor);
        leftVictor.follow(rightVictor);
        this.digitalInput = digitalInput;
    }

    @Override
    public void apply(double speed) {
        super.apply(speed);
        if (counted && digitalInput.get())
            counted = false;
    }

    @Override
    public boolean canMove(double speed) {
        if (!counted && digitalInput.get()) {
            if (speed > 0)
                magnets++;
            else
                magnets--;
        }
        if (magnets == 2 && speed < 0)
            return true;
        if (magnets == 0 && speed > 0)
            return true;
        return false;
    }


}
