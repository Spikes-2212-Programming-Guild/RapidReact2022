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
    private DigitalInput upRightLimit;
    private DigitalInput downRightLimit;
    private DigitalInput upLeftLimit;
    private DigitalInput downLeftLimit;
    public DigitalInput rightHook;
    public DigitalInput leftHook;

    public PlacerClimber(WPI_TalonSRX rightPlacer, WPI_TalonSRX leftPlacer, DigitalInput upRightLimit,
                         DigitalInput downRightLimit, DigitalInput downLeftLimit, DigitalInput upLeftLimit,
                         DigitalInput rightHook, DigitalInput leftHook) {
        super(MIN_SPEED, MAX_SPEED, "placerClimber", rightPlacer, leftPlacer);
        this.leftPlacer = leftPlacer;
        this.rightPlacer = rightPlacer;
        this.upRightLimit = upRightLimit;
        this.downRightLimit = downRightLimit;
        this.rightHook = rightHook;
        this.upLeftLimit = upLeftLimit;
        this.downLeftLimit = downLeftLimit;
        this.leftHook = leftHook;
        leftPlacer.follow(rightPlacer);
        rightPlacer.setInverted(true);

    }

    public static PlacerClimber getInstance() {
        if (instance == null)
            instance = new PlacerClimber(new WPI_TalonSRX(-1), new WPI_TalonSRX(-1),
                    new DigitalInput(-1), new DigitalInput(-1), new DigitalInput(-1),
                    new DigitalInput(-1), new DigitalInput(-1), new DigitalInput(-1));
        return instance;

    }

    @Override
    public void apply(double speed) {
        super.apply(speed);
    }

    @Override
    public boolean canMove(double speed) {
        if (leftHook.get() || rightHook.get())
            return false;
        if (speed > 0 && (upRightLimit.get() || upLeftLimit.get()))
            return false;
        if (speed < 0 && (downRightLimit.get() || downLeftLimit.get()))
            return false;
        return true;
    }

    @Override
    public void stop() {
        rightPlacer.stopMotor();

    }

}
