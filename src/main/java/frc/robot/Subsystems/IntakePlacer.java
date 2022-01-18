package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

public class IntakePlacer extends MotoredGenericSubsystem {

    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.3;

    private static IntakePlacer instance;
    private DigitalInput upperLimit;
    private DigitalInput lowerLimit;

    public static IntakePlacer getInstance() {
        if (instance == null) {
            instance = new IntakePlacer();
        }
        return instance;
    }

    private IntakePlacer() {
        super(MIN_SPEED, MAX_SPEED, "intake", new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER));
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_LOWER_LIMIT);
    }
}
