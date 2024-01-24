// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorPIDCmd extends Command {
    private final ElevatorSubsystem elevatorSubsystem;
    private final PIDController pidController;

public ElevatorPIDCmd(ElevatorSubsystem elevatorSubsystem, double setpoint) {
    this.elevatorSubsystem = elevatorSubsystem;
    this.pidController = new PIDController(1.0, 0, 0);  // Put in Constants
    pidController.setSetpoint(setpoint);
    addRequirements(elevatorSubsystem);
  }

  @Override
  public void initialize() {  // initialize runs once every command call. Vs constructor runs once at startup
      System.out.println("ElevatorPIDCmd Started");
      pidController.reset();
  }

  @Override
  public void execute() {
      double speed = pidController.calculate(elevatorSubsystem.getEncoderMeters());
      elevatorSubsystem.setMotor(speed);
  }

  @Override
  public void end(boolean interrupted) {
      elevatorSubsystem.setMotor(0);
      System.out.println("ElevatorPIDCmd Ended");
  }

  @Override
  public boolean isFinished() {
      return false;
  }
}
