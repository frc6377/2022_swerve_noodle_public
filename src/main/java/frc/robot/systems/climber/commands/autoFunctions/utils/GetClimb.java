package frc.robot.systems.climber.commands.autoFunctions.utils;

import frc.robot.systems.climber.commands.autoFunctions.climbs.*;
import frc.robot.systems.climber.ClimberConfig;
import frc.robot.systems.climber.ClimberHardware;

import java.util.function.DoubleSupplier;

/**
 * Use to convert from Climbs to a ClimbSequence.
 */
public class GetClimb {
  public static ClimbSequence getClimb(Climbs climb, ClimberConfig cfg, ClimberHardware CH, DoubleSupplier pitchAccessor){
    switch(climb){
      case AUTO_SOONER:
      return new AutoSooner(cfg, CH, pitchAccessor);
      case NO_WAIT:
      return new NoWaitClimb(cfg, CH, pitchAccessor);
      case SWING:
      return new SwingClimb(cfg, CH, pitchAccessor);
      case TRAV_PAUSE:
      return new TravPauseClimb(cfg, CH, pitchAccessor);
      default:
      System.out.println("-------- No climb Seq picked");
      return null;
    }
  }
}
