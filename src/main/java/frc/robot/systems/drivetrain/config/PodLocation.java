package frc.robot.systems.drivetrain.config;

public enum PodLocation{
  FRONT_RIGHT(0),
  FRONT_LEFT(1),
  BACK_RIGHT(2),
  BACK_LEFT(3);
  private int id; 

  // Notes from Oct 2022 robot config
  // pod C is motor id  6 as drive & 5 as steer. It is in Front Right.
  // pod D is motor id 10 as drive & 7 as steer. It is in Back Right.
  // pod A is motor id  2 as drive & 1 as steer. It is in Back Left.
  // pod B is motor id  4 as drive & 3 as steer. It is in Front Left.

  private PodLocation(int id){
    this.id = id;
  }

  public int getDriveMotorID(){
    switch(id){
      case 0:
        return 6; //Done
      case 1:
        return 4; //Done
      case 2:
        return 10; //Done
      case 3:
        return 2; //Done
      default:
        return 0;
    }
  }

  public int getSteerMotorID(){
    switch(id){
      case 0:
        return 5; //Done
      case 1:
        return 3; //Done
      case 2:
        return 7; //Done
      case 3:
        return 1; //Done
      default:
        return 0;
    }
  }

  public double getOffSet(){
    switch(id){
      case 0:
        return -Math.toRadians(0);
      case 1:
        return -Math.toRadians(90);
      case 2:
        return -Math.toRadians(180);
      case 3:
        return -Math.toRadians(270);
      default:
        return 0;
    }
  }
}

