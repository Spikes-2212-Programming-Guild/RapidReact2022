package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI /* GEVALD */ {
    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);

    public OI() {

    }

    public double getRightY() {
        return right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
