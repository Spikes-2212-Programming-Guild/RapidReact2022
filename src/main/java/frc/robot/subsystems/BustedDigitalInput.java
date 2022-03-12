package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 * An extension of the {@code DigitalInput} class that checks for a continuous state over a specified period of time.
 */
public class BustedDigitalInput extends DigitalInput {

    public static final double TIMEOUT = 0.1;

    protected double firstOn = 0;

    public BustedDigitalInput(int channel) {
        super(channel);
    }

    @Override
    public boolean get() {
        if (firstOn != 0 && super.get() && firstOn + TIMEOUT < Timer.getFPGATimestamp())
            return true;
        if (!super.get())
            firstOn = 0;
        else {
            if (firstOn == 0)
                firstOn = Timer.getFPGATimestamp();
        }
        return false;
    }
}
