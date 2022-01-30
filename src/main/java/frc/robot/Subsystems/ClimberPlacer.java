package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class ClimberPlacer extends MotoredGenericSubsystem {

    public static ClimberPlacer getInstance() {
        return null;
    }

    public ClimberPlacer(String namespaceName, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
    }

    public WPI_TalonSRX getLeftTalon() {
        return null;
    }

    public WPI_TalonSRX getRightTalon() {
        return null;
    }
}
