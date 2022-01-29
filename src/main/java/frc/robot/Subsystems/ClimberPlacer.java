package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;

/**This is a motor controlled subsystem of the dynamic part of the climber.*/
public class ClimberPlacer extends MotoredGenericSubsystem {

    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    private static ClimberPlacer instance;
    private final DigitalInput upLimit;
    private final DigitalInput downLimit;
    public final DigitalInput hook;

    public static ClimberPlacer getInstance() {
        if (instance == null)
            instance = new ClimberPlacer(new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_RIGHT), new WPI_TalonSRX(RobotMap.CAN.PLACER_TALON_LEFT));
        return instance;
    }

    public ClimberPlacer(WPI_TalonSRX rightPlacer, WPI_TalonSRX leftPlacer) {
        super(MIN_SPEED, MAX_SPEED, "climberplacer", rightPlacer, leftPlacer);
        this.upLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_UP);
        this.downLimit = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_DOWN);
        this.hook = new DigitalInput(RobotMap.DIO.PLACER_LIMIT_HOOK);
    }

    @Override
    public boolean canMove(double speed) {
        if (hook.get())
            return false;
        if (speed > 0 && upLimit.get())
            return false;
        if (speed < 0 && downLimit.get())
            return false;
        return true;
    }

}
