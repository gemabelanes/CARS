/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsclient;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.ConsultationEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ejb.session.stateless.QueueEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InvalidLoginException;

/**
 *
 * @author gem
 */
public class MainApp {
    
    
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    private ConsultationEntityControllerRemote consultationEntityControllerRemote;
    private QueueEntityControllerRemote queueEntityControllerRemote;
  
    private AppointmentOperationsModule appointmentOperationsModule;
    private RegistrationOperationModule registrationOperationModule;
    private AdministrationOperationsModule administrationOperationsModule;
    
    private StaffEntity currentStaffEntity;
    
    public MainApp()
    {
    }

    public MainApp(StaffEntityControllerRemote staffEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, AppointmentEntityControllerRemote appointmentEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote, QueueEntityControllerRemote queueEntityControllerRemote) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
        this.queueEntityControllerRemote = queueEntityControllerRemote;
    }
    
    public void runApp() throws ParseException {
        System.out.println("STARTING UP...");
        
        Scanner sc = new Scanner(System.in);
        
        
        while(true) {
            System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            Integer response = 0;
            while(response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();
                if(response == 1) {
                    try {
                        doLogin();
                        menuMain();
                        break;
                    } catch (InvalidLoginException ex) {
                        System.out.println(ex);
                    }
                } else if(response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
            if(response == 2) {
                break;
            }
            sc.nextLine();
        }
        
        //this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote);
        //appointmentOperationsModule.appointmentMainMenu();
        //this.registrationOperationModule = new RegistrationOperationModule(appointmentEntityControllerRemote,patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote, queueEntityControllerRemote, null);
        //registrationOperationModule.registrationMainMenu();
        //this.administrationOperationsModule = new AdministrationOperationsModule(staffEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, appointmentEntityControllerRemote, consultationEntityControllerRemote);
        //administrationOperationsModule.administrationMainMenu();
        System.out.println("TERMINATING PROGRAM");
    }
    
    private void doLogin() throws InvalidLoginException
    {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CARS :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentStaffEntity = staffEntityControllerRemote.staffLogin(username, password);
        }
        else
        {
            throw new InvalidLoginException("Missing login credential!");
        }
    }
    
    private void menuMain() throws ParseException
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Main ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
            System.out.println("1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout\n");
            response = 0;
            
            while (response < 1 || response > 4)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    this.registrationOperationModule = new RegistrationOperationModule(appointmentEntityControllerRemote,patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote, queueEntityControllerRemote, null);
                    registrationOperationModule.registrationMainMenu();
                }
                else if(response == 2)
                {
                    this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, null);
                    appointmentOperationsModule.appointmentMainMenu();
                }
                else if(response == 3)
                {
                    this.administrationOperationsModule = new AdministrationOperationsModule(staffEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, appointmentEntityControllerRemote, consultationEntityControllerRemote);
                    administrationOperationsModule.administrationMainMenu();
                }
                else if(response == 4)
                {
                    return;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            sc.nextLine();
        }
    }
    
}
