package frc.robot.systems.climber.commands.autoFunctions.utils;

import frc.robot.systems.climber.ClimberSubsystem;

import java.util.function.DoubleSupplier;


public class RollDetection {
  private static ClimberSubsystem subsystem;
  private static DoubleSupplier rollGetter;
  private static double maxRoll = 20;

  public static void setRollAccessor(DoubleSupplier getter){
    rollGetter = getter;
  }

  public static void setSubsystem(ClimberSubsystem cSubsystem){
    subsystem = cSubsystem;
  }

  public static void setMaxRoll(double x){
    maxRoll = x;
  }

  /**
   * Checks if rolled over, and if rolled over stop auto control.
   * @return if currently rolled over
   */
  public static boolean isRolledOver(){
    if(Math.abs(rollGetter.getAsDouble()) > maxRoll){
      System.out.println("Is rolled over");
      subsystem.underAutoControl = false;
      return true;
    }
    return false;
  }
}
