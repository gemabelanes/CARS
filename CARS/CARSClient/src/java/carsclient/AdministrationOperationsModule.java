/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsclient;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.DoctorNotFoundException;
import util.exception.PatientNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author nicolechong
 */
public class AdministrationOperationsModule 
{
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    
    public AdministrationOperationsModule()
    {
    }
    
    public AdministrationOperationsModule(StaffEntityControllerRemote staffEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, AppointmentEntityControllerRemote appointmentEntityControllerRemote)
    {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
    }
    
    public void administrationMainMenu()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Administration Operation ***\n");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    doPatientManagement();
                }
                else if(response == 2)
                {
                    doDoctorManagement();
                }
                else if(response == 3)
                {
                    doStaffManagement();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
                
                if(response == 4)
                {
                    break;
                }
            }
        }
    }
    
    private void doPatientManagement()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management ***\n");
        System.out.println("1: Add Patient");
        System.out.println("2: View Patient Details");
        System.out.println("3: Update Patient");
        System.out.println("4: Delete Patient");
        System.out.println("5: View All Patients");
        System.out.println("6: Back\n");
        response = 0;
        
        while(true)
        {
            System.out.print("> ");
            response = 0;
            
            if(response == 1)
            {
                doAddPatient();
            }
            else if(response == 2)
            {
                doViewPatientDetails();
            }
            else if(response == 3)
            {
                doUpdatePatient();
            }
            else if(response == 4)
            {
                doDeletePatient();
            }
            else if(response == 5)
            {
                doViewAllPatients();
            }
            else if(response == 6)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void doAddPatient()
    {
        Scanner sc = new Scanner(System.in);
        PatientEntity newPatientEntity = new PatientEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Add Patient ***");
        
        System.out.print("Enter Identity Number> ");
        newPatientEntity.setIdentityNumber(sc.nextLine().trim());
        System.out.print("Enter First Name> ");
        newPatientEntity.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newPatientEntity.setLastName(sc.nextLine().trim());
        System.out.print("Enter Gender> ");
        newPatientEntity.setGender(sc.nextLine().trim());
        System.out.print("Enter Age> ");
        newPatientEntity.setAge(sc.nextInt());
        sc.nextLine();
        System.out.print("Enter Phone> ");
        newPatientEntity.setPhoneNumber(sc.nextLine().trim());
        System.out.print("Enter Address> ");
        newPatientEntity.setAddress(sc.nextLine().trim());
        
        Long newPatientEntityId = patientEntityControllerRemote.createNewPatient(newPatientEntity);
        System.out.println("Patient has been added successfully!");
    }
    
    private void doViewPatientDetails()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: View Patient Details ***\n");
        System.out.println("Enter Patient Identity Number> ");
        String identityNumber = sc.nextLine().trim();
        
        PatientEntity patientEntity = new PatientEntity();
        
        try
        {
            patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(identityNumber);
            System.out.printf("%8s%15s%15s%15s%15s%15s%15s%15s\n", "Patient ID", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
            System.out.printf("%8s%15s%15s%15s%15s%15s%15s%15s\n", patientEntity.getPatientId().toString(), patientEntity.getIdentityNumber(), patientEntity.getFirstName(), patientEntity.getLastName(), patientEntity.getGender(), patientEntity.getAddress(), patientEntity.getPhoneNumber(), patientEntity.getAddress());
        }
        catch(PatientNotFoundException ex)
        {
            System.out.println("An error has occured while retrieving patient: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdatePatient()
    {
        Scanner sc = new Scanner(System.in);
        String input;
        PatientEntity patientEntity = new PatientEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Update Patient ***\n");
        System.out.println("Enter Identity Number> ");
        input = sc.nextLine().trim();
        patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(input);
        
        System.out.print("Enter First Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setFirstName(input);
        }
        
        System.out.print("Enter Last Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setLastName(input);
        }
        
        System.out.print("Enter Gender (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setGender(input);
        }
        
        System.out.print("Enter Age (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setAge(Integer.parseInt(input));
        }
        
        System.out.print("Enter Phone (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setLastName(input);
        }
        
        System.out.print("Enter Address (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            patientEntity.setLastName(input);
        }
        
        patientEntityControllerRemote.updatePatientEntity(patientEntity);
        System.out.println("Patient updated successfully!\n");
    }
    
    private void doDeletePatient()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Delete Patient ***\n");
        System.out.print("Enter patient identity number> ");
        String identityNumber = sc.nextLine().trim();
        
        PatientEntity patientEntity = new PatientEntity();
        patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(identityNumber);
        System.out.printf("Confirm Delete Patient %s %s (Patient ID: %d) (Enter 'Y' to Delete)> ", patientEntity.getFirstName(), patientEntity.getLastName(), patientEntity.getPatientId());
        
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                List<AppointmentEntity> patientAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByPatient(patientEntity);
                for (AppointmentEntity appointment: patientAppointments) 
                {
                    patientEntity.removeAppointment(appointment);
                    appointment.getDoctorEntity().removeAppointment(appointment);
                    appointmentEntityControllerRemote.deleteAppointmentEntityById(appointment.getAppointmentId());
                }
                
                patientEntityControllerRemote.deletePatientEntityById(patientEntity.getPatientId());
                System.out.println("Patient deleted successfully!\n");
            }
            catch (PatientNotFoundException ex)
            {
                System.out.println("An error has occurred while deleting the patient: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Patient NOT deleted!\n");
        }
    }
    
    private void doViewAllPatients()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: View All Patients ***\n");
        
        List<PatientEntity> patientEntities = patientEntityControllerRemote.retrieveAllPatientEntities();
        System.out.printf("%8s%15s%15s%15s%15s%15s%15s%15s\n", "Patient ID", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
            
        for(PatientEntity patientEntity : patientEntities)
        {
            System.out.printf("%8s%15s%15s%15s%15s%15s%15s%15s\n", patientEntity.getPatientId().toString(), patientEntity.getIdentityNumber(), patientEntity.getFirstName(), patientEntity.getLastName(), patientEntity.getGender(), patientEntity.getAddress(), patientEntity.getPhoneNumber(), patientEntity.getAddress());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doDoctorManagement()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management ***\n");
        System.out.println("1: Add Doctor");
        System.out.println("2: View Doctor Details");
        System.out.println("3: Update Doctor");
        System.out.println("4: Delete Doctor");
        System.out.println("5: View All Doctors");
        System.out.println("6: Back\n");
        response = 0;
        
        while(true)
        {
            System.out.print("> ");
            response = 0;
            
            if(response == 1)
            {
                doAddDoctor();
            }
            else if(response == 2)
            {
                doViewDoctorDetails();
            }
            else if(response == 3)
            {
                doUpdateDoctor();
            }
            else if(response == 4)
            {
                doDeleteDoctor();
            }
            else if(response == 5)
            {
                doViewAllDoctors();
            }
            else if(response == 6)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void doAddDoctor()
    {
        Scanner sc = new Scanner(System.in);
        DoctorEntity newDoctorEntity = new DoctorEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Add Doctor ***");
        System.out.print("Enter First Name> ");
        newDoctorEntity.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newDoctorEntity.setLastName(sc.nextLine().trim());
        System.out.print("Enter Registration> ");
        newDoctorEntity.setRegistration(sc.nextLine().trim());
        System.out.print("Enter Qualifications> ");
        newDoctorEntity.setQualifications(sc.nextLine().trim());
        
        Long newDoctorEntityId = doctorEntityControllerRemote.createNewDoctor(newDoctorEntity);
        System.out.println("Doctor has been added successfully!");
    }
    
    private void doViewDoctorDetails()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View Doctor Details ***\n");
        System.out.println("Enter Doctor Id> ");
        Long doctorId = sc.nextLong();
        sc.nextLine();
        
        DoctorEntity doctorEntity = new DoctorEntity();
        
        try
        {
            doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
            System.out.printf("%8s%20s%20s%20s%20s\n", "Doctor ID", "First Name", "Last Name", "Registration", "Qualifications");
            System.out.printf("%8s%20s%20s%20s%20s\n", doctorEntity.getDoctorId().toString(), doctorEntity.getFirstName(), doctorEntity.getLastName(), doctorEntity.getRegistration(), doctorEntity.getQualifications());
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occured while retrieving doctor: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateDoctor()
    {
        Scanner sc = new Scanner(System.in);
        String input;
        DoctorEntity doctorEntity = new DoctorEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Update Doctor ***\n");
        System.out.println("Enter DoctorId> ");
        Long doctorId = sc.nextLong();
        sc.nextLine();
        
        doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
        
        System.out.print("Enter First Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            doctorEntity.setFirstName(input);
        }
        
        System.out.print("Enter Last Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            doctorEntity.setLastName(input);
        }
        
        System.out.print("Enter Registration (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            doctorEntity.setRegistration(input);
        }
        
        System.out.print("Enter Qualifications (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            doctorEntity.setQualifications(input);
        }
        
        doctorEntityControllerRemote.updateDoctorEntity(doctorEntity);
        System.out.println("Doctor updated successfully!\n");
    }
    
    private void doDeleteDoctor()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Delete Doctor ***\n");
        System.out.print("Enter DoctorId> ");
        Long doctorId = sc.nextLong();
        sc.nextLine();
        
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
        System.out.printf("Confirm Delete Doctor %s %s (Doctor ID: %d) (Enter 'Y' to Delete)> ", doctorEntity.getFirstName(), doctorEntity.getLastName(), doctorEntity.getDoctorId());
        
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                List<AppointmentEntity> doctorAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByDoctor(doctorEntity);
                for (AppointmentEntity appointment: doctorAppointments) {
                    doctorEntity.removeAppointment(appointment);
                    appointment.getPatientEntity().removeAppointment(appointment);
                    appointmentEntityControllerRemote.deleteAppointmentEntityById(appointment.getAppointmentId());
                }
                
                doctorEntityControllerRemote.deleteDoctorEntityById(doctorEntity.getDoctorId());
                System.out.println("Doctor deleted successfully!\n");
            }
            catch (DoctorNotFoundException ex)
            {
                System.out.println("An error has occurred while deleting the doctor: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Doctor NOT deleted!\n");
        }
    }
    
    private void doViewAllDoctors()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View All Doctor ***\n");
        
        List<DoctorEntity> doctorEntities = doctorEntityControllerRemote.retrieveAllDoctorEntities();
        System.out.printf("%8s%20s%20s%20s%20s\n", "Doctor ID", "First Name", "Last Name", "Registration", "Qualifications");
        for(DoctorEntity doctorEntity : doctorEntities)
        {
            System.out.printf("%8s%20s%20s%20s%20s\n", doctorEntity.getDoctorId().toString(), doctorEntity.getFirstName(), doctorEntity.getLastName(), doctorEntity.getRegistration(), doctorEntity.getQualifications());    
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doStaffManagement()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
        System.out.println("1: Add Staff");
        System.out.println("2: View Staff Details");
        System.out.println("3: Update Staff");
        System.out.println("4: Delete Staff");
        System.out.println("5: View All Staff");
        System.out.println("6: Back\n");
        response = 0;
        
        while(true)
        {
            System.out.print("> ");
            response = 0;
            
            if(response == 1)
            {
                doAddStaff();
            }
            else if(response == 2)
            {
                doViewStaffDetails();
            }
            else if(response == 3)
            {
                doUpdateStaff();
            }
            else if(response == 4)
            {
                doDeleteStaff();
            }
            else if(response == 5)
            {
                doViewAllStaff();
            }
            else if(response == 6)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void doAddStaff()
    {
        Scanner sc = new Scanner(System.in);
        StaffEntity newStaffEntity = new StaffEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Add Staff ***");
   
        System.out.print("Enter First Name> ");
        newStaffEntity.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newStaffEntity.setLastName(sc.nextLine().trim());
        System.out.print("Enter Username> ");
        newStaffEntity.setUsername(sc.nextLine().trim());
        System.out.print("Enter Password> ");
        newStaffEntity.setPassword(sc.nextLine().trim());
        
        Long newStaffEntityId = staffEntityControllerRemote.createNewStaffEntity(newStaffEntity);
        System.out.println("Patient has been added successfully!");
    }
    
    private void doViewStaffDetails()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: View Staff Details ***\n");
        System.out.println("Enter StaffId> ");
        Long staffId = sc.nextLong();
        
        StaffEntity staffEntity = new StaffEntity();
        
        try
        {
            staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
            System.out.printf("%8s%20s%20s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Username", "Password");
            System.out.printf("%8s%20s%20s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUsername(), staffEntity.getPassword());
        }
        catch(StaffNotFoundException ex)
        {
            System.out.println("An error has occured while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateStaff()
    {
        Scanner sc = new Scanner(System.in);
        String input;
        StaffEntity staffEntity = new StaffEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Update Staff ***\n");
        System.out.println("Enter StaffId> ");
        Long staffId = sc.nextLong();
        staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
        
        System.out.print("Enter First Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setFirstName(input);
        }
        
        System.out.print("Enter Last Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setLastName(input);
        }
        
        System.out.print("Enter Username (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setUsername(input);
        }
        
        System.out.print("Enter Passworld (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setPassword(input);
        }
        
        
        staffEntityControllerRemote.updateStaffEntity(staffEntity);
        System.out.println("Staff updated successfully!\n");
    }
    
    private void doDeleteStaff()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Delete Staff ***\n");
        System.out.print("Enter StaffId> ");
        Long staffId = sc.nextLong();
        
        StaffEntity staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
        System.out.printf("Confirm Delete Staff %s %s (Patient ID: %d) (Enter 'Y' to Delete)> ", staffEntity.getFirstName(), staffEntity.getLastName(), staffId);
        
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                staffEntityControllerRemote.deleteStaffEntity(staffEntity);
                System.out.println("Staff deleted successfully!\n");
            }
            catch (StaffNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting the staff: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Staff NOT deleted!\n");
        }
    }
    
    private void doViewAllStaff()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: View All Staff ***\n");
        
        List<StaffEntity> staffEntities = staffEntityControllerRemote.retrieveAllStaffEntity();
        System.out.printf("%8s%20s%20s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Username", "Password");
            
        for(StaffEntity staffEntity : staffEntities)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUsername(), staffEntity.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
}
