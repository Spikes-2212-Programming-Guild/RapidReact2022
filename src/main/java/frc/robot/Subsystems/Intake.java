package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.RobotMap;

public class Intake extends GenericSubsystem {

    private static final double MAX_SPEED = 0.5;
    private static final double MIN_SPEED = -0.5;

    private static Intake instance;
    private WPI_VictorSPX placer;
    private WPI_VictorSPX roller;

    public static final RootNamespace intakeNamespace = new RootNamespace("intake");

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private Intake() {
        super(MIN_SPEED, MAX_SPEED);
        placer = new WPI_VictorSPX(RobotMap.CAN.INTAKE_PLACER);
        roller = new WPI_VictorSPX(RobotMap.CAN.INTAKE_ROLLER);
    }

    @Override
    protected void apply(double speed) {
        placer.set(speed);
        roller.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        placer.stopMotor();
        roller.stopMotor();
    }

    @Override
    public void periodic() {
    intakeNamespace.update();
    }
}
