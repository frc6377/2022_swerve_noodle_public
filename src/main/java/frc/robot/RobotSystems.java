package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.common.Splash;
import frc.robot.common.config.*;
import frc.robot.common.dashboard.ShuffleboardHelper;
import frc.robot.systems.ballhandler.BallHandlerSubsystem;
import frc.robot.systems.ballhandler.UpgoerSubsystem;
import frc.robot.systems.climber.ClimberHardware;
import frc.robot.systems.compressor.CompressorSubsystem;
import frc.robot.systems.drivetrain.DrivetrainSubsystem;
import frc.robot.systems.intake.IntakeSubsystem;
import frc.robot.systems.sensors.Pigeon;
import frc.robot.systems.turret.*;

public class RobotSystems {
    private Config config = null;
    private final Commands commands;
    public final XboxController DriverController;
    public final XboxController GunnerController;
    public final DrivetrainSubsystem DriveTrain;
    public final ShooterSubsystem Shooter;
    public final LimelightSubsystem Limelight;
    public final IntakeSubsystem Intake;
    public final CompressorSubsystem Compressor;
    public final BallHandlerSubsystem BallHandler;
    public final TurretSubsystem Turret;
    public final UpgoerSubsystem Upgoer;
    public final ClimberHardware Climber;
    public final Pigeon pigeon;

    public RobotSystems() {
        try {
            // Load information that we only know at runtime.
            RuntimeConfig runtimeConfig = new RuntimeConfig((String s) -> Main.class.getResourceAsStream(s));
            // Find the configuration class to load for this robot
            var robotConfigType = RobotDetector.GetConfigClass(runtimeConfig.macAddress);
            // Load config from disk if it has changed, otherwise use the default robot
            // configuration
            var configReader = new ConfigReader<Config>(robotConfigType);
            config = configReader.Read();
            if (config == null) {
                throw new ConfigCreationException("could create robot");
            }

            Splash.printAllStatusFiles(config, runtimeConfig);
            // Initialize ALL the hardware
            ShuffleboardHelper.AddRuntimeConfig(runtimeConfig, config);
            // Instantiate our RobotContainer. This will perform all our button bindings,
            // and put our
            // autonomous chooser on the dashboard.
        } catch (ConfigCreationException cfgException) {
            cfgException.printStackTrace();
        }

        Limelight = new LimelightSubsystem(config.limelightConfig, config.targetConfig);

        pigeon = new Pigeon(config.pigeonConfig.id, config.pigeonConfig.isPigeon2);

        DriverController = new XboxController(config.Driver.joystickPortID);
        GunnerController = new XboxController(config.Gunner.joystickPortID);
        
        DriveTrain = new DrivetrainSubsystem(pigeon.getYawSupplier());         
        // this instance of shooter will be tied to commands
        Shooter = new ShooterSubsystem(config.shooterConfig, Limelight, DriveTrain.getOdometryRefernce());
        Intake = new IntakeSubsystem(config.intakeConfig);
        // Might need a separate pneumatic config
        Compressor = new CompressorSubsystem(config.pneumaticConfig);
        BallHandler = new BallHandlerSubsystem(config.ballHandlerConfig);

        Turret = new TurretSubsystem(Limelight, config.turretConfig, config.targetConfig, DriveTrain.getOdometryRefernce());
        Upgoer = new UpgoerSubsystem(config.upgoerConfig);
        Climber = new ClimberHardware(config.climberConfig, pigeon.getPitchSupplier(), pigeon.getRollSupplier());

        commands = new Commands(this);
    }

    public Config getConfig() {
        return config;
    }

    public Commands getCommands() {
        return commands;
    }

    public class Commands {
        private final RobotSystems systems;

        public Commands(RobotSystems systems) {
            this.systems = systems;
        }

        public CommandBase turnOnLEDs() {
            return new InstantCommand(() -> {systems.Limelight.turnOnLEDs();}, systems.Limelight);
        }

        public CommandBase turnOffLEDs() {
            return new InstantCommand(() -> {systems.Limelight.turnOffLEDs();}, systems.Limelight);
        }

        public CommandBase pointAtHub() {
            return new InstantCommand(
                    () -> systems.Turret.setTurretState(TurretSubsystem.TurretState.IDLE),
                    systems.Turret);
        }

        public CommandBase turretTarget() {
            return new InstantCommand(
                    () -> systems.Turret.setTurretState(TurretSubsystem.TurretState.TARGETING),
                    systems.Turret);
        }

        public CommandBase turretAutoTarget() {
            return new InstantCommand(
                    () -> systems.Turret.setTurretState(TurretSubsystem.TurretState.AUTO),
                    systems.Turret);
        }
        

        public CommandBase intakeCargo() {
            return new InstantCommand(() -> systems.Intake.intakeCargo(), systems.Intake);
        }

        public CommandBase ejectCargo() {
            return new InstantCommand(() -> systems.Intake.ejectCargo(), systems.Intake);
        }

        public CommandBase idleRollers() {
            return new InstantCommand(() -> systems.Intake.idleRollers(), systems.Intake);
        }

        public CommandBase toggleIntake() {
            return new SequentialCommandGroup(
                    new InstantCommand(() -> systems.Intake.toggleIntake()),
                    new WaitCommand(systems.config.intakeConfig.intakeWait),
                    new InstantCommand(() -> systems.Intake.neutralExtension())
            );
        }

        public CommandBase shooterIdle() {
            return new InstantCommand(
                    () -> systems.Shooter.setShooterState(ShooterSubsystem.ShooterState.IDLE),
                    Shooter
            );
        }

        public CommandBase shooterTarget() {
            return new InstantCommand(
                    () -> systems.Shooter.setShooterState(ShooterSubsystem.ShooterState.TARGETING),
                    Shooter
            );
        }

        public CommandBase upgoerUp() {
            return new InstantCommand(
                    () -> systems.Upgoer.RunUpgoer(UpgoerSubsystem.State.UP),
                    systems.Upgoer
            );
        }

        public CommandBase upgoerDown() {
            return new InstantCommand(
                    () -> systems.Upgoer.RunUpgoer(UpgoerSubsystem.State.DOWN),
                    systems.Upgoer
            );
        }

        public CommandBase upgoerNeutral() {
            return new InstantCommand(
                    () -> systems.Upgoer.RunUpgoer(UpgoerSubsystem.State.NEUTRAL),
                    systems.Upgoer
            );
        }

        public CommandBase ballHandlerForward() {
            return new InstantCommand(
                    () -> systems.BallHandler.setForward(),
                    systems.BallHandler
            );
        }

        public CommandBase ballHandlerReverse() {
            return new InstantCommand(
                    () -> systems.BallHandler.setReverse(),
                    systems.BallHandler
            );
        }

        public CommandBase ballHandlerNeutral() {
            return new InstantCommand(
                    () -> systems.BallHandler.setNeutral(),
                    systems.BallHandler
            );
        }
    }
}
