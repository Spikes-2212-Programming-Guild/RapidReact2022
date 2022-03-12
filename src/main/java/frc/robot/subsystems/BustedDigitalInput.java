package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class BustedDigitalInput extends DigitalInput {

    protected double firstOn = 0;

    public static final double TIMEOUT = 0.1;

    /**
     * Create an instance of a Digital Input class. Creates a digital input given a channel.
     *
     * @param channel the DIO channel for the digital input 0-9 are on-board, 10-25 are on the MXP
     */
    public BustedDigitalInput(int channel) {
        super(channel);
    }

    @Override
    public boolean get() {
        if (super.get() && firstOn != 0 && firstOn + TIMEOUT < Timer.getFPGATimestamp())
            return true;
        if (!super.get())
            firstOn = 0;
        else {
            if (super.get() && firstOn == 0)
                firstOn = Timer.getFPGATimestamp();
        }
        return false;
    }
}
