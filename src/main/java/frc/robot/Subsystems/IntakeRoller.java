package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Controls the roller part of the intake subsystem.
 *
 * @author Ofri Rosenbaum
 * @see MotoredGenericSubsystem
 */
public class IntakeRoller extends MotoredGenericSubsystem {

    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.5;

    private static IntakeRoller instance;

    /**
     * The limit at the start of the transfer subsystem. When it is pressed, there is a cargo in the transfer subsystem.
     */
    private DigitalInput limit;

    public static IntakeRoller getInstance() {
        if (instance == null) {
            instance = new IntakeRoller();
        }
        return instance;
    }

    private IntakeRoller() {
        super(MIN_SPEED, MAX_SPEED, "intake roller", new WPI_VictorSPX(RobotMap.CAN.INTAKE_ROLLER));
        limit = new DigitalInput(RobotMap.DIO.INTAKE_ROLLER_LIMIT);
    }

    /**
     * Whether the intake subsystem can intake a cargo.
     *
     * @return whether the intake subsystem can move up/down.
     */
    @Override
    public boolean canMove(double speed) {
        return !limit.get();
    }
}
