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

    // TODO: 1/22/2022
    public int getLimelightPipeline() {
        return 0;
    }

    public void setLimelightPipeline(int pipeline) {
        limelight.setPipeline(pipeline);
    }
}
