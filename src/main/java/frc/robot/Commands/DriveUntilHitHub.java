package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import frc.robot.Subsystems.Drivetrain;

import java.util.function.Supplier;

/**
 * drive the robot forward until it hits the hub. Robot needs to be pre aligned to the hub.
 */
public class DriveUntilHitHub extends DriveArcade {

    // A value lower than the current the motors get when stalling and higher than the current the motors get when
    // running
    private final Supplier<Double> STALL_CURRENT;

    private final Drivetrain drivetrain;

    public DriveUntilHitHub(Drivetrain drivetrain, Supplier<Double> movementSpeed, Supplier<Double> stallCurrent) {
        super(drivetrain, movementSpeed, () -> 0.0);
        this.drivetrain = drivetrain;
        this.STALL_CURRENT = stallCurrent;
    }

    public DriveUntilHitHub(Drivetrain drivetrain, double movementSpeed, double stallCurrent) {
        this(drivetrain, () -> movementSpeed, () -> stallCurrent);
    }

    @Override
    public boolean isFinished() {
        return drivetrain.getRightTalon1Current() > STALL_CURRENT.get() &&
                drivetrain.getLeftTalon1Current() > STALL_CURRENT.get();
    }
}
