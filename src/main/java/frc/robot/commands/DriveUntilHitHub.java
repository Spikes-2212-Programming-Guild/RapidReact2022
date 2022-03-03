package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

/**
 * Drives the robot forward until it hits the hub. Robot needs to be pre aligned to the hub.
 */
public class DriveUntilHitHub extends DriveArcade {

    private static final RootNamespace rootNamespace = new RootNamespace("drive until hit hub");

    /**
     * A value between the stall current and running current of the motors
     */
    private static final Supplier<Double> STALL_CURRENT
            = rootNamespace.addConstantDouble("stall current", 19);
    private static final Supplier<Double> MOVEMENT_SPEED
            = rootNamespace.addConstantDouble("movement speed", -0.7);
    private static final Supplier<Double> GET_OVER_STALL_TIMEOUT
            = rootNamespace.addConstantDouble("get over stall timeout", 0.2);

    public DriveUntilHitHub(Drivetrain drivetrain) {
        super(drivetrain, MOVEMENT_SPEED, () -> 0.0);
    }

    @Override
    public void initialize() {
        new DriveArcade(tankDrivetrain, MOVEMENT_SPEED, () -> 0.0) {
            @Override
            public void end(boolean interrupted) {
            }
        }.withTimeout(GET_OVER_STALL_TIMEOUT.get());
    }

    @Override
    public boolean isFinished() {
        Drivetrain drivetrain = (Drivetrain) tankDrivetrain;
        return Math.abs(drivetrain.getRightTalon().getStatorCurrent()) > STALL_CURRENT.get() &&
               Math.abs(drivetrain.getLeftTalon().getStatorCurrent()) > STALL_CURRENT.get();
    }
}
