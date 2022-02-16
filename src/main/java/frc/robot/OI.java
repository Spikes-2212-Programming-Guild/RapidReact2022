package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Commands.IntakeCargo;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;

public class OI /* GEVALD */ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    private final XboxControllerWrapper controller = new XboxControllerWrapper(2);

    public OI() {
        IntakeRoller intakeRoller = IntakeRoller.getInstance();
        IntakePlacer intakePlacer = IntakePlacer.getInstance();
        Button intake = controller.getRBButton();
        Button rollOut = controller.getLeftButton();
        Button rollIn = controller.getRightButton();
        Button intakePlacerUp = controller.getUpButton();
        Button intakePlacerDown = controller.getDownButton();
        intake.whenPressed(new IntakeCargo());
        rollOut.whileHeld(new MoveGenericSubsystem(intakeRoller, IntakeRoller.MAX_SPEED));
        rollIn.whileHeld(new MoveGenericSubsystem(intakeRoller, IntakeRoller.MIN_SPEED));
        intakePlacerUp.whileHeld(new MoveGenericSubsystem(intakePlacer, IntakePlacer.MAX_SPEED));
        intakePlacerDown.whileHeld(new MoveGenericSubsystem(intakePlacer, IntakePlacer.MAX_SPEED));
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}
