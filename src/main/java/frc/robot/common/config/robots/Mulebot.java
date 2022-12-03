package frc.robot.common.config.robots;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.common.config.Config;

public class Mulebot extends Config {
  {
    robotName = "MuleBot";

                    
    climberConfig.leadClimberMotor = 7; 
    climberConfig.followClimberMotor = 6;

    ballHandlerConfig.simulated = false;
    shooterConfig.simulate = true;
    turretConfig.simulated = true;
    intakeConfig.simulated = true;
    upgoerConfig.simulated = true;
    climberConfig.simulated = true;
    
          
          
    shooterConfig.flywheelID = 9;
    shooterConfig.flywheelFollowID = 11;
    shooterConfig.kf = 0.1;
    shooterConfig.kp = 0;
    shooterConfig.shooterInvertIsClockwise = true;
    shooterConfig.followMasterInvert = false;
    shooterConfig.idleVelo = 1000;

    turretConfig.ticsToDegrees = 1 / 150;
    turretConfig.turretId = 30;
    turretConfig.turretRange = 90;
    turretConfig.degreesToTics = 150;
    turretConfig.invertIsClockwise = true;
    turretConfig.kF = 0.051291;
    turretConfig.kP = 0.354916;
    turretConfig.maxAccel = 500;
    turretConfig.maxVelo = 1000;

    intakeConfig.simulated = true;
    intakeConfig.soleniodExtensionModuleId = 1;
    intakeConfig.soleniodRetrationModuleId = 0;

    // pneumaticConfig.simulated = true;
    pneumaticConfig.compressorID = 0;
    pneumaticConfig.pcmType = PneumaticsModuleType.CTREPCM;

    intakeConfig.pcmType = pneumaticConfig.pcmType;
    intakeConfig.rollerId = 44;
    intakeConfig.ejectPercentage = -0.3;
    intakeConfig.intakePercentage = 0.3;
    intakeConfig.intakeWait = 0.1;
    intakeConfig.pneumaticControllerID = 0;

    upgoerConfig.upgoerLeftMotorID = 255;
  }
}
