// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Commands.IntakeCargo;
import frc.robot.Subsystems.Transfer;
import frc.robot.Subsystems.IntakeToTransfer;
import frc.robot.Subsystems.IntakePlacer;
import frc.robot.Subsystems.IntakeRoller;
import frc.robot.Subsystems.Drivetrain;

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
    private RootNamespace rootNamespace;

    @Override
    public void robotInit() {
        oi = new OI();
        drivetrain = Drivetrain.getInstance();
        drivetrain.configureDashboard();
        intakePlacer = IntakePlacer.getInstance();
        intakeRoller = IntakeRoller.getInstance();
        intakeToTransfer = IntakeToTransfer.getInstance();
        transfer = Transfer.getInstance();
        intakePlacer.configureDashboard();
        intakeRoller.configureDashboard();
        intakeToTransfer.configureDashboard();
        transfer.configureDashboard();
        rootNamespace = new RootNamespace("Robot Namespace");
        rootNamespace.putData("intake cargo", new IntakeCargo());
        intakePlacer.setDefaultCommand(new MoveGenericSubsystem(intakePlacer, IntakePlacer.IDLE_SPEED) {
            @Override
            public void execute() {
                if (intakePlacer.getShouldBeUp() && !intakePlacer.isUp()) {
                    subsystem.move(speedSupplier.get());
                } else {
                    subsystem.move(0);
                }
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        });
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
        rootNamespace.update();
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
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
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
