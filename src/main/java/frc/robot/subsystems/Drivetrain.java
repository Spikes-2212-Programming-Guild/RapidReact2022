package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.util.BustedMotorControllerGroup;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain extends TankDrivetrain {
    private static Drivetrain instance;

    public WPI_TalonSRX talon;
    public ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    public PIDSettings gyroPIDSettings = new PIDSettings(0.017, 0.0028, 0, 5, 0.1);
    public PIDSettings cameraPIDSettings = new PIDSettings(0.0055, 2, 999);

    public static Drivetrain getInstance() {
        if (instance == null) {
            WPI_TalonSRX talon1 = new WPI_TalonSRX(1);
            instance = new Drivetrain(
                    new BustedMotorControllerGroup(1, talon1, new WPI_TalonSRX(3)),
                    new BustedMotorControllerGroup(0.85, new WPI_VictorSPX(2), new WPI_VictorSPX(4))

            );
            instance.talon = talon1;
        }
        return instance;
    }

    public Drivetrain(String namespaceName, MotorController left, MotorController right) {
        super(namespaceName, left, right);

    }

    public Drivetrain(MotorController left, MotorController right) {
        super(left, right);
    }

    public void resetPigeon() {
        gyro.reset();
    }
}
