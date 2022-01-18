package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import frc.robot.RobotMap;

public class IntakeRoller extends MotoredGenericSubsystem {

    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.5;

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
}
