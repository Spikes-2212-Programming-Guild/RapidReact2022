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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;
import frc.robot.commands.autonomous.*;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
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

        drivetrain = Drivetrain.getInstance();
        intakePlacer = IntakePlacer.getInstance();
        intakeRoller = IntakeRoller.getInstance();
        intakeToTransfer = IntakeToTransfer.getInstance();
        transfer = Transfer.getInstance();
        climberWinch = ClimberWinch.getInstance();

        drivetrain.configureDashboard();
        intakePlacer.configureDashboard();
        intakeRoller.configureDashboard();
        intakeToTransfer.configureDashboard();
        transfer.configureDashboard();
        climberWinch.configureDashboard();

        rootNamespace = new RootNamespace("robot namespace");
        rootNamespace.putData("intake cargo", new IntakeCargo(false));
        rootNamespace.putData("release cargo", new ReleaseCargo());
        rootNamespace.putData("drive forward", new DriveArcade(drivetrain, 0.5, 0));
        rootNamespace.putData("drive backward", new DriveArcade(drivetrain, -0.5, 0));
        rootNamespace.putData("move to cargo", new MoveToCargo(drivetrain, MoveToCargo.CARGO_MOVE_VALUE));
        rootNamespace.putBoolean("is in auto", false);
        rootNamespace.putNumber("Move To Cargo Source", MoveToCargo::getCargoX);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        drivetrain.periodic();
        intakePlacer.periodic();
        intakeRoller.periodic();
        intakeToTransfer.periodic();
        transfer.periodic();
        climberWinch.periodic();

        rootNamespace.update();

        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
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
//        new GyroAutonomous().schedule();
        new YeetAndRetreat().schedule();
//        new SimpleSix().schedule();
    }

    /**6
     * This function is called periodically during autonomous.
     */
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

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
