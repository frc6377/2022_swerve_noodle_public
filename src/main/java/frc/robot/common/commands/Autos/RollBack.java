package frc.robot.common.commands.Autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotSystems;
import frc.robot.systems.drivetrain.commands.SwerveAutoCommand;
import frc.robot.systems.turret.commands.StopTargeting;
import frc.robot.systems.turret.commands.Target;

public class RollBack extends SequentialCommandGroup {
  public RollBack (RobotSystems systems){
    addCommands(
      new Target(systems),
      new SwerveAutoCommand("rollback", systems.DriveTrain, true),
      new WaitCommand(0.1),
      systems.getCommands().ballHandlerForward(),
      systems.getCommands().upgoerUp(),
      new WaitCommand(1),
      systems.getCommands().upgoerNeutral(),
      new StopTargeting(systems)

    );
  }
}
