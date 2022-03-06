package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.ClimberPlacer;

import java.util.function.Supplier;

public class MovePlacerToNextBar extends MoveGenericSubsystem {

    private final ClimberPlacer placer;

    private static final RootNamespace rootNamespace = new RootNamespace("drop placers");
    public static final Supplier<Double> INITIAL_WAIT_TIME = rootNamespace.addConstantDouble("initial wait time", 0.25);

    private double startTime;

    public MovePlacerToNextBar(ClimberPlacer placer) {
        super(placer, placer.RAISE_SPEED);
        this.placer = placer;
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || (placer.isStalling() && initialTimePassed());
    }

    private boolean initialTimePassed() {
        return Timer.getFPGATimestamp() - startTime > INITIAL_WAIT_TIME.get();
    }
}
