package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Drivetrain extends TankDrivetrain {


    private Drivetrain(MotorController left, MotorController right) {
        super(left, right);
    }

    public WPI_TalonSRX getRightTalon() {
        return null;
    }

    public WPI_TalonSRX getLeftTalon() {
        return null;
    }
}
