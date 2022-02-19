package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.ReleaseCargo;
import frc.robot.subsystems.IntakePlacer;
import frc.robot.subsystems.IntakeRoller;
import frc.robot.subsystems.IntakeToTransfer;
import frc.robot.subsystems.Transfer;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(0);
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(0);

    public OI() {
        IntakeRoller roller = IntakeRoller.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        Transfer transfer = Transfer.getInstance();

        xbox.getRTButton().whenActive(new IntakeCargo());
        xbox.getRBButton().whenPressed(new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED));
        xbox.getLTButton().whileActiveOnce(new ReleaseCargo());

        xbox.getLeftButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MAX_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, -IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(transfer, () -> -transfer.MOVE_SPEED.get())
        ));

        xbox.getDownButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MAX_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, -IntakeToTransfer.SPEED)
        ));

    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
