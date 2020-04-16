/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amsclient;

import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.ParseException_Exception;
import ws.client.PatientEntity;
import ws.client.PatientNotFoundException_Exception;

/**
 *
 * @author gem
 */
public class MainApp {
    
    private PatientEntity currentPatientEntity;
    private AppointmentOperationsModule appointmentOperationsModule;

    public MainApp() {
    }
    
    public void runApp() throws ParseException_Exception
    {
        System.out.println("STARTING UP...\n");
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to AMS Client ***");
            System.out.println("Enter 0 at any point to return to previous menu.\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = -1;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    doRegister();
                }
                else if(response == 2)
                {
                    try
                    {
                        doLogin();
                        if(currentPatientEntity != null) {
                            System.out.println("Login successful!\n");
                            this.appointmentOperationsModule = new AppointmentOperationsModule(currentPatientEntity);
                            menuMain();
                        } else {
                            System.out.println("Login failed");
                        }
                    }
                    catch(InvalidLoginException ex)
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
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
            
            if(response == 3 || response == 0)
            {
                break;
            }
        }
        System.out.println("TERMINATING PROGRAM\n");
    }
    
    
    private void menuMain() throws ParseException_Exception
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
            response = -1;
            
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
                else if(response == 4 || response == 0)
                {
                    currentPatientEntity = null;
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 0 || response == 4)
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
            if(!doesPatientExistByIc(ic)) {
                String password;
                while (true) {
                    System.out.print("Enter Password> ");
                    password = sc.nextLine();
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
                    if(firstName.trim().length() > 0) {
                        break;
                    }
                }
                String lastName;
                while (true) {
                    System.out.print("Enter First Name> ");
                    lastName = sc.nextLine();
                    if(lastName.trim().length() > 0) {
                        break;
                    }
                }
                String gender;
                while (true) {
                    System.out.print("Enter gender > ");
                    gender = sc.nextLine();
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
                        break;
                    } catch (Exception ex) {
                        System.out.println("Please enter a valid integer.");
                    }
                }
                String phoneNumber;
                while (true) {
                    System.out.print("Enter Phone Number> ");
                    phoneNumber = sc.nextLine();
                    if(phoneNumber.trim().length() > 0) {
                        break;
                    }
                }
                String address;
                while (true) {
                    System.out.print("Enter Address> ");
                    address = sc.nextLine();
                    if(address.trim().length() > 0) {
                        break;
                    }
                }
                
                createNewPatient(ic, password, firstName, lastName, gender, age, phoneNumber, address);
                
                try {
                    System.out.println("Patient ID : " + retrievePatientByIc(ic).getPatientId() + " has been registered successfully!");
                } catch (PatientNotFoundException_Exception ex) {
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
        System.out.print("Enter Identity Number> ");
        identityNumber = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if(identityNumber.length() > 0 && password.length() > 0)
        {
            try {
                currentPatientEntity = patientLogin(identityNumber, password);
            } catch (InvalidLoginException_Exception ex) {
                throw new InvalidLoginException("An error occurred while logging in");
            }
        }
        else
        {
            throw new InvalidLoginException("Missing login credential!");
        }
    }
    
    private static PatientEntity patientLogin(java.lang.String identityNumber, java.lang.String password) throws InvalidLoginException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.patientLogin(identityNumber, password);
    }

   

    private static boolean doesPatientExistByIc(java.lang.String arg0) {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.doesPatientExistByIc(arg0);
    }

    private static Long createNewPatient(java.lang.String identityNumber, java.lang.String password, java.lang.String firstName, java.lang.String lastName, java.lang.String gender, java.lang.Integer age, java.lang.String phoneNumber, java.lang.String address) {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.createNewPatient(identityNumber, password, firstName, lastName, gender, age, phoneNumber, address);
    }

    private static PatientEntity retrievePatientByIc(java.lang.String identityNumber) throws PatientNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrievePatientByIc(identityNumber);
    }
    
    
    
}
