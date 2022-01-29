package frc.robot.Subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

public class PlacerClimber extends MotoredGenericSubsystem {
    private WPI_TalonSRX rightPlacer;
    private WPI_TalonSRX leftPlacer;
    private static final double MIN_SPEED = -0.6;
    private static final double MAX_SPEED = 0.6;
    private static PlacerClimber instance;
    private DigitalInput upLimit;
    private DigitalInput downLimit;
    public DigitalInput Hook;


    public PlacerClimber(WPI_TalonSRX rightPlacer, WPI_TalonSRX leftPlacer, DigitalInput upLimit, DigitalInput downLimit,
                         DigitalInput Hook) {
        super(MIN_SPEED, MAX_SPEED, "placerClimber", rightPlacer, leftPlacer);
        this.leftPlacer = leftPlacer;
        this.rightPlacer = rightPlacer;
        this.upLimit = upLimit;
        this.downLimit = downLimit;
        this.Hook = Hook;
        leftPlacer.follow(rightPlacer);
        rightPlacer.setInverted(true);

    }

    public static PlacerClimber getInstance() {
        if (instance == null)
            instance = new PlacerClimber(new WPI_TalonSRX(-1), new WPI_TalonSRX(-1),
                    new DigitalInput(-1), new DigitalInput(-1), new DigitalInput(-1));
        return instance;

    }

    @Override
    public void apply(double speed) {
        super.apply(speed);
    }

    @Override
    public boolean canMove(double speed) {
        if (Hook.get())
            return false;
        if (speed > 0 && upLimit.get())
            return false;
        if (speed < 0 && downLimit.get())
            return false;
        return true;
    }

    @Override
    public void stop() {
        rightPlacer.stopMotor();

    }

}
