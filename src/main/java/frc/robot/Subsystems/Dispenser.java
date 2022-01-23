package frc.robot.Subsystems;

import com.spikes2212.util.Limelight;

public class Dispenser {

    private Limelight limelight;

    private Dispenser() {
        this.limelight = new Limelight();
    }

    public static Dispenser getInstance() {
        return null;
    }

    public boolean isOnTarget() {
        return limelight.isOnTarget();
    }

    public int getLimelightPipeline() {
        return limelight.getPipeline();
    }

    public void setLimelightPipeline(int pipeline) {
        limelight.setPipeline(pipeline);
    }

}
