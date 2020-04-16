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
import util.exception.PatientNotFoundException;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.ParseException_Exception;
import ws.client.PatientNotFoundException_Exception;

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
    
    
    public static void main(String[] args) throws AppointmentNotFoundException_Exception, ParseException, PatientNotFoundException, ParseException_Exception, PatientNotFoundException_Exception {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }
    
}
