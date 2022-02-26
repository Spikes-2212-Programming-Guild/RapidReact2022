package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.RobotMap;
import frc.robot.commands.MoveToCargo;

/**
 * Controls the roller part of the intake subsystem.
 *
 * @see MotoredGenericSubsystem
 */
public class IntakeRoller extends MotoredGenericSubsystem {

    public static final double MAX_SPEED = 0.6;
    public static final double MIN_SPEED = -0.6;

    private static IntakeRoller instance;

    public static IntakeRoller getInstance() {
        if (instance == null) {
            instance = new IntakeRoller();
        }
        return instance;
    }

    private IntakeRoller() {
        super(MIN_SPEED, MAX_SPEED, "intake roller", new WPI_VictorSPX(RobotMap.CAN.INTAKE_ROLLER_VICTOR));
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("intake roller", new MoveGenericSubsystem(this, MIN_SPEED));
        rootNamespace.putBoolean("oriented to cargo", () -> MoveToCargo.getCargoX() <= 13 && MoveToCargo.getCargoX() >= 7);
    }
}
