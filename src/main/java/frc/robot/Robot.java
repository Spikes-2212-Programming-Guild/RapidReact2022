// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;
import frc.robot.commands.autonomous.*;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {

    private OI oi;
    private Drivetrain drivetrain;

    private RootNamespace rootNamespace;
    private SendableChooser<Command> autoChooser;

    @Override
    public void robotInit() {
        CameraServer.startAutomaticCapture();
        CvSink cvSink = CameraServer.getVideo();
        CvSource outputStream = CameraServer.putVideo("back camera", 720, 1280);

        getSubsystemInstances();
        configureDashboards();
        namespaceSetup();
        autoChooserSetup();
    }

    @Override
    public void robotPeriodic() {
        periodic();
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        rootNamespace.putBoolean("is in auto", false);
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
//        rootNamespace.putBoolean("is in auto", true);
//        drivetrain.resetPigeon();
//        autoChooser.getSelected().schedule();
//        autoChooser.close();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
//        rootNamespace.putBoolean("is in auto", false);
        drivetrain.resetPigeon();

//        DriveArcade driveArcade = new DriveArcade(drivetrain, oi::getRightY, oi::getLeftX);
//        drivetrain.setDefaultCommand(driveArcade);
        new SuperAutonomous().schedule();

    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }

    private void getSubsystemInstances() {
        drivetrain = Drivetrain.getInstance();
    }

    private void configureDashboards() {
        drivetrain.configureDashboard();
    }

    private void namespaceSetup() {
        rootNamespace = new RootNamespace("robot namespace");
        rootNamespace.putData("drive forward", new DriveArcade(drivetrain, 0.5, 0));
        rootNamespace.putData("drive backward", new DriveArcade(drivetrain, -0.5, 0));
        rootNamespace.putData("move to cargo", new MoveToCargoWithIntake(drivetrain, MoveToCargoWithIntake.CARGO_MOVE_VALUE));
        rootNamespace.putBoolean("is in auto", false);
        rootNamespace.putNumber("move to cargo source", MoveToCargoWithIntake::getCargoX);
        rootNamespace.putData("driveArcade", new DriveArcade(drivetrain, 0.6, 0));
    }

    private void autoChooserSetup() {
        autoChooser = new SendableChooser<>();
        autoChooser.addOption("super autonomous", new SuperAutonomous());
        rootNamespace.putData("auto chooser", autoChooser);
    }

    private void periodic() {
        drivetrain.periodic();
        rootNamespace.update();
    }
}
