package frc.robot.utils;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import java.util.function.Supplier;

/**
 * An extension of the MotorControllerGroup class that has the ability to add a correction to its speed.
 * Best used to correct deviation in the drivetrain
 */
public class BustedMotorControllerGroup extends MotorControllerGroup {

    private Supplier<Double> correction;

    public BustedMotorControllerGroup(double correction, MotorController motorController, MotorController... motorControllers) {
        this(() -> correction, motorController, motorControllers);
    }

    public BustedMotorControllerGroup(Supplier<Double> correction, MotorController motorController, MotorController... motorControllers) {
        super(motorController, motorControllers);

        this.correction = correction;
    }

    @Override
    public void set(double speed) {
        super.set(speed * correction.get());
    }
}
