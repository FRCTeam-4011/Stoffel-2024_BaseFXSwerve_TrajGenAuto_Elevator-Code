// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase {
  
    private final CANSparkMax elevatorMotor;
    private final RelativeEncoder elevatorEncoder;
    private final double kEncoderTick2Meter = 1.0 / 42.0 * 0.1 * Math.PI;



    public ElevatorSubsystem() {
     // elevatorMotor = new CANSparkMax(Constants.elevatorNeoID, MotorType.kBrushless); THIS IS OLD NOW

      elevatorMotor = new CANSparkMax(Constants.elevatorNeoID, MotorType.kBrushless);

      elevatorEncoder = elevatorMotor.getEncoder();
      elevatorMotor.restoreFactoryDefaults();

      elevatorMotor.setInverted(false);
      elevatorEncoder.setInverted(false);

    }

     public double getEncoderMeters() { 
        return elevatorEncoder.getPosition() * kEncoderTick2Meter;
     }


  @Override
      public void periodic() {
          SmartDashboard.putNumber("Elevator encoder value", getEncoderMeters());
    }

      public void setMotor(double speed) {
          elevatorMotor.set(speed);
    }
}