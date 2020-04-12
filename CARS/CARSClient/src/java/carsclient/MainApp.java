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
    
    private StaffEntity currentStaffEntity;

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
        System.out.println("RUNNING REGISTRATION OPERATIONS MODULE TEST");
        //this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote);
        //appointmentOperationsModule.appointmentMainMenu();
        this.registrationOperationModule = new RegistrationOperationModule(appointmentEntityControllerRemote,patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote, queueEntityControllerRemote);
        registrationOperationModule.registrationMainMenu();
        System.out.println("TERMINATING PROGRAM");
    }
    
    private void doLogin() throws InvalidLoginException
    {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CARS :: Login ***\n");
        System.out.println("Enter username> ");
        username = sc.nextLine().trim();
        System.out.println("Enter password> ");
        password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentStaffEntity = staffEntitySessionBeanRemote.staffLogin(username, password);
        }
        else
        {
            throw new InvalidLoginException("Missing login credential!");
        }
    }
    
    private void menuMain()
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
                    registrationModule.registrationMenuOperation();
                }
                else if(response == 2)
                {
                    appointmentModule.appointmentMenuOperation();
                }
                else if(response == 3)
                {
                    administrationModule.adminstrationMenuOperation();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
}
