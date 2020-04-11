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
import entity.StaffEntity;

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
  
    private AppointmentOperationsModule appointmentOperationsModule;
    private RegistrationOperationModule registrationOperationModule;
    
    private StaffEntity currentStaffEntity;

    public MainApp(StaffEntityControllerRemote staffEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, AppointmentEntityControllerRemote appointmentEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
    }
    
    public void runApp() {
        System.out.println("STARTING UP...");
        System.out.println("RUNNING REGISTRATION OPERATIONS MODULE TEST");
        //this.appointmentOperationsModule = new AppointmentOperationsModule(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote);
        //appointmentOperationsModule.appointmentMainMenu();
        this.registrationOperationModule = new RegistrationOperationModule(appointmentEntityControllerRemote,patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote);
        registrationOperationModule.registrationMainMenu();
        System.out.println("TERMINATING PROGRAM");
    }
    
}
