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
import javax.ejb.EJB;

/**
 *
 * @author gem
 */
public class Main {

    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    @EJB
    private static PatientEntityControllerRemote patientEntityControllerRemote;
    @EJB
    private static DoctorEntityControllerRemote doctorEntityControllerRemote;
    @EJB
    private static AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    @EJB
    private static ConsultationEntityControllerRemote consultationEntityControllerRemote;
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(staffEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, appointmentEntityControllerRemote, consultationEntityControllerRemote);
        mainApp.runApp();
    }
    
}
