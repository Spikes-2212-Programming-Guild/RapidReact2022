package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;


/**
 * Controls the climber winch which controls the height of telescopic arm.
 */
public class ClimberWinch extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;

    /**
     * Which magnet the hall effect sensor is attached to.
     */
    private Level magnetLevel;
    private static ClimberWinch instance;

    /**
     * Whether the arm reached her max height or minimum height.
     */
    private final DigitalInput hallEffect;

    enum Level {UPPER, LOWER, MIDDLE}

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(new WPI_VictorSPX(RobotMap.CAN.WINCH_VICTOR_LEFT),
                    new WPI_VictorSPX(RobotMap.CAN.WINCH_VICTOR_RIGHT));
        }
        return instance;
    }

    private ClimberWinch(WPI_VictorSPX leftWinch, WPI_VictorSPX rightWinch) {
        super(MIN_SPEED, MAX_SPEED, "climber winch", leftWinch, rightWinch);
        this.magnetLevel = Level.LOWER;
        this.hallEffect = new DigitalInput(RobotMap.DIO.WINCH_HALL_EFFECT);
    }

    @Override
    public boolean canMove(double speed) {
        return !(magnetLevel == Level.UPPER && speed > 0 || magnetLevel == Level.LOWER && speed < 0);
    }

    @Override
    public void periodic() {
        super.periodic();
        double speed = getSpeed();
        if (hallEffect.get()) {
            if (speed > 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.UPPER;
            if (speed < 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.LOWER;
        } else
            magnetLevel = Level.MIDDLE;
    }
}
