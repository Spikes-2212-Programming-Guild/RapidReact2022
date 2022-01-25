package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;


public class Climber extends MotoredGenericSubsystem{
    private WPI_VictorSPX rightVictor;
    private WPI_VictorSPX leftVictor;
    private static final double MINSPEED=-0.6;
    private static final double MAXSPEED=-0.6;
    private static boolean counted=false;
    private static int magnets=0;
    private static DigitalInput digitalInput;


    public Climber(WPI_VictorSPX rightVictor, WPI_VictorSPX leftVictor, DigitalInput digitalInput){
        super(MINSPEED,MAXSPEED,"climber",rightVictor,leftVictor);
        this.rightVictor=rightVictor;
        this.leftVictor=leftVictor;
        leftVictor.follow(rightVictor);
        this.digitalInput=digitalInput;
    }

    @Override
    public void apply(double speed) {
        rightVictor.set(speed);
        if(counted&&digitalInput.get())
            counted=false;
        }

    @Override
    public boolean canMove(double speed) {
        if(!counted&&digitalInput.get()){
            if(speed>0)
                magnets++;
            else
                magnets--;
        }
        if(magnets==2&&speed<0)
            return true;
        if(magnets==0&&speed>0)
            return true;
        return false;

    }

    @Override
    public void stop() {
        rightVictor.stopMotor();
}

}
