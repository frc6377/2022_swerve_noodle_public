package frc.robot.common.utilities;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.robot.systems.drivetrain.config.DriverConfig;

public class DriveInput implements DoubleSupplier{
  private SlewRateLimiter limiter = new SlewRateLimiter(50);
  private final DoubleSupplier in;
  private InputType type;
  private static boolean highGear;
  private DriverConfig cfg;
  private int invert;


  public DriveInput(DoubleSupplier in, InputType type, DriverConfig cfg){
    this(in, type, cfg, false);
  }

  public DriveInput(DoubleSupplier in, InputType type, DriverConfig cfg, boolean isX){
    this.in = in;
    this.cfg = cfg;
    this.type = type;
    invert = isX ? -1 : 1;
  }

  @Override
  public double getAsDouble() {
    double percentage = type.getMult(highGear, cfg);
    double val = limiter.calculate(in.getAsDouble() * invert * percentage);
    val = MathUtil.applyDeadband(val, cfg.deadband);
    return val;
  }  

  public static void toggleGear(){
    highGear = !highGear;
  }

  public enum InputType{
    TRANSLATION(0),
    ROTATION(1);
    private int id;
    private InputType(int id){
      this.id = id;
    }

    public double getMult(boolean highGear, DriverConfig cfg){
      switch(id){
        default:
        case 0:
        if(highGear) return -cfg.highGearMaxSped;
        else return -cfg.lowGearMaxSped;
        case 1:
        if(highGear) return cfg.highGearTurnPercent;
        else return cfg.lowgearTurnPercent;
      }
    }

  }
}
