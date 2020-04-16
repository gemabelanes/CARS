package amsclient;

import com.sun.istack.Nullable;
import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ws.client.AppointmentEntity;
import ws.client.DoctorEntity;
import ws.client.PatientEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateAppointmentException;
import util.exception.DoctorNotFoundException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.PatientNotFoundException;
import util.exception.PatientRemoveAppointmentException;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.CreateAppointmentException_Exception;
//import ws.client.DeleteAppointmentExceptionWs_Exception;
import ws.client.DoctorNotFoundException_Exception;
import ws.client.DoctorRemoveAppointmentException_Exception;
import ws.client.ParseException_Exception;
import ws.client.PatientNotFoundException_Exception;
import ws.client.PatientRemoveAppointmentException_Exception;

/**
 *
 * @author nicolechong
 */
public class AppointmentOperationsModule {
    SimpleDateFormat sdf2;
    SimpleDateFormat sdf3;
    
    PatientEntity loggedInPatient;

    public AppointmentOperationsModule() {
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf3 = new SimpleDateFormat("HH:mm");
    }

    public AppointmentOperationsModule(PatientEntity loggedInPatient) {
        this();
        this.loggedInPatient = loggedInPatient;
                
    }
    
    void appointmentMainMenu() throws ParseException_Exception, ParseException {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("*** CARS :: Appointment Operation ***");
            System.out.println();
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back");
            System.out.println();
            Integer response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    viewPatientAppointments();
                } else if (response == 2) {
                    addAppointment();
                } else if (response == 3) {
                    cancelAppointment();
                } else if (response == 4 || response == 0) {
                    return;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
                
                
            }
            sc.nextLine();
            //break;
      
        }
        
    }

    void viewPatientAppointments()  {
       Scanner sc = new Scanner(System.in);
       System.out.println("*** Automated Machine Service :: View Appointments ***\n");
           PatientEntity patientEntity;
           List<AppointmentEntity> patientAppointments;
            try {
                patientEntity = retrievePatientByIc(loggedInPatient.getIdentityNumber());
                patientAppointments = retrieveAppointmentsByPatient(patientEntity);
            } catch (PatientNotFoundException_Exception ex) {
                System.out.println("Error retrieving patient");
                return;
            }
           
           
           if(patientAppointments.size() == 0) 
           {
               System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
           } else 
           {
               System.out.printf("%-5s%-20s%-15s%-30s", "Id ", "| Date ", "| Time", "| Doctor");
               System.out.println();
               //System.out.println("number of appointments: " + patientAppointments.size());
               
               for(AppointmentEntity ae : patientAppointments) 
               {
                   try {
                       System.out.printf("%-5s%-20s%-15s%-30s", ae.getAppointmentId(), "| " + sdf2.format(ae.getDate().toGregorianCalendar().getTime()), "| " + ae.getTime(), "| " + fetchAppointmentsDoctor(ae.getAppointmentId()).getFullName());
                       System.out.println();
                   } catch (AppointmentNotFoundException_Exception ex) {
                       System.out.println("Appointment ID : " + ae.getAppointmentId() + " does not exist!");
                   }
               }
           }
    }

    void addAppointment() throws ParseException_Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Automated Machine Service :: Add Appointment ***\n");
        
        List<DoctorEntity> doctors = retrieveAllDoctorEntities();
        System.out.println("Doctor: ");
        System.out.printf("%-10s%-20s", "Id ", "| Name");
        System.out.println();
        for(DoctorEntity doctor : doctors) {
            System.out.printf("%-10s%-20s", doctor.getDoctorId(), "| DR." + doctor.getFullName());
            System.out.println();
        }
        
        System.out.println();
        DoctorEntity doctorEntity;
        Date selectedDate;
        while(true) {
            try { 
                long doctorId;
                while(true) {
                    System.out.print("Enter Doctor ID > ");
                    try {
                        doctorId = sc.nextLong();
                        if(doctorId == 0 ) {
                            return;
                        }
                        sc.nextLine();
                        break;
                    } catch (Exception ex) {
                        System.out.println("Please enter an integer. ");
                        sc.nextLine();
                    }
                }
                doctorEntity = retrieveDoctorEntityById(doctorId);
                break;
            } catch (DoctorNotFoundException_Exception ex) {
                System.out.println("Doctor ID does not exist! Please try again.");
            }
        }
      
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        while(true) {
            while(true) {
                try {
                    System.out.print("Enter Date> ");
                    String selectedTime = sc.nextLine().trim();
                    if(selectedTime.equals("0")) {
                        return;
                    }
                    selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTime);
                    Calendar tempDow = Calendar.getInstance();
                    tempDow.setTime(selectedDate);
                    int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
                    if(dayOfWeek == 7 || dayOfWeek == 1) {
                        System.out.println("The clinic is closed on weekends, please enter another date.");
                    } else {
                        break;
                    }
                } catch (ParseException ex) {
                    System.out.println("Invalid Date Format, enter in yyyy-mm-dd.");
                }
            }
            long dateDiff = selectedDate.getTime() - today.getTime();
            int diffDays = (int) (dateDiff/(24* 60 * 60 * 1000));
            if(diffDays >= 2) {
                break;
            } else {
                System.out.println("Appointment date must be 2 days after today : " + sdf2.format(today));
            }
        }
        Calendar tempDow = Calendar.getInstance();
        tempDow.setTime(selectedDate);
        int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
        ArrayList<Time> timeSlots = new ArrayList<>();
        try {
            Date openingHr = sdf3.parse("08:30");
            Calendar tempHr = Calendar.getInstance();
            tempHr.setTime(openingHr);
            for(int i = 0; i < 19; i++) {
                Time tempTime = new Time(tempHr.getTimeInMillis());
                String tempTime2 = sdf3.format(tempTime);
                Time tempTime3 = new Time(sdf3.parse(tempTime2).getTime());
                if(doctorAvailableAtTime(doctorEntity, tempTime2, sdf2.format(selectedDate))) {
                    timeSlots.add(tempTime3);
                }
                tempHr.add(Calendar.MINUTE, 30);
            }
            Time tempTime1 = new Time(sdf3.parse("12:30".trim()).getTime());
            Time tempTime2 = new Time(sdf3.parse("13:00".trim()).getTime());
            timeSlots.remove(tempTime1);
            timeSlots.remove(tempTime2);
            
        } catch (ParseException ex) {
            System.out.println("Error parsing openingHr");
        }
        if(dayOfWeek == 5) {
            try {
                Time tempTime1 = new Time(sdf3.parse("17:30".trim()).getTime());
                Time tempTime2 = new Time(sdf3.parse("17:00".trim()).getTime());
                timeSlots.remove(tempTime1);
                timeSlots.remove(tempTime2);
            } catch (ParseException ex) {
                System.out.println("Error parsing Thursday hours");
            }
        } else if(dayOfWeek == 6) {
            try {
                Time tempTime1 = new Time(sdf3.parse("17:30").getTime());
                timeSlots.remove(tempTime1);
            } catch (ParseException ex) {
                System.out.println("Error parsing Friday hours");
            }
        }
        /*
        List<AppointmentEntity> doctorAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByDoctorDate(doctorEntity, selectedDate);
        for(AppointmentEntity ae : doctorAppointments) {
            if(timeSlots.contains(ae.getTime())) {
                timeSlots.remove(ae.getTime());
            }
        }*/
        
        
        while (true) {
            try {
                System.out.println("Availability for " + doctorEntity.getFullName() + " on " + sdf2.format(selectedDate));
                for(Time time : timeSlots) {
                    System.out.print(sdf3.format(time) + " ");
                }
                System.out.println();
                System.out.print("Enter Time > ");
                String sTime = sc.nextLine().trim();
                if(sTime.equals("0")) {
                    return;
                }
                Time selectedTime = new Time(sdf3.parse(sTime).getTime());
                if(timeSlots.contains(selectedTime)) {
                    try {
                        PatientEntity patientEntity;
                        try {
                            patientEntity = retrievePatientByIc(loggedInPatient.getIdentityNumber());
                        } catch (PatientNotFoundException_Exception ex) {
                            System.out.println("Error retrieving existing patient");
                            return;
                        }
                        if(patientAvailableAtTime(patientEntity, sdf3.format(selectedTime), sdf2.format(selectedDate))) {
                            try {
                                //AppointmentEntity newAppointment = new AppointmentEntity(selectedDate, selectedTime, );
                                long appointmentId = createAppointmentEntity(sdf2.format(selectedDate), sdf3.format(selectedTime), doctorEntity, patientEntity);
                                System.out.println(patientEntity.getFullName() + " appointment with DR." + doctorEntity.getFullName() + " at " + sdf3.format(selectedTime) + " on " + sdf2.format(selectedDate) + " has been added.\n");
                                break;
                            } catch (PatientNotFoundException_Exception | DoctorNotFoundException_Exception ex) {
                                System.out.println("Error creating appointment");
                                return;
                            }


                        } else {
                            System.out.println("Patient already has an existing appointment at " + sdf3.format(selectedTime) + ". Please try again.");
                            break;
                        }
                    } catch (CreateAppointmentException_Exception | ParseException_Exception ex ) {
                                System.out.println("Unable to create appointment");
                    }
                break;
                } else {
                    System.out.println("Please enter a valid option.");
                }
            } catch (ParseException ex) {
                System.out.println("Invalid Time Format, enter in hh:mm .");
            }
        } 
    }

    void cancelAppointment() throws ParseException {
        System.out.println("*** Automated Machine Service :: Cancel Appointment ***\n");
        
        Scanner sc = new Scanner(System.in);
        Date today = Calendar.getInstance().getTime();
        //Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-05-14 11:00");
        Time time;
        String cTime = sdf3.format(today);
        time = new Time(sdf3.parse(cTime).getTime());
        
        while(true) 
        {
            
            PatientEntity patientEntity;
            ArrayList<AppointmentEntity> patientAppointments = new ArrayList<>();
            List<AppointmentEntity> fetchAppointments;
            ArrayList<String> patientAppointmentIds;
            
            try {
                patientEntity = retrievePatientByIc(loggedInPatient.getIdentityNumber());
                fetchAppointments = retrieveAppointmentsByPatient(patientEntity);
                patientAppointmentIds = new ArrayList<>();
            } catch (PatientNotFoundException_Exception ex) {
                System.out.println("An error occured while fetching the existing patient records");
                return;
            }
                
            if(fetchAppointments.size() == 0) 
            {
                System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
            } 
            else 
            { 
                
                for(AppointmentEntity ae : fetchAppointments) {
                    Time appointmentTime = new Time(sdf3.parse(ae.getTime()).getTime());
                    Date apptDate = ae.getDate().toGregorianCalendar().getTime();
                    if(apptDate.compareTo(today) > 0 || (sdf2.format(apptDate).equals(sdf2.format(today)) && !appointmentTime.before(time))) {
                        System.out.println("APPOINTMENT ID : " + ae.getAppointmentId() + "ADDED");
                        patientAppointments.add(ae);
                    }
                }
                
                if(patientAppointments.size() == 0) 
                {
                    System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
                    return;
                } else {
                    System.out.println("Appointments: ");
                    System.out.printf("%-5s%-20s%-15s%-30s", "Id ", "| Date ", "| Time", "| Doctor");
                    System.out.println();
                    for(AppointmentEntity ae : patientAppointments) {
                        try {
                            patientAppointmentIds.add(ae.getAppointmentId().toString());
                            System.out.printf("%-5s%-20s%-15s%-30s", ae.getAppointmentId(), "| " + sdf2.format(ae.getDate().toGregorianCalendar().getTime()), "| " + ae.getTime(), "| " + fetchAppointmentsDoctor(ae.getAppointmentId()).getFullName());
                            System.out.println();
                        } catch (AppointmentNotFoundException_Exception ex) {
                            System.out.println("An error occured while fetching the appointment records");
                            return;
                        }
                    }
                    System.out.println();
                }
            }

            while(true) 
            {
                try 
                {
                    System.out.print("Enter Appointment ID > ");
                    Integer appointmentId = sc.nextInt();
                    if(appointmentId == 0) {
                        return;
                    }
                    sc.nextLine();
                    
                    if(patientAppointmentIds.contains(appointmentId.toString())) {
                        try 
                        {
                            AppointmentEntity fetchAppointment = retrieveAppointmentById(appointmentId);
                            DoctorEntity appointmentsDoctor = fetchAppointmentsDoctor(appointmentId);
                            deleteAppointmentEntityById(appointmentId);
                            System.out.println(patientEntity.getFullName() + " appointment with DR." + appointmentsDoctor.getFullName() + " at " + fetchAppointment.getTime() + " on " + sdf2.format(fetchAppointment.getDate().toGregorianCalendar().getTime()) + " has been cancelled.");
                            break;
                        } 
                        catch (DoctorRemoveAppointmentException_Exception | PatientRemoveAppointmentException_Exception ex) 
                        {
                            System.out.println("Error occured while deleting appointment record.");
                        }
                    } else {
                        System.out.println("Patient does not have appointment ID : " + appointmentId);
                    }
                } 
                catch (AppointmentNotFoundException_Exception ex) 
                {
                    System.out.println("Appointment does not exist! Please try again.");
                }
                break;
            } 
            break;
        }   
        
    }

    private static PatientEntity retrievePatientByIc(java.lang.String identityNumber) throws PatientNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrievePatientByIc(identityNumber);
    }



    private static java.util.List<ws.client.DoctorEntity> retrieveAllDoctorEntities() {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveAllDoctorEntities();
    }

    private static DoctorEntity retrieveDoctorEntityById(long arg0) throws DoctorNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveDoctorEntityById(arg0);
    }


    private static AppointmentEntity retrieveAppointmentById(long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveAppointmentById(arg0);
    }

    private static void deleteAppointmentEntityById(long appointmentId) throws DoctorRemoveAppointmentException_Exception, PatientRemoveAppointmentException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        port.deleteAppointmentEntityById(appointmentId);
    }

    private static DoctorEntity fetchAppointmentsDoctor(long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.fetchAppointmentsDoctor(arg0);
    }

    private static boolean patientAvailableAtTime(ws.client.PatientEntity patientEntity, java.lang.String time, java.lang.String date) throws ParseException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.patientAvailableAtTime(patientEntity, time, date);
    }

    private static boolean doctorAvailableAtTime(ws.client.DoctorEntity doctorEntity, java.lang.String time, java.lang.String date) throws ParseException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.doctorAvailableAtTime(doctorEntity, time, date);
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveAppointmentsByPatient(ws.client.PatientEntity patientEntity) throws PatientNotFoundException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveAppointmentsByPatient(patientEntity);
    }

    private static long createAppointmentEntity(java.lang.String date, java.lang.String time, ws.client.DoctorEntity doctorEntity, ws.client.PatientEntity patientEntity) throws PatientNotFoundException_Exception, DoctorNotFoundException_Exception, CreateAppointmentException_Exception, ParseException_Exception {
        ws.client.AMSWebService_Service service = new ws.client.AMSWebService_Service();
        ws.client.AMSWebService port = service.getAMSWebServicePort();
        return port.createAppointmentEntity(date, time, doctorEntity, patientEntity);
    }

    

    








    
    
    

    
}
