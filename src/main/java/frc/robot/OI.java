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
     * <b>Trigger of Right Joystick</b>: Release cargo<br>
     * <b>Xbox Right Joystick Button</b>: Intake cargo (ignoring limit)<br>
     * <b>Right Trigger</b>: Intake cargo (not ignoring limit)<br>
     * <b>Right Bumper</b>: Intake placer up<br>
     * <b>Left Trigger</b>: Release cargo<br>
     * <b>Left Bumper</b>: Intake two cargos<br>
     * <b>Green Button</b>: Climber down (stops by encoder)<br>
     * <b>Yellow Button</b>: Climber up (stops by encoder)<br>
     * <b>Blue Button</b>: Stop ClimberWinch<br>
     * <b>Red Button</b>: Stop IntakeRoller<br>
     * <b>D-Pad Down Button</b>: Climber down (while held)<br>
     * <b>D-Pad Up Button</b>: Climber up (while held)<br>
     * <b>D-Pad Left Button</b>: Reverse IntakeToTransfer and IntakeRoller<br>
     * <b>D-Pad Right Button</b>: Reverse all
     */
    public OI() {
        IntakeRoller roller = IntakeRoller.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        Transfer transfer = Transfer.getInstance();
        ClimberWinch climberWinch = ClimberWinch.getInstance();

        JoystickButton trigger = new JoystickButton(right, 1);
        trigger.whileHeld(new ReleaseCargo());

        xbox.getRTButton().whenActive(new IntakeCargo(false));
        xbox.getRightStickButton().whileHeld(new IntakeCargo(true));

        xbox.getRBButton().whenPressed(new MoveIntakePlacerUp());
        xbox.getLTButton().whileActiveOnce(new ReleaseCargo());

        //intakes two cargos
        xbox.getLBButton().whenPressed(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MIN_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(transfer, transfer.MOVE_SPEED).withInterrupt(transfer::getEntranceSensor)
        ).withInterrupt(() -> (intakeToTransfer.getLimit()) && transfer.getEntranceSensor()));

        xbox.getGreenButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED));

        xbox.getYellowButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED));

        xbox.getDownButton().whileHeld(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED) {
                    @Override
                    public void execute() {
                        climberWinch.moveUsingApply(speedSupplier.get());
                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                });

        xbox.getUpButton().whileHeld(
                new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED) {
                    @Override
                    public void execute() {
                        climberWinch.moveUsingApply(speedSupplier.get());
                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                });

        //stops the climberWinch
        xbox.getBlueButton().whenPressed(new MoveGenericSubsystem(climberWinch, 0));

        //stops the roller
        xbox.getRedButton().whenPressed(new MoveGenericSubsystem(roller, 0) {
            @Override
            public boolean isFinished() {
                return true;
            }
        });

        //reverse all the subsystems, to return cargos
        xbox.getRightButton().whileHeld(new ParallelCommandGroup(
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
