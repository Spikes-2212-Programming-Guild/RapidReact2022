package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the position of the {@code IntakeRoller}.
 *
 * @see MotoredGenericSubsystem
 */
public class IntakePlacer extends MotoredGenericSubsystem {

    public static final double MAX_SPEED = 0.5;
    public static final double MIN_SPEED = -0.1;
    public static final double SERVO_START_ANGLE = 110;
    public static final double SERVO_TARGET_ANGLE = 0;

    private static IntakePlacer instance;

    /**
     * Controls the mechanical brake that keeps this subsystem vertical.
     */
    private final Servo servo;

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
            instance = new IntakePlacer();
        }
        return instance;
    }

    private IntakePlacer() {
        super(MIN_SPEED, MAX_SPEED, "intake placer", new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER_VICTOR));
        upperLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_UPPER_LIMIT);
        lowerLimit = new DigitalInput(RobotMap.DIO.INTAKE_PLACER_LOWER_LIMIT);
        servo = new Servo(RobotMap.PWM.INTAKE_PLACER_SERVO);
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
        rootNamespace.putBoolean("can move", !(isDown() && speed < 0) && !(isUp() && speed > 0));
        return !(speed < 0 && isDown()) && !(speed > 0 && isUp());
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("move intake down", new MoveGenericSubsystem(this, MIN_SPEED));
        rootNamespace.putData("move intake up", new MoveGenericSubsystem(this, MAX_SPEED));
        rootNamespace.putData("move servo", new InstantCommand(() -> setServoAngle(SERVO_TARGET_ANGLE)));
        rootNamespace.putData("reset servo", new InstantCommand(() -> setServoAngle(SERVO_START_ANGLE)));
    }

    public void setServoAngle(double angle) {
        servo.setAngle(angle);
    }

    public boolean isUp() {
        return upperLimit.get();
    }

    public boolean isDown() {
        return lowerLimit.get();
    }
}
