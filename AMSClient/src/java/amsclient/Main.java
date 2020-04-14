/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amsclient;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author nicolechong
 */
public class Main {

   
    @EJB
    private static PatientEntityControllerRemote patientEntityControllerRemote;
    @EJB
    private static DoctorEntityControllerRemote doctorEntityControllerRemote;
    @EJB
    private static AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    
    
    public static void main(String[] args) throws ParseException {
        MainApp mainApp = new MainApp(appointmentEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote);
        mainApp.runApp();
    }
    
}
