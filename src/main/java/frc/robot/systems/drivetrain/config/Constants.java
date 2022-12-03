// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.systems.drivetrain.config;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.62865; 
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.62865; 

    public static final int DRIVETRAIN_PIGEON_ID = 1; 

    public static final PodName FRONT_LEFT_POD_NAME = PodName.B;

    public static final PodName FRONT_RIGHT_POD_NAME = PodName.C;

    public static final PodName BACK_LEFT_POD_NAME = PodName.A;

    public static final PodName BACK_RIGHT_POD_NAME = PodName.D;

}
