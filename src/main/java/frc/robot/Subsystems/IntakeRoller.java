package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**
 * Controls the roller part of the intake subsystem.
 *
 * @see MotoredGenericSubsystem
 */
public class IntakeRoller extends MotoredGenericSubsystem {

    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.5;
    public static final double SPEED = 0.4;

    private static Transfer transfer = Transfer.getInstance();

    private static IntakeRoller instance;

    public static IntakeRoller getInstance() {
        if (instance == null) {
            instance = new IntakeRoller();
        }
        return instance;
    }

    private IntakeRoller() {
        super(MIN_SPEED, MAX_SPEED, "intake roller", new WPI_VictorSPX(RobotMap.CAN.INTAKE_ROLLER));
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("intake roller", new MoveGenericSubsystem(this, SPEED));
    }
}
