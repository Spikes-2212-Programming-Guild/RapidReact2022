package frc.robot.Commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems.IntakePlacer;

import java.util.ArrayList;

public class IntakeDefaultCommand extends MoveGenericSubsystem {

    public static final double SECONDS_BENCHMARK = 2;
    public static final int HITS_BENCHMARK = 3;

    private final IntakePlacer intakePlacer;

    private final ArrayList<Double> timestampList;
    private final Timer timer;

    private boolean hitThreeTimes;

    public IntakeDefaultCommand(IntakePlacer intakePlacer, double initialSpeed) {
        super(intakePlacer, initialSpeed);
        this.intakePlacer = intakePlacer;
        timer = new Timer();
        timestampList = new ArrayList<>();
        hitThreeTimes = false;
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void execute() {
        double speed = speedSupplier.get();
        if (hitThreeTimes)
            speed = speed / 2;
        if (intakePlacer.getShouldBeUp() && !intakePlacer.isUp()) {
            subsystem.move(speed);
        } else {
            subsystem.move(0);
        }
        configureHits();
    }

    private void configureHits() {
        if (timestampList.size() == HITS_BENCHMARK) {
            if (timestampList.get(HITS_BENCHMARK - 1) - timestampList.get(0) <= SECONDS_BENCHMARK)
                hitThreeTimes = true;
            else
                timestampList.remove(0);
        } else if (intakePlacer.isUp())
            timestampList.add(timer.get());
    }
}
