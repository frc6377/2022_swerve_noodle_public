package frc.robot.systems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.systems.drivetrain.DrivetrainSubsystem;

public class ResetGyro extends CommandBase {
  DrivetrainSubsystem system;
  public ResetGyro(DrivetrainSubsystem system){

    this.system = system;
  }

  public void intialize(){
system.resetGyro();
  }

  public boolean isfinished(){
    return true;
  }
}
