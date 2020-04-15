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
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.AppointmentEntity;
import entity.ConsultationEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.DoctorNotFoundException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.DoctorRemoveConsultationException;
import util.exception.PatientNotFoundException;
import util.exception.PatientRemoveAppointmentException;
import util.exception.PatientRemoveConsultationException;
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
    private ConsultationEntityControllerRemote consultationEntityControllerRemote;
    
    public AdministrationOperationsModule()
    {
    }
    
    public AdministrationOperationsModule(StaffEntityControllerRemote staffEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, AppointmentEntityControllerRemote appointmentEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote)
    {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
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
            response = -1;
            
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
                else if(response == 4 || response == 0)
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
    
    private void doPatientManagement()
    {
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("*** CARS :: Administration Operation :: Patient Management ***\n");
            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back\n");
            Integer response = -1;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

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
                else if(response == 6 || response == 0)
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
    
    private void doAddPatient()
    {
        Scanner sc = new Scanner(System.in);
        PatientEntity newPatientEntity = new PatientEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Add Patient ***");
        
        while (true) {
            System.out.print("Enter Identity Number> ");
            String ic = sc.nextLine().trim();
            if(!patientEntityControllerRemote.doesPatientExistByIc(ic)) {
                newPatientEntity.setIdentityNumber(ic);
                break;
            } else {
                System.out.println("Patient IC : " + ic + " already exists! Please try again.");
            }
        }
        System.out.print("Enter First Name> ");
        newPatientEntity.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newPatientEntity.setLastName(sc.nextLine().trim());
        System.out.print("Enter Gender> ");
        newPatientEntity.setGender(sc.nextLine().trim());
        while (true) {
            try {
                System.out.print("Enter Age> ");
                int age = sc.nextInt();
                newPatientEntity.setAge(age);
                sc.nextLine();
                break;
            } catch (Exception ex) {
                System.out.println("Please input a valid integer.");
                sc.nextLine();
            }
        }
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
        while (true) {
            System.out.print("Enter Patient Identity Number> ");
            String identityNumber = sc.nextLine().trim();

            PatientEntity patientEntity = new PatientEntity();
            try
            {
                patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(identityNumber);
                System.out.printf("%-15s%-20s%-20s%-20s%-10s%-10s%-15s%-30s\n", "Patient ID ", "| Identity Number ", "| First Name", "| Last Name ", "| Gender ", "| Age ", "| Phone ", "| Address ");
                System.out.printf("%-15s%-20s%-20s%-20s%-10s%-10s%-15s%-30s\n", patientEntity.getPatientId().toString(), "| " + patientEntity.getIdentityNumber(), "| " + patientEntity.getFirstName(), "| " + patientEntity.getLastName(), "| " + patientEntity.getGender(), "| " + patientEntity.getAge(), "| " + patientEntity.getPhoneNumber(), "| " + patientEntity.getAddress());
                break;
            }
            catch(PatientNotFoundException ex)
            {
                System.out.println("Patient IC : " + identityNumber + " does not exist! Please try again.");
            }
        }
    }
    
    private void doUpdatePatient()
    {
        Scanner sc = new Scanner(System.in);
        PatientEntity patientEntity = new PatientEntity();
        String input;
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Update Patient ***\n");
        while(true) {
            System.out.print("Enter Identity Number> ");
            input = sc.nextLine().trim();
            try {
                patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(input);
                break;
            } catch (PatientNotFoundException ex) {
                System.out.println("Patient IC : " + input + " does not exist! Please try again.");
            }
            
        }
        
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
        
        
        while(true) {
            System.out.print("Enter Age (blank if no change)> ");
            int age;
            try {
                input = sc.nextLine().trim();
                //sc.nextLine();
                if(input.length() > 0)
                {
                    age = Integer.parseInt(input);
                    patientEntity.setAge(Integer.parseInt(input));
                }
                break;
            } catch (Exception ex) {
                System.out.println("Please enter a valid integer.");
                //sc.nextLine();
            }

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
        PatientEntity patientEntity;
        while(true) {
            System.out.print("Enter patient identity number> ");
            String identityNumber = sc.nextLine().trim();
            try {
                patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(identityNumber);
                break;
            } catch (PatientNotFoundException ex) {
                System.out.println("Patient IC : " + identityNumber + " does not exist! Please try again.");
                
            }
        }
        
        System.out.printf("Confirm Delete Patient %s %s (Patient ID: %d) (Enter 'Y' to Delete)> ", patientEntity.getFirstName(), patientEntity.getLastName(), patientEntity.getPatientId());
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            List<AppointmentEntity> patientAppointments = patientEntity.getPatientAppointments();
            List<ConsultationEntity> consultationAppointments = patientEntity.getPatientConsultations();
            for (AppointmentEntity appointment: patientAppointments) 
            {
                try {
                    //patientEntity.removeAppointment(appointment);
                    //appointment.getDoctorEntity().removeAppointment(appointment);
                    appointmentEntityControllerRemote.deleteAppointmentEntityById(appointment.getAppointmentId());
                } catch (DoctorRemoveAppointmentException | PatientRemoveAppointmentException ex) {
                    System.out.println("Error deleting appointment id : " + appointment.getAppointmentId() + " due to " + ex );
                }

            }

            for(ConsultationEntity consultationEntity : consultationAppointments) {
                try {
                    consultationEntityControllerRemote.deleteConsultationEntityById(consultationEntity.getConsultationId());
                } catch (DoctorRemoveConsultationException | PatientRemoveConsultationException ex) {
                    System.out.println("Error deleting consultation id : " + consultationEntity.getConsultationId() + " due to " + ex );
                }

            }

            patientEntityControllerRemote.deletePatientEntityById(patientEntity.getPatientId());
            System.out.println("Patient deleted successfully!\n");
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
        if(patientEntities.isEmpty()) {
            System.out.println("No patient records found");
        } else {
            System.out.printf("%-15s%-20s%-20s%-20s%-10s%-10s%-15s%-30s\n", "Patient ID ", "| Identity Number ", "| First Name", "| Last Name ", "| Gender ", "| Age ", "| Phone ", "| Address ");
            for(PatientEntity patientEntity : patientEntities)
            {
                System.out.printf("%-15s%-20s%-20s%-20s%-10s%-10s%-15s%-30s\n", patientEntity.getPatientId().toString(), "| " + patientEntity.getIdentityNumber(), "| " + patientEntity.getFirstName(), "| " + patientEntity.getLastName(), "| " + patientEntity.getGender(), "| " + patientEntity.getAge(), "| " + patientEntity.getPhoneNumber(), "| " + patientEntity.getAddress());
            
            }
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doDoctorManagement()
    {
        Scanner sc = new Scanner(System.in);
        
        
        while(true)
        {
        System.out.println("*** CARS :: Administration Operation :: Doctor Management ***\n");
        System.out.println("1: Add Doctor");
        System.out.println("2: View Doctor Details");
        System.out.println("3: Update Doctor");
        System.out.println("4: Delete Doctor");
        System.out.println("5: View All Doctors");
        System.out.println("6: Back\n");
        Integer response = -1;
        
            while(response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

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
                else if(response == 6 || response == 0)
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
        
        while(true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View Doctor Details ***\n");
            Long doctorId;
            while (true) {
                try {
                    System.out.print("Enter Doctor Id> ");
                    doctorId = sc.nextLong();
                    sc.nextLine();
                    break;
                } catch (Exception ex) {
                    System.out.println("Please enter a valid integer.");
                }
            }
            try
            {
                DoctorEntity doctorEntity = new DoctorEntity();
                doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
                System.out.printf("%-15s%-20s%-20s%-20s%-30s\n", "Doctor ID", "| First Name", "| Last Name", "| Registration", "| Qualifications");
                System.out.printf("%-15s%-20s%-20s%-20s%-30s\n", doctorEntity.getDoctorId().toString(), "| " + doctorEntity.getFirstName(), "| " + doctorEntity.getLastName(), "| " + doctorEntity.getRegistration(), "| " + doctorEntity.getQualifications());
                break;
            }
            catch(DoctorNotFoundException ex)
            {
                System.out.println(ex + "\n");
            }
        }
    }
    
    private void doUpdateDoctor()
    {
        Scanner sc = new Scanner(System.in);
        String input;
        DoctorEntity doctorEntity = new DoctorEntity();
        Long doctorId;
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Update Doctor ***\n");
        
        while(true) {
            System.out.print("Enter DoctorId> ");
            doctorId = sc.nextLong();
            sc.nextLine();
            try {
                doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
                break;
            } catch (DoctorNotFoundException ex) {
                System.out.println(ex + "\n");
            }
        }
        
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
        Long doctorId;
        DoctorEntity doctorEntity;
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Delete Doctor ***\n");
        while(true) {
            System.out.print("Enter Doctor Id> ");
            doctorId = sc.nextLong();
            sc.nextLine();
            try {
                doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
                System.out.printf("Confirm Delete Doctor %s %s (Doctor ID: %d) (Enter 'Y' to Delete)> ", doctorEntity.getFirstName(), doctorEntity.getLastName(), doctorEntity.getDoctorId());
                break;
            } catch (DoctorNotFoundException ex) {
                System.out.println(ex + "\n");
            }
        }
            
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            List<AppointmentEntity> doctorAppointments = doctorEntity.getDoctorAppointments();
            List<ConsultationEntity> doctorConsultations = doctorEntity.getConsultations();
            for (AppointmentEntity appointment: doctorAppointments) {
                try {
                    //doctorEntity.removeAppointment(appointment);
                    //appointment.getPatientEntity().removeAppointment(appointment);
                    appointmentEntityControllerRemote.deleteAppointmentEntityById(appointment.getAppointmentId());
                } catch (DoctorRemoveAppointmentException | PatientRemoveAppointmentException ex) {
                    System.out.println("Error deleting appointment id : " + appointment.getAppointmentId() + " due to " + ex);
                }
            }

            for(ConsultationEntity consultationEntity: doctorConsultations) {
                try {
                    consultationEntityControllerRemote.deleteConsultationEntityById(consultationEntity.getConsultationId());
                } catch (DoctorRemoveConsultationException | PatientRemoveConsultationException ex) {
                   System.out.println("Error deleting consultation id : " + consultationEntity.getConsultationId() + " due to " + ex);
                }
            }

            doctorEntityControllerRemote.deleteDoctorEntityById(doctorEntity.getDoctorId());
            System.out.println("Doctor deleted successfully!\n");
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
        if(doctorEntities.isEmpty()) {
            System.out.println("No doctor records found");
        } else {
            System.out.printf("%-15s%-20s%-20s%-20s%-30s\n", "Doctor ID", "| First Name", "| Last Name", "| Registration", "| Qualifications");
                for(DoctorEntity doctorEntity : doctorEntities)
            {
               System.out.printf("%-15s%-20s%-20s%-20s%-30s\n", doctorEntity.getDoctorId().toString(), "| " + doctorEntity.getFirstName(), "| " + doctorEntity.getLastName(), "| " + doctorEntity.getRegistration(), "| " + doctorEntity.getQualifications());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doStaffManagement()
    {
        Scanner sc = new Scanner(System.in);
        
        while (true) {
        System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
        System.out.println("1: Add Staff");
        System.out.println("2: View Staff Details");
        System.out.println("3: Update Staff");
        System.out.println("4: Delete Staff");
        System.out.println("5: View All Staff");
        System.out.println("6: Back\n");
        Integer response = -1;
        
            while(response < 1 || response > 6) 
            {
                System.out.print("> ");
                response = sc.nextInt();

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
                else if(response == 6 || response == 0)
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
        
        while (true) {
            StaffEntity staffEntity = new StaffEntity();
            System.out.print("Enter Staff Id> ");
            Long staffId = sc.nextLong();
            try
            {
                staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
                System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", "Staff ID", "| First Name", "| Last Name", "| Username", "| Password");
                System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", staffEntity.getStaffId().toString(), "| " + staffEntity.getFirstName(), "| " + staffEntity.getLastName(), "| " + staffEntity.getUsername(), "| " + staffEntity.getPassword());
                break;
            }
            catch(StaffNotFoundException ex)
            {
                System.out.println("Staff ID : " + staffId + "does not exist! Please try again.");
            }
        }
    }
    
    private void doUpdateStaff()
    {
        Scanner sc = new Scanner(System.in);
        String input;
        StaffEntity staffEntity = new StaffEntity();
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Update Staff ***\n");
        while (true) {
            Long staffId;
            while (true) {
                System.out.print("Enter StaffId> ");
                try {
                    staffId = sc.nextLong();
                    sc.nextLine();
                    break;
                } catch (Exception ex) {
                    System.out.println("Please enter a valid integer.");
                    sc.nextLine();
                }
            }
            try {
                staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
                break;
            } catch (StaffNotFoundException ex) {
                System.out.println("Staff ID : " + staffId + " does not exist! Please try again.");
            }
        }
        
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
        
        System.out.print("Enter Password (blank if no change)> ");
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
        Long staffId;
        StaffEntity staffEntity;
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Delete Staff ***\n");
        while(true) {
            
            while(true) {
                try {
                System.out.print("Enter StaffId> ");
                staffId = sc.nextLong();
                sc.nextLine();
                break;
                } catch (Exception ex) {
                    System.out.println("Please enter a valid integer");
                    sc.nextLine();
                }
            }
            
            try {
                staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
                System.out.printf("Confirm Delete Staff %s %s (Patient ID: %d) (Enter 'Y' to Delete)> ", staffEntity.getFirstName(), staffEntity.getLastName(), staffId);
                break;
            } catch (StaffNotFoundException ex) {
                System.out.println("Staff ID : " + staffId + " does not exist! Please try again");
            }
        }
        String input;
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                staffEntityControllerRemote.deleteStaffEntityById(staffEntity.getStaffId());
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
        if(staffEntities.isEmpty()) {
            System.out.println("No staff records found");
        } else {
            System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", "Staff ID", "| First Name", "| Last Name", "| Username", "| Password");
            
            for(StaffEntity staffEntity : staffEntities)
            {
                 System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", staffEntity.getStaffId().toString(), "| " + staffEntity.getFirstName(), "| " + staffEntity.getLastName(), "| " + staffEntity.getUsername(), "| " + staffEntity.getPassword());
            }
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
}
