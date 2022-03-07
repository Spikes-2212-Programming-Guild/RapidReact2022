package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    /**
     * <b>Trigger of Left Joystick</b>: Aim to cargo<br>
     * <b>Right Trigger</b>: Intake cargo<br>
     * <b>Right Bumper</b>: Intake placer up<br>
     * <b>Left Trigger</b>: Release cargo<br>
     * <b>Green Button</b>: Climber down<br>
     * <b>Yellow Button</b>: Climber up<br>
     * <b>Blue Button</b>: Climber stop<br>
     * <b>Red Button</b>: Roller stop<br>
     * <b>D-Pad Down Button</b>: Reverse all<br>
     * <b>D-Pad Left Button</b>: Reverse IntakeToTransfer and Intake<br>
     * <b>Xbox Left Joystick Button</b>: Move to next bar
     */
    public OI() {
        IntakeRoller roller = IntakeRoller.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        Transfer transfer = Transfer.getInstance();
        ClimberWinch climberWinch = ClimberWinch.getInstance();

        JoystickButton trigger = new JoystickButton(right, 1);
        trigger.whileHeld(new ReleaseCargo());

        xbox.getRTButton().whenActive(new IntakeCargo());
        xbox.getRBButton().whenPressed(new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED));
        xbox.getLTButton().whileActiveOnce(new ReleaseCargo());

        xbox.getGreenButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED));
        xbox.getYellowButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED));
        xbox.getBlueButton().whenPressed(new MoveGenericSubsystem(climberWinch, 0));
        xbox.getRedButton().whenPressed(new MoveGenericSubsystem(roller, 0));

        xbox.getLeftStickButton().whenPressed(new MoveToNextBar());

        //reverse all the subsystems, to return cargos
        xbox.getDownButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MAX_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, -IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(transfer, () -> -transfer.MOVE_SPEED.get())
        ));

        //reverse the roller and intakeToTransfer to return the cargo that is in the intakeToTransfer
        xbox.getLeftButton().whileHeld(new ParallelCommandGroup(
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
