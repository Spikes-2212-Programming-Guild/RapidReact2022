package frc.robot;

import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Commands.IntakeCargo;

public class OI /* GEVALD */ {

    private XboxControllerWrapper controller = new XboxControllerWrapper(0);

    public OI() {
        Button intake = controller.getGreenButton();
        intake.whenPressed(new IntakeCargo());
    }
}
