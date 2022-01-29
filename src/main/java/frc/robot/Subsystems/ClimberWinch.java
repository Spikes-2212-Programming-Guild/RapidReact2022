package frc.robot.Subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**This is a motor controlled subsystem of the static part of the climber.*/
public class ClimberWinch extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    /**This variable is indicating which magnet the hall effect sensor is attached to.*/
    private Level magnetLevel;
    private static ClimberWinch instance;
    /**This variable represents a sensor that detect whether the arm reached her max height or minimum height. */
    private final DigitalInput hallEffect;

    enum Level {UPPER, LOWER, MIDDLE}

    public static ClimberWinch getInstance() {
        if (instance == null) {
            instance = new ClimberWinch(new WPI_VictorSPX(RobotMap.CAN.WINCH_VICTOR_LEFT),
                    new WPI_VictorSPX(RobotMap.CAN.WINCH_VICTOR_RIGHT));
        }
        return instance;
    }

    public ClimberWinch(WPI_VictorSPX rightWinch, WPI_VictorSPX leftWinch) {
        super(MIN_SPEED, MAX_SPEED, "climberwinch", rightWinch, leftWinch);
        this.magnetLevel=Level.LOWER;
        this.hallEffect = new DigitalInput(RobotMap.DIO.WINCH_HALL_EFFECT);
    }

    @Override
    public boolean canMove(double speed) {
        if (magnetLevel == Level.UPPER && speed > 0 || magnetLevel == Level.LOWER && speed < 0)
            return false;
        return true;
    }

    public void periodic(double speed) {
        if (hallEffect.get()) {
            if (speed > 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.UPPER;
            if (speed < 0 && magnetLevel == Level.MIDDLE)
                magnetLevel = Level.LOWER;
        } else
            magnetLevel = Level.MIDDLE;
    }
}
