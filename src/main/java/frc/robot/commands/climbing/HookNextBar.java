package frc.robot.commands.climbing;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

/**
 * Hooks the robot to the bar while keeping the placers in place until the bar meets the upper part of the static
 */
public class HookNextBar extends ParallelCommandGroup {

    private final ClimberWinch winch;

    public HookNextBar() {
        winch = ClimberWinch.getInstance();
        addCommands(
                new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED),
                new PlacerToBarPID(ClimberPlacer.getLeftInstance()),
                new PlacerToBarPID(ClimberPlacer.getRightInstance())
        );
    }

    @Override
    public boolean isFinished() {
        return winch.getEncoderPosition() <= winch.ENCODER_STATIC_MEET_BAR_POSITION.get();
    }
}
