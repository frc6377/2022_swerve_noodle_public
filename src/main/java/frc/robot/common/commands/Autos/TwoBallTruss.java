package frc.robot.common.commands.Autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotSystems;
import frc.robot.systems.drivetrain.commands.SwerveAutoCommand;
import frc.robot.systems.turret.commands.StopTargeting;
import frc.robot.systems.turret.commands.Target;

public class TwoBallTruss extends SequentialCommandGroup {
  public TwoBallTruss (RobotSystems systems){
    addCommands(
      new Target(systems),
      systems.getCommands().toggleIntake(),
      systems.getCommands().ejectCargo(),
      systems.getCommands().ballHandlerForward(),
      new SwerveAutoCommand("2BallTruss", systems.DriveTrain, true),
      new WaitCommand(2),
      systems.getCommands().toggleIntake(),
      systems.getCommands().upgoerUp(),
      new WaitCommand(1),
      systems.getCommands().upgoerNeutral(),
      new StopTargeting(systems)
    );
  }
}
