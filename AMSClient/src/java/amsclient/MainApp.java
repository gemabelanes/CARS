package amsclient;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import entity.PatientEntity;
import java.text.ParseException;
import java.util.Scanner;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author nicolechong
 */
public class MainApp {
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    
    private AppointmentOperationsModule appointmentOperationsModule;
    
    private PatientEntity currentPatientEntity;
    
    public MainApp()
    {
    }
    
    public MainApp(AppointmentEntityControllerRemote appointmentEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote)
    {
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
    }
    
    public void runApp() throws ParseException
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to AMS Client ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    doRegister();
                    System.out.println("Registration is successful!");
                }
                else if(response ==2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, currentPatientEntity);
                        menuMain();
                    }
                    catch(InvalidLoginException ex)
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    private void doRegister() 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** AMS Client :: Register ***\n");
        
        
        while (true) 
        {
            System.out.print("Enter Identity Number> ");
            String ic = sc.nextLine();
            //System.out.println("DEBUG LINE 1 : " + patientEntityControllerRemote.doesPatientExistByIc(ic));
            if(!patientEntityControllerRemote.doesPatientExistByIc(ic)) {
                System.out.print("Enter Password> ");
                String password = sc.nextLine();
                System.out.print("Enter First Name> ");
                String firstName = sc.nextLine();
                System.out.print("Enter Last Name> ");
                String lastName = sc.nextLine();
                System.out.print("Enter Gender> ");
                String gender = sc.nextLine();
                Integer age;
                while (true) {
                    try {
                        System.out.print("Enter Age> ");
                        age = sc.nextInt();
                        sc.nextLine();
                        break;
                    } catch (Exception ex) {
                        System.out.println("Please enter a valid integer.");
                    }
                }
                System.out.print("Enter Phone Number> ");
                String phoneNumber = sc.nextLine();
                System.out.print("Enter Address> ");
                String address = sc.nextLine();
                
                PatientEntity newPatient = new PatientEntity(ic, firstName, lastName, gender, age, phoneNumber, address, password);
                patientEntityControllerRemote.createNewPatient(newPatient);
                try {
                    System.out.println("Patient ID : " + patientEntityControllerRemote.retrievePatientEntityByIc(ic).getPatientId() + " has been registered successfully!");
                } catch (PatientNotFoundException ex) {
                    System.out.println("Error retrieving Patient IC : " + ic);
                }
                break;
            } else {
                System.out.println("Patient already exists! Please try again.");
            }
        }
    }
    
    private void doLogin() throws InvalidLoginException
    {
        Scanner sc = new Scanner(System.in);
        String identityNumber = "";
        String password = "";
        
        System.out.println("*** AMS Client :: Login ***\n");
        System.out.println("Enter Identity Number> ");
        identityNumber = sc.nextLine().trim();
        System.out.println("Enter password> ");
        password = sc.nextLine().trim();
        
        if(identityNumber.length() > 0 && password.length() > 0)
        {
            currentPatientEntity = patientEntityControllerRemote.patientLogin(identityNumber, password);
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
            System.out.println("*** AMS Client :: Main ***\n");
            System.out.println("You are login as " + currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + "\n");
            System.out.println("1: View Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Logout\n");
            response = 0;
            
            while (response < 1 || response > 4)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    appointmentOperationsModule.viewPatientAppointments();
                }
                else if(response == 2)
                {
                    appointmentOperationsModule.addAppointment();
                }
                else if(response == 3)
                {
                    appointmentOperationsModule.cancelAppointment();
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
