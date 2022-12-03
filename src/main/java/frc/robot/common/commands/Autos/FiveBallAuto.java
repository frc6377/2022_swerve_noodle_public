package frc.robot.common.commands.Autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotSystems;
import frc.robot.systems.drivetrain.commands.SwerveAutoCommand;
import frc.robot.systems.turret.commands.StopTargeting;
import frc.robot.systems.turret.commands.Target;

public class FiveBallAuto extends SequentialCommandGroup {
  public FiveBallAuto (RobotSystems systems){
    addCommands(
      systems.getCommands().toggleIntake(),
      systems.getCommands().ejectCargo(),
      systems.getCommands().ballHandlerForward(),
      new Target(systems),
      new SwerveAutoCommand("firstBall", systems.DriveTrain, true),
      new SwerveAutoCommand("BackupAndShoot", systems.DriveTrain, false),
      new WaitCommand(0.5),
      systems.getCommands().toggleIntake(),
      systems.getCommands().upgoerUp(),
      new WaitCommand(1),
      systems.getCommands().toggleIntake(),
      systems.getCommands().upgoerNeutral(),
      systems.getCommands().ejectCargo(),
      new SwerveAutoCommand("pickOne", systems.DriveTrain, false),
      new WaitCommand(0.1),
      systems.getCommands().toggleIntake(),
      systems.getCommands().upgoerUp(),
      new WaitCommand(1),
      systems.getCommands().upgoerNeutral(),
      systems.getCommands().toggleIntake(),
      systems.getCommands().ejectCargo(),
      new SwerveAutoCommand("pickTwo", systems.DriveTrain, false),
      new WaitCommand(1),
      systems.getCommands().toggleIntake(),
      new SwerveAutoCommand("shootTwo", systems.DriveTrain, false),
      new WaitCommand(0.1),
      systems.getCommands().upgoerUp(),
      new WaitCommand(2),
      systems.getCommands().upgoerNeutral(),
      new StopTargeting(systems)
    );


      }
  }

