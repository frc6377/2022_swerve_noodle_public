package frc.robot.common.config.robots;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.common.config.Config;

public class RapidReactBot extends Config {
  {
    robotName = "RapidReactBot";

    // --------------------------- Hardware on robot and types ---------------------------
    shooterConfig.simulate = true;
    turretConfig.simulated = true;
    pneumaticConfig.simulated = true;
    intakeConfig.simulated = true; // Is dependt on Pneumatics being enabled
    upgoerConfig.simulated = true;
    ballHandlerConfig.simulated = true;
    climberConfig.simulated = false;

    pneumaticConfig.pcmType = PneumaticsModuleType.REVPH;

    // --------------------------- IDs ---------------------------
                    
     
    shooterConfig.flywheelID = 9;
    shooterConfig.flywheelFollowID = 11;
    shooterConfig.hoodRightID = 6;
    shooterConfig.hoodLeftID = 7;
    turretConfig.turretId = 30;

    pneumaticConfig.compressorID = 1;

    intakeConfig.soleniodExtensionModuleId = 1;
    intakeConfig.soleniodRetrationModuleId = 0;

    intakeConfig.rollerId = 13;
    intakeConfig.pneumaticControllerID = 1;

    ballHandlerConfig.frontFlywheelsBeltMotorID = 14;
    ballHandlerConfig.forwardOutput = -0.4;
    ballHandlerConfig.reverseOutput = 0.4;

    upgoerConfig.upgoerLeftMotorID = 2;
    upgoerConfig.upOutput = 0.8;

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
    turretConfig.turretRange = 180;
    turretConfig.degreesToTics = 175.7234568;
    turretConfig.invertIsClockwise = true;
    turretConfig.kF = 0.025583;
    turretConfig.kP = 0.291082;
    turretConfig.maxAccel = 29770;
    turretConfig.maxVelo = 25304;

    limelightConfig.mountingAngleCloseRange = 48;
    limelightConfig.mountingHeightCloseRange = 30;
    limelightConfig.mountingAngleLongRange = 42;
    limelightConfig.mountingHeightLongRange = 31;

    targetConfig.targetHeight = 104;

    intakeConfig.update();
    intakeConfig.intakePercentage = 0.8;
    intakeConfig.ejectPercentage = -0.8;

                    
    intakeConfig.simulated = false;
    intakeConfig.rollerId = 13;
    intakeConfig.soleniodExtensionModuleId = 1;
    intakeConfig.soleniodRetrationModuleId = 0;
  }
}
