package frc.robot.Commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.Subsystems.Drivetrain;

import java.util.function.Supplier;

/**
 * Drives the robot forward until it hits the hub. Robot needs to be pre aligned to the hub.
 */
public class DriveUntilHitHub extends DriveArcade {

    RootNamespace rootNamespace = new RootNamespace("drive until hit hub");

    /**
     * A value between the stall current and running current of the motors
     */
    private final Supplier<Double> STALL_CURRENT = rootNamespace.addConstantDouble("stall current", 0);

    private final Drivetrain drivetrain;

    public DriveUntilHitHub(Drivetrain drivetrain, Supplier<Double> movementSpeed) {
        super(drivetrain, movementSpeed, () -> 0.0);
        this.drivetrain = drivetrain;
    }

    public DriveUntilHitHub(Drivetrain drivetrain, double movementSpeed, double stallCurrent) {
        this(drivetrain, () -> movementSpeed);
    }

    @Override
    public boolean isFinished() {
        return drivetrain.getRightTalonCurrent() > STALL_CURRENT.get() &&
                drivetrain.getLeftTalonCurrent() > STALL_CURRENT.get();
    }
}
