package frc.robot.commands.climbing;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

/**
 * Moves the robot to the next bar. <br>
 * Steps: <br>
 * 1. Moves the winch up to release the hooks from the bar.<br>
 * 2. Moves the placers downwards.<br>
 * 3. Extends the winch to it's full extent.<br>
 * 4. Raises the placers until they hit the next bar and go into a stall.<br>
 * 5. Closes the winch while keeping the placers next to the bar to move the robot to it.<br>
 * Must start the command <b>ONLY</b> when you are already on a bar.
 */
public class MoveToNextBar extends SequentialCommandGroup {

    public MoveToNextBar() {
        ClimberWinch winch = ClimberWinch.getInstance();
        ClimberPlacer leftPlacer = ClimberPlacer.getLeftInstance();
        ClimberPlacer rightPlacer = ClimberPlacer.getRightInstance();
        addCommands(
                new MoveGenericSubsystem(winch, ClimberWinch.UP_SPEED).withInterrupt(() -> winch.getEncoderPosition() <=
                        winch.ENCODER_RELEASE_BAR_POSITION.get()),
                new ParallelCommandGroup(
                        new MoveGenericSubsystem(leftPlacer, ClimberPlacer.MIN_SPEED).withInterrupt(leftPlacer::isDown),
                        new MoveGenericSubsystem(rightPlacer, ClimberPlacer.MIN_SPEED).withInterrupt(rightPlacer::isDown)
                ),
                new MoveGenericSubsystem(winch, ClimberWinch.UP_SPEED),
                new MoveBothPlacersToNextBar(),
                new HookNextBar(),
                new CloseTelescopic()
        );
    }
}
