package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class OI /* GEVALD */ {

    private static final Joystick left = new Joystick(0);
    private static final Joystick right = new Joystick(1);
    private static final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    /**
     * <b>Trigger of Left Joystick</b>: Reverse Transfer<br>
     * <b>Trigger of Right Joystick</b>: Release Cargo<br>
     * <b>Right Trigger</b>: Intake Cargo<br>
     * <b>Right Bumper</b>: Intake Placer Up<br>
     * <b>Left Trigger</b>: Release Cargo<br>
     * <b>Left Bumper</b>: Intake Two Cargos<br>
     * <b>Green Button</b>: Climber Down<br>
     * <b>Yellow Button</b>: Climber Up<br>
     * <b>Blue Button</b>: Stop Climber<br>
     * <b>Red Button</b>: Stop Roller<br>
     * <b>D-Pad Down Button</b>: Reverse All<br>
     * <b>D-Pad Up Button</b>: Reverse IntakeToTransfer And Intake<br>
     * <b>D-Pad Left Button</b>: Climber Placer Down<br>
     * <b>D-Pad Right Button</b>: Climber Placer Up<br>
     * <b>Xbox Left Joystick Button</b>: Move To Next Bar
     */
    public OI() {
        IntakeRoller roller = IntakeRoller.getInstance();
        IntakeToTransfer intakeToTransfer = IntakeToTransfer.getInstance();
        Transfer transfer = Transfer.getInstance();
        ClimberWinch climberWinch = ClimberWinch.getInstance();
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();

        JoystickButton trigger1 = new JoystickButton(right, 1);
        trigger1.whileHeld(new ReleaseCargo());
        JoystickButton trigger2 = new JoystickButton(left, 1);
        trigger2.whileHeld(new MoveGenericSubsystem(transfer, -transfer.MOVE_SPEED.get()));

        xbox.getRTButton().whenActive(new IntakeCargo());
        xbox.getRBButton().whenPressed(new MoveGenericSubsystem(IntakePlacer.getInstance(), IntakePlacer.MAX_SPEED));
        xbox.getLTButton().whileActiveOnce(new ReleaseCargo());

        // intakes two cargos at once
        xbox.getLBButton().whenPressed(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MIN_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(transfer, transfer.MOVE_SPEED.get()).withInterrupt(transfer::getEntranceSensor)
        ).withInterrupt(() -> (intakeToTransfer.getLimit() && transfer.getEntranceSensor())));

        xbox.getGreenButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.DOWN_SPEED));
        xbox.getYellowButton().whenPressed(new MoveGenericSubsystem(climberWinch, ClimberWinch.UP_SPEED));
        xbox.getBlueButton().whenPressed(new ParallelCommandGroup(new MoveGenericSubsystem(climberWinch, 0),
                new MoveGenericSubsystem(leftPlacer, 0), new MoveGenericSubsystem(rightPlacer, 0)));
        xbox.getRedButton().whenPressed(new MoveGenericSubsystem(roller, 0));

        xbox.getLeftStickButton().whenPressed(new MoveToNextBar());

        //reverse all the subsystems, to return cargos
        xbox.getDownButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MAX_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, -IntakeToTransfer.SPEED),
                new MoveGenericSubsystem(transfer, () -> -transfer.MOVE_SPEED.get())
        ));

        //reverse the roller and intakeToTransfer to return the cargo that is in the intakeToTransfer
        xbox.getUpButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(roller, IntakeRoller.MAX_SPEED),
                new MoveGenericSubsystem(intakeToTransfer, -IntakeToTransfer.SPEED)
        ));

        xbox.getLeftButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(leftPlacer, ClimberPlacer.MIN_SPEED),
                new MoveGenericSubsystem(rightPlacer, ClimberPlacer.MIN_SPEED)
        ));
        xbox.getRightButton().whileHeld(new ParallelCommandGroup(
                new MoveGenericSubsystem(leftPlacer, ClimberPlacer.MIN_SPEED),
                new MoveGenericSubsystem(rightPlacer, ClimberPlacer.MIN_SPEED)));
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }

    public static void setRumble(GenericHID.RumbleType rumbleType, double value) {
        xbox.setRumble(rumbleType, value);
    }
}
