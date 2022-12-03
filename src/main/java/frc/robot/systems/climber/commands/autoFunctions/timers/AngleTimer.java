package frc.robot.systems.climber.commands.autoFunctions.timers;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.systems.climber.commands.autoFunctions.utils.DirectionOfSwing;
import frc.robot.systems.climber.ClimberConfig;

public class AngleTimer extends CommandBase{
  private DoubleSupplier pitchAccessor;
  private DirectionOfSwing targetDirection;
  private double prevAngle = 0;
  private double taretgAngle;
  private ClimberConfig cfg;
  
  public AngleTimer(DoubleSupplier gyro, double targAngle, DirectionOfSwing dir, ClimberConfig cfg){
    this.pitchAccessor = gyro;
    this.targetDirection = dir;
    this.taretgAngle = targAngle;
    this.cfg = cfg;
  }

  public void setAngle(double ang){
    taretgAngle = ang;
  }

  @Override
  public boolean isFinished(){
    double curAngle = pitchAccessor.getAsDouble();
    DirectionOfSwing actualDir = null;
    // Determine actual travel of climber
    if(curAngle > prevAngle){
      actualDir = DirectionOfSwing.DOWN;
    }else{
      actualDir = DirectionOfSwing.UP;
    }
    prevAngle = curAngle;

    System.out.println("target direction "+targetDirection.toString()+" current direction "+actualDir.toString()+ " prev angle"+prevAngle+" curAngle:"+curAngle);

    // Make sure travelling in the correct direction
    if(actualDir != targetDirection) return false;

    // See if with in allowed error
    return Math.abs(taretgAngle - curAngle) < cfg.allowedAngleError;
  }

  @Override
  public void end(boolean nop){

    System.out.println("----------- Timer Tripped");
  }


}
