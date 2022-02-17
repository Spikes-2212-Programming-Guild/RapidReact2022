package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Commands.IntakeCargo;
import frc.robot.Commands.ReleaseCargo;
import frc.robot.Subsystems.IntakePlacer;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    public OI() {
        xbox.getRTButton().whenActive(new IntakeCargo());
        xbox.getRBButton().whenPressed(new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED));
        xbox.getLTButton().whileActiveOnce(new ReleaseCargo());
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
