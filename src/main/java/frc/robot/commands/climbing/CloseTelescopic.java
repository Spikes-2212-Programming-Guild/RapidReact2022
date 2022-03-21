package frc.robot.commands.climbing;

import com.revrobotics.CANSparkMax.IdleMode;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberPlacer;
import frc.robot.subsystems.ClimberWinch;

public class CloseTelescopic extends ParallelCommandGroup {

    private double rightEncoderPos;
    private double leftEncoderPos;

    /**
     * steps:
     * 1. sets the placers to brake<br>
     * 2. moves the robot up until the static meets the bar on the upper part<br>
     * 3. sets the placers to coast so the bar will slide on the static<br>
     * 4. moves the robot up<br>
     * 5. sets the placers and the winch to brake so the robot won't fall<br>
     */
    public CloseTelescopic() {
        ClimberWinch winch = ClimberWinch.getInstance();
        ClimberPlacer left = ClimberPlacer.getLeftInstance();
        ClimberPlacer right = ClimberPlacer.getRightInstance();
//        addRequirements(winch, ClimberPlacer.getLeftInstance(), ClimberPlacer.getRightInstance());
        addCommands(
                new SequentialCommandGroup(
                        new SetPlacersIdleMode(IdleMode.kBrake),
                        new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED).withInterrupt(() -> winch.getEncoderPosition()
                                <= winch.ENCODER_STATIC_MEET_BAR_POSITION.get()),
                        new SetPlacersIdleMode(IdleMode.kCoast),
                        new InstantCommand(() -> winch.setIdleMode(IdleMode.kCoast)),
                        new MoveGenericSubsystem(winch, ClimberWinch.DOWN_SPEED),
                        new SetPlacersIdleMode(IdleMode.kBrake),
                        new InstantCommand(() -> winch.setIdleMode(IdleMode.kBrake))
                ), new MoveGenericSubsystemWithPID(right, () -> rightEncoderPos, right::getEncoderPosition,
                        new PIDSettings(0.002, 0, 999)),
                new MoveGenericSubsystemWithPID(left, () -> leftEncoderPos, left::getEncoderPosition,
                        new PIDSettings(0.002, 0, 999))
        );
    }

    @Override
    public void initialize() {
        rightEncoderPos = ClimberPlacer.getRightInstance().getEncoderPosition();
        leftEncoderPos = ClimberPlacer.getLeftInstance().getEncoderPosition();
    }
}
