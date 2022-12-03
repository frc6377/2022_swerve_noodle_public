package frc.robot.systems.climber.commands.autoFunctions.climbs;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.systems.climber.commands.autoFunctions.timers.AngleTimer;
import frc.robot.systems.climber.commands.autoFunctions.timers.NoWaitTimer;
import frc.robot.systems.climber.commands.autoFunctions.utils.ClimbSequence;
import frc.robot.systems.climber.commands.autoFunctions.utils.DirectionOfSwing;
import frc.robot.systems.climber.ClimberConfig;
import frc.robot.systems.climber.ClimberHardware;

/**
 * Waits for next bar at a partial extension
 * ACTIVATION: Manual once, Line up, Manual once, Wait for hooks, Auto
 */
public class TravPauseClimb extends ClimbSequence{

public TravPauseClimb(ClimberConfig cfg, ClimberHardware CH,  DoubleSupplier pitchAccessor) {
    super(CH);

    timers = new Command[]{ 
      // Retracted -- Extend - Human
      // Extend -- Hook - Human
      new NoWaitTimer(), // Hook -- pause
      new AngleTimer(pitchAccessor, cfg.pauseToExtendAngle, DirectionOfSwing.DOWN, cfg),// Pause -- Extend
      new AngleTimer(pitchAccessor, cfg.extendToSwingAngle, DirectionOfSwing.UP, cfg), //Extend -- swing
      new AngleTimer(pitchAccessor, cfg.swingToHookAngle, DirectionOfSwing.UP, cfg), // swing -- hook
      new NoWaitTimer(), // hook -- pause
      new AngleTimer(pitchAccessor, cfg.pauseToExtendAngle, DirectionOfSwing.DOWN, cfg), // Pause -- Extend
      new AngleTimer(pitchAccessor, cfg.extendToSwingAngle, DirectionOfSwing.UP, cfg), //Extend -- swing
    };

    climbSeq = new Command[]{
      retract,
      extendClimberCommand,
      lowerPosition,
      traversalPause,
      traversalClimb,
      swinging,
      lowerPosition,
      traversalPause,
      traversalClimb,
      swinging
    }; 
  }
}
