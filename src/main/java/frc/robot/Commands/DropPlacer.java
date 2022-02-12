package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems.ClimberPlacer;

import java.util.function.Supplier;

public class DropPlacer extends MoveGenericSubsystem {

    private final ClimberPlacer placer;

    private static final RootNamespace rootNamespace = new RootNamespace("drop placers");
    public static final Supplier<Double> initialWaitTime = rootNamespace.addConstantDouble("initial wait time", 0);

    private double startTime;

    public DropPlacer(ClimberPlacer placer) {
        super(placer, placer.getDropSpeed());
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
        return Timer.getFPGATimestamp() - startTime > initialWaitTime.get();
    }
}
