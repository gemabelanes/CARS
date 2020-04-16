/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekiosk;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.ConsultationEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ejb.session.stateless.QueueEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
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
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    private ConsultationEntityControllerRemote consultationEntityControllerRemote;
    private QueueEntityControllerRemote queueEntityControllerRemote;
    
    private RegistrationOperationModule registrationOperationModule;
    private AppointmentOperationsModule appointmentOperationsModule;
    
    private PatientEntity currentPatientEntity;
    
    public MainApp()
    {
    }
    
    public MainApp(QueueEntityControllerRemote queueEntityControllerRemote, AppointmentEntityControllerRemote appointmentEntityControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote)
    {
        this.queueEntityControllerRemote = queueEntityControllerRemote;
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
    }
    
    public void runApp() throws ParseException
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Self-Service Kiosk ***");
            System.out.println("Enter 0 at any point to return to previous menu.\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit\n");
            response = -1;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        this.registrationOperationModule = new RegistrationOperationModule(appointmentEntityControllerRemote,patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote, queueEntityControllerRemote, currentPatientEntity);
                        this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, currentPatientEntity);
                        menuMain();
                    }
                    catch(InvalidLoginException ex)
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response ==2)
                {
                    doRegister();
                    
                }
                else if(response == 3 || response == 0)
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
    
    private void doLogin() throws InvalidLoginException
    {
        Scanner sc = new Scanner(System.in);
        String identityNumber = "";
        String password = "";
        
        System.out.println("*** Self-Service Kiosk :: Login ***\n");
        System.out.print("Enter Identity Number> ");
        identityNumber = sc.nextLine().trim();
        System.out.print("Enter password> ");
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
    
    private void doRegister() 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Register ***\n");
        
        
        while (true) 
        {
            String ic;
            while (true) {
                System.out.print("Enter Identity Number> ");
                ic = sc.nextLine();
                if(ic.equals("0")) {
                    return;
                }
                if(ic.trim().length() > 0) {
                    break;
                }
            }
            //System.out.println("DEBUG LINE 1 : " + patientEntityControllerRemote.doesPatientExistByIc(ic));
            if(!patientEntityControllerRemote.doesPatientExistByIc(ic)) {
                String password;
                while (true) {
                    System.out.print("Enter Password> ");
                    password = sc.nextLine();
                    if(password.equals("0")) {
                        return;
                    }
                    boolean isNumeric = password.chars().allMatch( Character::isDigit );
                    if(isNumeric && (password.length() == 6)) {
                        break;
                    } else {
                        System.out.println("Please enter a valid 6 pin NUMERIC password.");
                    }
                    
                }
                String firstName;
                while (true) {
                    System.out.print("Enter First Name> ");
                    firstName = sc.nextLine();
                    if(firstName.equals("0")) {
                        return;
                    }
                    if(firstName.trim().length() > 0) {
                        break;
                    }
                }
                String lastName;
                while (true) {
                    System.out.print("Enter First Name> ");
                    lastName = sc.nextLine();
                    if(lastName.equals("0")) {
                        return;
                    }
                    if(lastName.trim().length() > 0) {
                        break;
                    }
                }
                String gender;
                while (true) {
                    System.out.print("Enter gender > ");
                    gender = sc.nextLine();
                    if(gender.equals("0")) {
                        return;
                    }
                    if(gender.trim().length() > 0) {
                        break;
                    }
                }
                Integer age;
                while (true) {
                    try {
                        System.out.print("Enter Age> ");
                        age = sc.nextInt();
                        sc.nextLine();
                        if(age == 0) {
                            return;
                        }
                        break;
                    } catch (Exception ex) {
                        System.out.println("Please enter a valid integer.");
                        sc.nextLine();
                    }
                }
                String phoneNumber;
                while (true) {
                    System.out.print("Enter Phone Number> ");
                    phoneNumber = sc.nextLine();
                    if(phoneNumber.equals("0")) {
                        return;
                    }
                    if(phoneNumber.trim().length() > 0) {
                        break;
                    }
                }
                String address;
                while (true) {
                    System.out.print("Enter Address> ");
                    address = sc.nextLine();
                    if(address.equals("0")) {
                        return;
                    }
                    if(address.trim().length() > 0) {
                        break;
                    }
                }
                
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
    
    private void menuMain() throws ParseException
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Self-Service Kiosk :: Main ***");
            System.out.println("You are login as " + currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + "\n");
            System.out.println("1: Register Walk-In Consultation");
            System.out.println("2: Register Consultation By Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Add Appointment");
            System.out.println("5: Cancel Appointment");
            System.out.println("6: Logout\n");
            response = 0;
            
            while (response < 1 || response > 6)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    registrationOperationModule.registerWalkInConsultation();
                }
                else if(response == 2)
                {
                    registrationOperationModule.registerConsultationAppointment();
                }
                else if(response == 3)
                {
                    appointmentOperationsModule.viewPatientAppointments();
                }
                else if(response == 4)
                {
                    appointmentOperationsModule.addAppointment();
                }
                else if(response == 5)
                {
                    appointmentOperationsModule.cancelAppointment();
                }
                else if(response == 6 || response == 0)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
        System.out.println("TERMINATING PROGRAM\n");
    }
    
}
