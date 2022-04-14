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
    private static final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

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
    }

    public static boolean buttonPressed() {
        return xbox.getBlueButton().get();
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
