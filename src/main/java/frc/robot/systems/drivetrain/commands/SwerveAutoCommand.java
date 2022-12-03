package frc.robot.systems.drivetrain.commands;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.systems.drivetrain.DrivetrainSubsystem;
import frc.robot.systems.sensors.Pigeon;

public class SwerveAutoCommand extends SequentialCommandGroup {
  public SwerveAutoCommand (
    String pathTofollow,
    DrivetrainSubsystem drivetrainSubsystem,
    Boolean isFirstPath) {
    PathPlannerTrajectory trajectory = PathPlanner.loadPath(pathTofollow, 4.5, 2.5);
    Pose2d initialPose;

    ProfiledPIDController thetaController = new ProfiledPIDController(2, 0.1, 0, new TrapezoidProfile.Constraints(2*720, 2*360));

    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    PPSwerveControllerCommand command =
    new PPSwerveControllerCommand(
    trajectory,
    drivetrainSubsystem::getPose,
    drivetrainSubsystem.getKinematics(),
    new PIDController(1, 0, 0),
    new PIDController(1, 0, 0), 
    thetaController, 
    drivetrainSubsystem::updateAutoDemand, 
    drivetrainSubsystem);
    
    
    
    initialPose = trajectory.getInitialPose();
    if(isFirstPath) drivetrainSubsystem.resetPose(initialPose); //TODO do this only once not every call
    drivetrainSubsystem.sendTrajectoryToNT(trajectory);
      System.out.println("total time " + trajectory.getTotalTimeSeconds());
    addCommands(command.andThen( new InstantCommand(() -> drivetrainSubsystem.drive(new ChassisSpeeds()))));
  }

  private class MotionController extends PPSwerveControllerCommand {
    public MotionController(
      PathPlannerTrajectory trajectory,
      Supplier<Pose2d> pose,
      SwerveDriveKinematics kinematics,
      PIDController xController,
      PIDController yController,
      ProfiledPIDController thetaController,
      Consumer<SwerveModuleState[]> outputModuleStates,
      Subsystem... requirements){
        super(trajectory,
        pose,
        kinematics,
        xController,
        yController,
        thetaController,
        outputModuleStates,
        requirements);
      }

    @Override
    public boolean isFinished(){
      return false;
    }
  }
}
