package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final XboxController operatorXbox = new XboxController(1);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);

    /* Operator Buttons */
    private final JoystickButton elevatorManualUp = new JoystickButton(operatorXbox, XboxController.Button.kX.value);
    private final JoystickButton elevatorManualDown = new JoystickButton(operatorXbox, XboxController.Button.kY.value);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();

 /* Auto names */
    private final Command exampleAuto = new exampleAuto(s_Swerve);              // NEEDS MORE SUBSYSTEMS
    private final Command SecondAutoForGrins = new SecondAutoForGrins(s_Swerve); 
    private final Command thirdSeqCmdGrpAuto = new thirdSeqCmdGrpAuto(s_Swerve);

    // A chooser for autonomous commands
    SendableChooser<Command> m_chooser = new SendableChooser<>();




    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean()
            )
        );
        elevatorSubsystem.setDefaultCommand(new ElevatorJoystickCmd(elevatorSubsystem, 0));

// Add commands to the autonomous command chooser
        m_chooser.setDefaultOption("Example Auto", exampleAuto);        // *****************
        m_chooser.addOption("Second Auto", SecondAutoForGrins);         //    KEY SECTION
        m_chooser.addOption("Third Auto Option", thirdSeqCmdGrpAuto);   // *****************

        // Put the chooser on the dashboard
        Shuffleboard.getTab("Autonomous").add(m_chooser);
        // Put subsystems to dashboard.
        Shuffleboard.getTab("Drivetrain").add(s_Swerve);
//     Shuffleboard.getTab("HatchSubsystem").add(m_hatchSubsystem);
 
        // Log Shuffleboard events for command initialize, execute, finish, interrupt
        CommandScheduler.getInstance()
         .onCommandInitialize(
             command ->
                 Shuffleboard.addEventMarker(
                     "Command initialized", command.getName(), EventImportance.kNormal));
        CommandScheduler.getInstance()
         .onCommandExecute(
             command ->
                 Shuffleboard.addEventMarker(
                     "Command executed", command.getName(), EventImportance.kNormal));
        CommandScheduler.getInstance()
         .onCommandFinish(
             command ->
                 Shuffleboard.addEventMarker(
                     "Command finished", command.getName(), EventImportance.kNormal));
        CommandScheduler.getInstance()
         .onCommandInterrupt(
             command ->
                 Shuffleboard.addEventMarker(
                     "Command interrupted", command.getName(), EventImportance.kNormal));



        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));

        /* Operator Buttons */
        new JoystickButton(operatorXbox, 6).onTrue(new ElevatorPIDCmd(elevatorSubsystem, 1.2)); // meters
        new JoystickButton(operatorXbox, 7).onTrue(new ElevatorPIDCmd(elevatorSubsystem, 0));
        elevatorManualUp.whileTrue(new ElevatorJoystickCmd(elevatorSubsystem, 0.5));
        elevatorManualDown.whileTrue(new ElevatorJoystickCmd(elevatorSubsystem, -0.5));
  //      new JoystickButton(operatorXbox, 8).onTrue(new IntakeSetCmd(intakeSubsystem, false));
        
        new JoystickButton(operatorXbox, 5).whileTrue(new ElevatorJoystickCmd(elevatorSubsystem, 0));
                

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new exampleAuto(s_Swerve);
    }
}
