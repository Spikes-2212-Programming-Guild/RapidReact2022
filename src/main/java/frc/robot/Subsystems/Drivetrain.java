package frc.robot.Subsystems;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Drivetrain extends TankDrivetrain {

    public Drivetrain(MotorController left, MotorController right) {
        super(left, right);
    }

    public double getLeftTalon1Current() {
        return 0;
    }

    public double getRightTalon1Current() {
        return 0;
    }
}
