package frc.robot.systems.turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.common.motorfactory;
import frc.robot.common.utilities.MovingAverage;

public class TurretSubsystem extends SubsystemBase {
  private final LimelightSubsystem limelight;
  private final TurretConfig cfg;
  private final TargetConfig trgcfg;
  private TurretState turretState;
  private double robotNorth = -90;
  private double autoOffset = 0;
  private MovingAverage dataSet = new MovingAverage();
  private SwerveDriveOdometry odom;
  private final TurretHardware hardware;

  private ShuffleboardTab tab;
  private NetworkTableEntry useOdomPointing;

  public TurretSubsystem(
      LimelightSubsystem limelight,
      TurretConfig cfg,
      TargetConfig trgcfg,
      SwerveDriveOdometry odom) {
    this.odom = odom;
    this.limelight = limelight;
    this.cfg = cfg;
    this.trgcfg = trgcfg;
    this.hardware = new TurretHardware(cfg);
    turretState = TurretState.IDLE;

    tab = Shuffleboard.getTab("Shooter");
    useOdomPointing =
            tab.add("OdomPointing", true)
              .getEntry();
    useOdomPointing.setBoolean(true);
  }

  public enum TurretState {
    TARGETING,
    AUTO,
    IDLE
  }


  public void pointAtVisionTarget() {
    moveTurret(-limelight.getXOffset() + autoOffset + offset());
  }

  private void pointAtHubNoVision(){
    if(turretState != TurretState.AUTO){
      setTurretAbsolute(robotNorth);
      return;
    }

    Translation2d hub = new Translation2d((54*12)/2d, (27*12)/2d);

    double metersToInches = 39.3701;
    Pose2d curPoseMeters = odom.getPoseMeters();
    Translation2d curTranslateFeet = curPoseMeters.getTranslation().times(metersToInches);
    Pose2d curPoseFeet = new Pose2d(curTranslateFeet, curPoseMeters.getRotation());

    Translation2d translationToHub = hub.minus(curTranslateFeet);

    Rotation2d angleToHubFromFieldNorth = new Rotation2d(Math.atan2(translationToHub.getY(), translationToHub.getX()));

    Rotation2d turretToHubFromFieldNorth = angleToHubFromFieldNorth.minus(curPoseFeet.getRotation());

    if(useOdomPointing.getBoolean(false)) setTurretAbsolute(turretToHubFromFieldNorth.getDegrees());
  }

  private void printTranslation(Translation2d translate){
    System.out.print("x:"+translate.getX()+" y:"+translate.getY()+" mag:"+getMagnitude(translate));
  }

  private double getMagnitude(Translation2d translate){
    return Math.sqrt(translate.getX()*translate.getX() + translate.getY()*translate.getY());
  }

  public void setTurretAbsolute(double pos) {
    hardware.turret.set(ControlMode.MotionMagic, pos * cfg.degreesToTics);
  }

  /**
   * Points at a target angle in degrees.
   * @param pos -target angle in degrees 
   */
  public void moveTurret(double pos) {
    var sensorPosition = hardware.turret.getSelectedSensorPosition();
    double setpoint = sensorPosition + (pos * cfg.degreesToTics);
    SmartDashboard.putNumber("tsetpoint preclamp", setpoint);
    setpoint = MathUtil.clamp(setpoint, -(cfg.turretRange*cfg.degreesToTics), cfg.turretRange*cfg.degreesToTics);
    SmartDashboard.putNumber("tsetpoint postclamp", setpoint);
    hardware.turret.set(ControlMode.MotionMagic, setpoint);
  }

  public boolean canSeeTarget() {
    return limelight
        .hasValidTarget(); // could also pass limelight object into command but i opted for an
    // accessor cause it was easier
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("turret state", turretState.toString());
    SmartDashboard.putNumber("turret angle", getTurretHeading());
    SmartDashboard.putNumber("heading to hub", getTurretHeading() - limelight.getXOffset());
    setAutoOffset(SmartDashboard.getNumber("turret offset", 0));
    if (limelight.hasValidTarget() && turretState != TurretState.IDLE) {
      pointAtVisionTarget();
    } else {      
      pointAtHubNoVision();
      dataSet.addData(0);
    }
  }

  public double getTurretHeading() {
    return hardware.turret.getSelectedSensorPosition() * cfg.ticsToDegrees;
  }

  public double getTargetX() {
    return trgcfg.targetX;
  }

  public double getTargetY() {
    return trgcfg.targetY;
  }

  public void setTurretState(TurretState newTurretState) {
    turretState = newTurretState;
  }

  public void setRobotNorth(double robotNorth) {
    this.robotNorth = robotNorth;
  }

  public void setAutoOffset (double autoOffset) {
    this.autoOffset = autoOffset;
  }

  public double offset(){
    return ((limelight.getDist()*-0.0625) + 7);
  }

  private class TurretHardware {
    public TalonFX turret;

    public TurretHardware(
            TurretConfig cfg) {
      if (cfg.simulated) return;
      var turretConfig = new motorfactory.MotorConfiguration();

      invert(cfg, turretConfig);
      setClosedLoopGains(cfg, turretConfig);
      turretConfig.neutralMode = NeutralMode.Brake;
      turretConfig.talonFXConfig.forwardSoftLimitEnable = true;
      turretConfig.talonFXConfig.reverseSoftLimitEnable = true;
      turretConfig.talonFXConfig.forwardSoftLimitThreshold =
              (cfg.turretRange / 2) * cfg.degreesToTics;
      turretConfig.talonFXConfig.reverseSoftLimitThreshold =
              -((cfg.turretRange / 2)+20) * cfg.degreesToTics;
      turretConfig.talonFXConfig.primaryPID.selectedFeedbackSensor =
              TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();
      turretConfig.talonFXConfig.motionAcceleration = cfg.maxAccel;
      turretConfig.talonFXConfig.motionCruiseVelocity = cfg.maxVelo;

      turret = motorfactory.createTalonFX(cfg.turretId, turretConfig);
    }

    private void invert(TurretConfig cfg, motorfactory.MotorConfiguration motorConfig) {
      motorConfig.invertType = TalonFXInvertType.CounterClockwise;
      if (cfg.invertIsClockwise) motorConfig.invertType = TalonFXInvertType.Clockwise;
    }

    private void setClosedLoopGains(TurretConfig cfg, motorfactory.MotorConfiguration motorConfig) {
      motorConfig.talonFXConfig.slot0.kF = cfg.kF;
      motorConfig.talonFXConfig.slot0.kP = cfg.kP;
    }
  }
}
