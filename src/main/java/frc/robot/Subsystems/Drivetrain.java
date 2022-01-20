package frc.robot.Subsystems;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.util.Color;

public class Drivetrain extends TankDrivetrain {

    private static Drivetrain instance;

    public Drivetrain(MotorController left, MotorController right) {
        super(left, right);
    }

    public static Drivetrain getInstance() {
        return instance;
    }

    public Color getLeftColor(){
        return Color.kWhite;
    }

    public Color getRightColor(){
        return Color.kWhite;
    }
}
