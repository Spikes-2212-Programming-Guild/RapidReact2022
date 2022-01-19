package frc.robot;

import com.spikes2212.util.XboXUID;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Commands.IntakeCargo;

public class OI /* GEVALD */ {

    private XboXUID controller = new XboXUID(0);

    public OI() {
        Button intake = controller.getRBButton();
        intake.whenPressed(new IntakeCargo());
    }
}
