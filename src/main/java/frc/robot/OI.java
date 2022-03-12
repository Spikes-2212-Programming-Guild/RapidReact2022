package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
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
     * <b>Trigger of Left Joystick</b>: Aim To Cargo<br>
     * <b>Right Trigger</b>: Intake cargo<br>
     * <b>Right Bumper</b>: Intake placer up<br>
     * <b>Left Trigger</b>: Release Cargo<br>
     * <b>Green Button</b>: Climber Down<br>
     * <b>Yellow Button</b>: Climber Up<br>
     * <b>Blue Button</b>: Climber Stop<br>
     * <b>Red Button</b>: Roller Stop<br>
     * <b>D-Pad Down Button</b>: Reverse all<br>
     * <b>D-Pad Left Button</b>: Reverse IntakeToTransfer and Intake
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

        //lowers the climber
        xbox.getGreenButton().whenPressed(
                new MoveGenericSubsystemWithPID(climberWinch, climberWinch.ENCODER_DOWN_LIMIT,
                        climberWinch::getPosition, climberWinch.getPIDSettings(), climberWinch.getFFSettings()));

        //raises the climber
        xbox.getYellowButton().whenPressed(
                new MoveGenericSubsystemWithPID(climberWinch, climberWinch.ENCODER_UP_LIMIT,
                        climberWinch::getPosition, climberWinch.getPIDSettings(), climberWinch.getFFSettings()));
        xbox.getBlueButton().whenPressed(new MoveGenericSubsystem(climberWinch, 0));

        //stops the climber
        xbox.getRedButton().whenPressed(new MoveGenericSubsystem(roller, 0));

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
