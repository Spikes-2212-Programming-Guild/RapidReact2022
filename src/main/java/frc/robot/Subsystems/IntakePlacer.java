package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * Controls the position of the {@code IntakeRoller}.
 *
 * @see MotoredGenericSubsystem
 */
public class IntakePlacer extends MotoredGenericSubsystem {

    public static final double MAX_SPEED = 0.5;
    public static final double MIN_SPEED = -0.3;

    private static IntakePlacer instance;

    /**
     * The upper limit of the subsystem. When it is pressed, the intake system is vertical.
     */
    private final DigitalInput upperLimit;

    /**
     * The lower limit of the subsystem. When it is pressed, the intake system is horizontal.
     */
    private final DigitalInput lowerLimit;

    public static IntakePlacer getInstance() {
        if (instance == null) {
            instance = new IntakePlacer(new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER_VICTOR));
        }
        return instance;
    }

    public final WPI_VictorSPX wpi_victorSPX;

    private IntakePlacer(WPI_VictorSPX wpi_victorSPX) {
        super(MIN_SPEED, MAX_SPEED, "intake placer", wpi_victorSPX);
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_LOWER_LIMIT);
        wpi_victorSPX.setNeutralMode(NeutralMode.Brake);
        this.wpi_victorSPX = wpi_victorSPX;
    }

    /**
     * Whether the intake subsystem can move up/down.
     * <p>For positive values it checks if the subsystem can move up, while for negative values
     * it checks if it can move down.</p>
     *
     * @return whether the intake subsystem can move up/down.
     */
    @Override
    public boolean canMove(double speed) {
        return (!(isDown() && speed < 0) && !(isUp() && speed > 0)) || Robot.shouldClose;
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("move intake down", new MoveGenericSubsystem(this, MIN_SPEED).andThen(
                new InstantCommand(() -> Robot.shouldClose = false)
        ));
        rootNamespace.putData("move intake up", new MoveGenericSubsystem(this, MAX_SPEED).andThen(
                new InstantCommand(() -> Robot.shouldClose = true)
        ));
    }

    public boolean isUp() {
        return upperLimit.get();
    }

    public boolean isDown() {
        return lowerLimit.get();
    }
}
