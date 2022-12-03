package frc.robot.common.config.robots;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.common.config.Config;

public class DangerNoodle extends Config {
  {
    robotName = "DangerNoodle";

    // --------------------------- Hardware on robot and types ---------------------------
    shooterConfig.simulate = false;
    turretConfig.simulated = false;
    pneumaticConfig.simulated = false;
    intakeConfig.simulated = false; // Is dependt on Pneumatics being enabled
    upgoerConfig.simulated = false;
    ballHandlerConfig.simulated = false;
    climberConfig.simulated = false;

    pneumaticConfig.pcmType = PneumaticsModuleType.REVPH;

    // --------------------------- IDs ---------------------------
                    
     
    shooterConfig.flywheelID = 9;
    shooterConfig.flywheelFollowID = 11;
    shooterConfig.hoodRightID = 7;
    shooterConfig.hoodLeftID = 6;
    turretConfig.turretId = 30;

    pneumaticConfig.compressorID = 1;

    intakeConfig.soleniodExtensionModuleId = 1;
    intakeConfig.soleniodRetrationModuleId = 0;

    intakeConfig.rollerId = 13;
    intakeConfig.pneumaticControllerID = 1;

    ballHandlerConfig.frontFlywheelsBeltMotorID = 14;
    ballHandlerConfig.forwardOutput = -0.25;
    ballHandlerConfig.reverseOutput = 0.25;

    upgoerConfig.upgoerLeftMotorID = 8;
    upgoerConfig.upOutput = 0.4;

    // --------------------------- Random semi-important numbers ---------------------------
     
    shooterConfig.kf = 0.05;
    shooterConfig.kp = 0.2;
    shooterConfig.kI = 0.00/*125*/;
    shooterConfig.kD = 8;
    shooterConfig.iAccum = 600;
    shooterConfig.shooterInvertIsClockwise = false;
    shooterConfig.followMasterInvert = false;
    shooterConfig.idleVelo = 5000;

    turretConfig.ticsToDegrees = 1.0 / 175.7234568;
    turretConfig.turretRange = 200;
    turretConfig.degreesToTics = 175.7234568;
    turretConfig.invertIsClockwise = true;
    turretConfig.kF = 0.025583;
    turretConfig.kP = 0.291082;
    turretConfig.maxAccel = 3000;
    turretConfig.maxVelo = 2500;

    limelightConfig.mountingAngleLongRange = 40;
    limelightConfig.mountingHeightLongRange = 32;
    limelightConfig.mountingAngleCloseRange = 48;
    limelightConfig.mountingHeightCloseRange = 30;

    targetConfig.targetHeight = 102.5 ;

    intakeConfig.update();
    intakeConfig.intakePercentage = 0.8;
    intakeConfig.ejectPercentage = -0.8;

                    
    intakeConfig.simulated = false;
    intakeConfig.rollerId = 13;
    intakeConfig.soleniodExtensionModuleId = 1;
    intakeConfig.soleniodRetrationModuleId = 0;
  }
}
