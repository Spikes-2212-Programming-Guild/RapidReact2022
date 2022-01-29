package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Transfer extends MotoredGenericSubsystem {

    public static Transfer getInstance() {
        return null;
    }

    private Transfer(String namespaceName, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
    }

    public boolean getStrapEntranceSensor() {
        return false;
    }
}
