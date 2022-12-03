// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.systems.climber.commands.autoFunctions.StopAutoClimb;
import frc.robot.systems.climber.commands.autoFunctions.autoClimbCommands.SnapAngle;
import frc.robot.systems.drivetrain.commands.DefaultDriveCommand;
import frc.robot.systems.drivetrain.commands.ResetGyro;
import frc.robot.systems.drivetrain.commands.SwerveAutoCommand;
import frc.robot.systems.drivetrain.config.DriverConfig;
import frc.robot.systems.turret.commands.StopTargeting;
import frc.robot.systems.turret.commands.Target;
import frc.robot.common.AutoFactory;
import frc.robot.common.AutoFactory.Autos;
import frc.robot.common.dashboard.ShuffleboardHelper;
import frc.robot.common.utilities.DriveInput;
import frc.robot.common.utilities.GunnerRumble;
import frc.robot.common.utilities.TriggerSupplier;
import io.github.oblarg.oblog.Logger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private final RobotSystems systems;
    private SendableChooser<Autos> m_autoChooser;
    private AutoFactory m_AutoFactory;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer(RobotSystems systems) {
        Logger.configureLoggingAndConfig(this, false);

        this.systems = systems;

        ShuffleboardHelper shuffleboardHelper = new ShuffleboardHelper();
        shuffleboardHelper.register();

        m_autoChooser = new SendableChooser<>();
        m_autoChooser.setDefaultOption("rollback", Autos.ROLLBACK);
        m_autoChooser.addOption("Five ball auto", Autos.FIVEBALLAUTO);
        m_autoChooser.addOption("2 ball truss", Autos.TWOBALLTRUSS);
        m_autoChooser.addOption("do nothing", Autos.DONOTHING);
        SmartDashboard.putData(m_autoChooser);

        SmartDashboard.putNumber("shooter velo offset", 0);

        // Start the compressor
        CommandScheduler.getInstance().schedule(new InstantCommand(() -> systems.Compressor.start()));

        m_AutoFactory = new AutoFactory(systems);
        // Set up rumble system
        GunnerRumble.setController(systems.GunnerController);
        var driverConfig = new DriverConfig();
        DoubleSupplier xSupplier = new DriveInput(systems.DriverController::getLeftY, DriveInput.InputType.TRANSLATION, driverConfig, false);
        DoubleSupplier ySupplier = new DriveInput(systems.DriverController::getLeftX, DriveInput.InputType.TRANSLATION, driverConfig, false);
        DoubleSupplier thetaSupplier = ()-> 0.8*MathUtil.applyDeadband(-systems.DriverController.getRightX(),driverConfig.deadband);

        systems.DriveTrain.setDefaultCommand(
                new DefaultDriveCommand(systems.DriveTrain, xSupplier, ySupplier, thetaSupplier));


        // Configure the button bindings
        configureButtonBindings(systems.DriverController, systems.GunnerController);
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings(XboxController driver, XboxController gunner) {
        TriggerSupplier.getLeftBumper(driver).whenActive(systems.getCommands().toggleIntake());

        TriggerSupplier.getStartsButton(driver).whenActive(() -> systems.DriveTrain.resetGyro());

        Trigger leftTriggerDriver = TriggerSupplier.getLeftTrigger(driver, systems.getConfig().Driver.triggerSensitivity)
                .whenActive(systems.getCommands().ejectCargo())
                .whileActiveOnce(systems.getCommands().ballHandlerForward())
                .whenInactive(systems.getCommands().ballHandlerNeutral());

        Trigger rightTriggerDriver = TriggerSupplier.getRightTrigger(driver, systems.getConfig().Driver.triggerSensitivity)
                .whenActive(systems.getCommands().intakeCargo())
                .whileActiveOnce(systems.getCommands().ballHandlerReverse())
                .whenInactive(systems.getCommands().ballHandlerNeutral());


        // When not moving either direction, neutral rollers
        new Trigger(leftTriggerDriver).or(rightTriggerDriver).negate().whileActiveOnce(systems.getCommands().idleRollers());

        TriggerSupplier.getRightTrigger(gunner, systems.getConfig().Driver.triggerSensitivity)
        .whileActiveOnce(systems.getCommands().upgoerUp())
        .whileActiveOnce(systems.getCommands().ballHandlerForward())
        .whenInactive(systems.getCommands().upgoerNeutral())
        .whenInactive(systems.getCommands().ballHandlerNeutral());
        TriggerSupplier.getStartsButton(driver).whileActiveOnce(new ResetGyro(systems.DriveTrain));

        TriggerSupplier.getLeftBumper(gunner)
        .whileActiveOnce(systems.getCommands().ballHandlerReverse())
        .whenInactive(systems.getCommands().ballHandlerNeutral());

        TriggerSupplier.getRightBumper(driver).whenActive(() -> DriveInput.toggleGear()).whenInactive(() -> DriveInput.toggleGear());




        // ---------- Climber Bindings ----------

        TriggerSupplier.getYButton(gunner).whileActiveOnce(systems.Climber.partialUp);
        TriggerSupplier.getAButton(gunner).whileActiveOnce(systems.Climber.partialDown);

        TriggerSupplier.getBButton(gunner).whileActiveOnce(systems.Climber.autoNext);
        TriggerSupplier.getRightBumper(gunner).whenActive(systems.Climber.zeroClimber);

        TriggerSupplier.getXButton(gunner).whenActive(new SnapAngle(systems.pigeon.getPitchSupplier()));

        TriggerSupplier.getBackButton(gunner).whenActive(new StopAutoClimb(systems.Climber));
        TriggerSupplier.getStartsButton(gunner).whenActive(systems.Climber.autoClimb);
        TriggerSupplier.getLeftTrigger(gunner, systems.getConfig().Driver.triggerSensitivity)
                .whileActiveOnce(new Target(systems))
                .whenInactive(new StopTargeting(systems));

        TriggerSupplier.getLeftBumper(gunner).whenActive(systems.Climber.setUpForClimb);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return m_AutoFactory.constructAuto(m_autoChooser.getSelected());
    }

    public void haltActuation() {
        Command haltActuation = new ParallelDeadlineGroup(
                new WaitCommand(1.5),
                new StopTargeting(systems),
                systems.getCommands().ballHandlerNeutral(),
                systems.getCommands().idleRollers(),
                systems.getCommands().upgoerNeutral());

        haltActuation.schedule();
    }

    public void setRobotNorth(double robotNorth) {
        if (systems.getConfig().turretConfig.simulated)
            return;
        systems.Turret.setRobotNorth(robotNorth);
    }

    public void setAutoOffset(double autoOffset) {
        if (systems.getConfig().turretConfig.simulated)
            return;
        systems.Turret.setAutoOffset(autoOffset);
    }
}
