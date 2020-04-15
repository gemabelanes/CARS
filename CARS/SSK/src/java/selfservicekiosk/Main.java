package selfservicekiosk;

import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.ConsultationEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ejb.session.stateless.QueueEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author nicolechong
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
    @EJB
    private static QueueEntityControllerRemote queueEntityControllerRemote;
    
    public static void main(String[] args) throws ParseException {
        MainApp mainApp = new MainApp(queueEntityControllerRemote, appointmentEntityControllerRemote, staffEntityControllerRemote, patientEntityControllerRemote, doctorEntityControllerRemote, consultationEntityControllerRemote);
        mainApp.runApp();
    }
    
}
