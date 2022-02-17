package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Commands.IntakeCargo;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.Transfer;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(0);

    public OI() {
        xbox.getRTButton().whenActive(new IntakeCargo());
        xbox.getRBButton().whenPressed(new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED));
        xbox.getLTButton().whileActiveOnce(new ParallelCommandGroup(
                new MoveGenericSubsystem(Transfer.getInstance(), Transfer.getInstance().getTransferSpeed()),
                new MoveGenericSubsystem(IntakeToTransfer.getInstance(), IntakeToTransfer.SPEED)));
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
