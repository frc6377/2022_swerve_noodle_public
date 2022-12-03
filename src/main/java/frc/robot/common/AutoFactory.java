package frc.robot.common;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotSystems;
import frc.robot.common.commands.Autos.FiveBallAuto;
import frc.robot.common.commands.Autos.RollBack;
import frc.robot.common.commands.Autos.TwoBallTruss;

public class AutoFactory {
  RobotSystems m_systems;
  public AutoFactory (RobotSystems systems){
    m_systems = systems;
  }

  public Command constructAuto(Autos auto){
      Command cmdToRun = new InstantCommand();
      switch (auto){

      case FIVEBALLAUTO:
      System.out.println("loading Five Ball Auto");
      cmdToRun = new FiveBallAuto(m_systems);
      break;

      case ROLLBACK:
       System.out.println("loading rollback and shoot Auto");
       cmdToRun = new RollBack(m_systems);
        break;

      case TWOBALLTRUSS:
      System.out.println("loading Two Ball Truss Auto");
      cmdToRun = new TwoBallTruss(m_systems);
        break;

      case DONOTHING:
      System.out.println("doing nothing in auto");
      cmdToRun = new InstantCommand();
      break;

      default:
      System.out.println("no auto loaded");
        break;

    }
    return cmdToRun;
  }



  public enum Autos{FIVEBALLAUTO, TWOBALLTRUSS, ROLLBACK, DONOTHING}
}
