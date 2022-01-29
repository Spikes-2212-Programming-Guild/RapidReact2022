package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);

    public double getRightY() {
        return right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
