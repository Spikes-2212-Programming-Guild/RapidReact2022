package frc.robot.utils;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class BustedMotorControllerGroup extends MotorControllerGroup {

    private final double CORRECTION;

    public BustedMotorControllerGroup(double correction, MotorController motorController, MotorController... motorControllers) {
        super(motorController, motorControllers);

        this.CORRECTION = correction;
    }

    @Override
    public void set(double speed) {
        super.set(speed * CORRECTION);
    }
}
