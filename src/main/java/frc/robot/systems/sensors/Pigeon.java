package frc.robot.systems.sensors;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.PigeonIMU;

public class Pigeon {
  private Pigeon2 pigeon2;
  private PigeonIMU pigeon;
  private Runnable reset;

  private DoubleSupplier[] YPRSuppliers;
  private DoubleConsumer setYaw;

  public Pigeon(int id, boolean isPigeon2){
    if(isPigeon2){
      constructPigeon1(id);
    }else{
      constructPigoen2(id);
    }
  }

  public DoubleSupplier getYawSupplier(){
    return YPRSuppliers[0];
  }

  public DoubleSupplier getPitchSupplier(){
    return YPRSuppliers[1];
  }

  public DoubleSupplier getRollSupplier(){
    return YPRSuppliers[2];
  }

  public void reset(){
    reset.run();
  }

  public void setYaw(double yaw){
    setYaw.accept(yaw);
  }

  private void constructPigeon1(int id){
    pigeon = new PigeonIMU(id);
    YPRSuppliers = new DoubleSupplier[]{() -> pigeon.getYaw(),
                    () -> pigeon.getPitch(),
                    () -> pigeon.getRoll()};
    reset = () -> pigeon.setYaw(0);
    setYaw = (double yaw) -> pigeon.setYaw(yaw);
  }
  
  private void constructPigoen2(int id){
    pigeon2 = new Pigeon2(id);
    YPRSuppliers = new DoubleSupplier[]{() -> pigeon2.getYaw(),
      () -> pigeon2.getPitch(),
      () -> pigeon2.getRoll()};
    reset = () -> pigeon2.setYaw(0);
    setYaw = (double yaw) -> pigeon2.setYaw(yaw);
  }
}
