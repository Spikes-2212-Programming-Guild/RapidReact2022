package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakePlacer;

import javax.naming.InitialContext;
import java.util.ArrayList;

/**
 * A command which slowly raises the {@link IntakePlacer}. It's supposed to be used as the default command for the
 * subsystem in order to stop it from falling.
 *
 * If the upper limit of the {@link IntakePlacer} is pressed multiple times in a short timespan, the speed of the
 * {@link IntakePlacer} is cut in half in order to stop it from hitting the robot.
 */
public class IntakeDefaultCommand extends CommandBase {

    /**
     * The timespan in which the {@link IntakePlacer} needs to hit the limit a certain amount of times.
     */
    private static final double SECONDS_BENCHMARK = 2;

    /**
     * The amount of times the limit needs to be hit in a certain timespan in order to cut the speed in half.
     */
    private static final int HITS_BENCHMARK = 3;

    /**
     * List of the last {@value HITS_BENCHMARK} timestamps in which the upper limit of the {@link IntakePlacer} was hit.
     */
    private final ArrayList<Double> timestamps;
    private final Timer timer;

    private boolean hitEnoughTimes;

    private final IntakePlacer intakePlacer;

    private double speed;

    public IntakeDefaultCommand() {
        this.intakePlacer = IntakePlacer.getInstance();
        timer = new Timer();
        timestamps = new ArrayList<>();
        hitEnoughTimes = false;
    }

    /**
     * starts the timer
     */
    @Override
    public void initialize() {
        timer.start();
        speed = IntakePlacer.INITIAL_IDLE_SPEED;
    }


    @Override
    public void execute() {
        //If the condition of the limit being hit multiple times in a short timespan is met, the speed is cut in half.
        if (hitEnoughTimes)
            speed = speed / 2;
        if (intakePlacer.getShouldBeUp() && !intakePlacer.isUp()) {
            intakePlacer.move(speed);
        } else {
            intakePlacer.move(0);
        }
        configureHits();
    }

    /**
     * Updates the list of timespans to include the latest {@value HITS_BENCHMARK} timespans which the limit was hit.
     * If the difference between the first timespan and the last is less than {@value SECONDS_BENCHMARK}, changes the
     * {@code hitEnoughTimes} flag to true.
     */
    private void configureHits() {
        if (timestamps.size() == HITS_BENCHMARK) {
            if (timestamps.get(HITS_BENCHMARK - 1) - timestamps.get(0) <= SECONDS_BENCHMARK)
                hitEnoughTimes = true;
            else
                timestamps.remove(0);
        } else if (intakePlacer.isUp())
            timestamps.add(timer.get());
    }
}
