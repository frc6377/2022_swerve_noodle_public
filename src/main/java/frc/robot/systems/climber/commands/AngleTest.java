package frc.robot.systems.climber.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.systems.climber.commands.autoFunctions.timers.AngleTimer;
import frc.robot.systems.climber.commands.autoFunctions.utils.DirectionOfSwing;
import frc.robot.systems.climber.ClimberConfig;
import frc.robot.systems.climber.ClimberSubsystem;

public class AngleTest extends SequentialCommandGroup{
  public AngleTest(DoubleSupplier a, ClimberSubsystem subsystem, ClimberConfig cfg){
    addCommands(new AngleTimer(a, -20, DirectionOfSwing.DOWN, cfg));
  }
}
