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
    private IntakeToTransfer intakeToTransfer;
    private Transfer transfer;
    private IntakePlacer intakePlacer;
    private IntakeRoller intakeRoller;
    private ClimberWinch climberWinch;

    private RootNamespace rootNamespace;

    @Override
    public void robotInit() {
        oi = new OI();

        CameraServer.startAutomaticCapture();
        CvSink cvSink = CameraServer.getVideo();
        CvSource outputStream = CameraServer.putVideo("back camera", 720, 1280);

        getSubsystemInstances();
        configureDashboards();
        namespaceSetup();
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
        rootNamespace.putBoolean("is in auto", true);
        drivetrain.resetPigeon();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        rootNamespace.putBoolean("is in auto", false);
        drivetrain.resetPigeon();
        climberWinch.resetEncoder();
        intakePlacer.setServoAngle(IntakePlacer.SERVO_START_ANGLE.get());

        DriveArcade driveArcade = new DriveArcade(drivetrain, oi::getRightY, oi::getLeftX);
        drivetrain.setDefaultCommand(driveArcade);
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
        intakePlacer = IntakePlacer.getInstance();
        intakeRoller = IntakeRoller.getInstance();
        intakeToTransfer = IntakeToTransfer.getInstance();
        transfer = Transfer.getInstance();
        climberWinch = ClimberWinch.getInstance();
    }

    private void configureDashboards() {
        drivetrain.configureDashboard();
        intakePlacer.configureDashboard();
        intakeRoller.configureDashboard();
        intakeToTransfer.configureDashboard();
        transfer.configureDashboard();
        climberWinch.configureDashboard();
    }

    private void namespaceSetup() {
        rootNamespace = new RootNamespace("robot namespace");
        rootNamespace.putData("intake cargo", new IntakeCargo(false));
        rootNamespace.putData("release cargo", new ReleaseCargo());
        rootNamespace.putData("drive forward", new DriveArcade(drivetrain, 0.5, 0));
        rootNamespace.putData("drive backward", new DriveArcade(drivetrain, -0.5, 0));
        rootNamespace.putData("move to cargo", new MoveToCargo(drivetrain, MoveToCargo.CARGO_MOVE_VALUE));
        rootNamespace.putBoolean("is in auto", false);
        rootNamespace.putNumber("Move To Cargo Source", MoveToCargo::getCargoX);
    }

    private void periodic() {
        drivetrain.periodic();
        intakePlacer.periodic();
        intakeRoller.periodic();
        intakeToTransfer.periodic();
        transfer.periodic();
        climberWinch.periodic();
        rootNamespace.update();
    }
}
