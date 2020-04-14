/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsclient;

import com.sun.istack.Nullable;
import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.ConsultationEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import ejb.session.stateless.QueueEntityControllerRemote;
import entity.AppointmentEntity;
import entity.ConsultationEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.QueueEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateConsultationException;
import util.exception.DoctorNotFoundException;
import util.exception.PatientNotFoundException;
import util.exception.QueueNotFoundException;

/**
 *
 * @author gem
 */
public class RegistrationOperationModule {
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    private ConsultationEntityControllerRemote consultationEntityControllerRemote;
    private QueueEntityControllerRemote queueEntityControllerRemote;
    
    SimpleDateFormat sdf2;
    SimpleDateFormat sdf3;
    Date today;
    String todayString;
    Date todayFormatted;
    
    PatientEntity loggedInPatient;
                    

    public RegistrationOperationModule() {
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf3 = new SimpleDateFormat("HH:mm");
        try {
            fetchToday();
        } catch (ParseException ex) {
            Logger.getLogger(RegistrationOperationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RegistrationOperationModule(AppointmentEntityControllerRemote appointmentEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote, QueueEntityControllerRemote queueEntityControllerRemote, @Nullable PatientEntity loggedInPatient) {
        this();
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
        this.queueEntityControllerRemote = queueEntityControllerRemote;
        this.loggedInPatient = loggedInPatient;
        
    }
    
    
    void registrationMainMenu() throws ParseException {
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("*** CARS :: Registration Operation ***");
            System.out.println("*Enter 0 at any point to terminate*");
            System.out.println();
            System.out.println("1: Register New Patient");
            System.out.println("2: Register Walk-In Consultation");
            System.out.println("3: Register Consultation By Appointment");
            System.out.println("4: Back");
            System.out.println();
            Integer response = -1;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    registerNewPatient();
                } else if (response == 2) {
                    registerWalkInConsultation();
                } else if (response == 3) {
                    registerConsultationAppointment();
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

    private void registerNewPatient() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CARS :: Registration Operation :: Register New Patient ***\n");
        
        
        while (true) {
            System.out.print("Enter Identity Number> ");
            String ic = sc.nextLine();
            if(ic.equals("0")) {
                return;
            }
            //System.out.println("DEBUG LINE 1 : " + patientEntityControllerRemote.doesPatientExistByIc(ic));
            if(!patientEntityControllerRemote.doesPatientExistByIc(ic)) {
                System.out.print("Enter Password> ");
                String password = sc.nextLine();
                System.out.print("Enter First Name> ");
                String firstName = sc.nextLine();
                System.out.print("Enter Last Name> ");
                String lastName = sc.nextLine();
                System.out.print("Enter Gender> ");
                String gender = sc.nextLine();
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
                System.out.print("Enter Phone Number> ");
                String phoneNumber = sc.nextLine();
                System.out.print("Enter Address> ");
                String address = sc.nextLine();
                
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

    private void registerWalkInConsultation() throws ParseException {
        System.out.println("*** CARS :: Registration Operation :: Register Walk-In Consultation ***");
        // Actual date
        //Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Fake date : Monday, 2020-05-11 14:00 hrs
        //Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-05-14 11:00");
        //String todayString = sdf2.format(today);
        //Date todayFormatted = sdf2.parse(todayString);
        fetchToday();
        Calendar tempDow = Calendar.getInstance();
        Calendar tempRound = Calendar.getInstance();
        tempRound.setTime(today);
        String cTime = sdf3.format(tempRound.getTime());
        tempDow.setTime(today);
        int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
        Time currentTime;
        Time lastSlot;
        currentTime = new Time(sdf3.parse(cTime.trim()).getTime());
        lastSlot = isClinicOpen();
        if(lastSlot != null) {
            List<DoctorEntity> doctors = doctorEntityControllerRemote.retrieveAllDoctorEntities();
            System.out.println("Doctor: ");
            System.out.printf("%-10s%-20s", "Id ", "| Name");
            System.out.println();
            for(DoctorEntity doctor : doctors) {
                System.out.printf("%-10s%-20s", doctor.getDoctorId(), "| DR." + doctor.getFullName());
                System.out.println();
            }

            int currMinutes = Integer.parseInt(new SimpleDateFormat("mm").format(tempRound.getTime()));
            if(currMinutes >= 30) {
                tempRound.add(Calendar.HOUR_OF_DAY, 1);
                tempRound.set(Calendar.MINUTE, 0);
                tempRound.set(Calendar.SECOND, 0);    
            } else {
                tempRound.set(Calendar.MINUTE, 30);
                tempRound.set(Calendar.SECOND, 0);
            }
            ArrayList<Time> next6Slots = new ArrayList<>();
                Time break1 = new Time(sdf3.parse("12:30".trim()).getTime());
                Time break2 = new Time(sdf3.parse("13:00".trim()).getTime());
                for(int i = 0; i < 6; i++) {
                    Time tempTime = new Time(tempRound.getTimeInMillis());
                    String tempTime2 = sdf3.format(tempTime);
                    Time tempTime3 = new Time(sdf3.parse(tempTime2).getTime());
                    if(tempTime3.equals(break1) || tempTime3.equals(break2)) {
                        i--;
                    } else {
                        next6Slots.add(tempTime3);
                    }
                    if(tempTime3.equals(lastSlot)) {
                        break;
                    }
                    tempRound.add(Calendar.MINUTE, 30);
                }

            System.out.println("Availability");
            System.out.print("Time  |");    
            for(DoctorEntity doctor : doctors) {
                System.out.print(doctor.getDoctorId() + " |");
            }
            System.out.println();

            boolean[][] doctorAvailability = new boolean[next6Slots.size()][doctors.size()];
            for(int i = 0; i < next6Slots.size(); i++) {
                System.out.print(sdf3.format(next6Slots.get(i).getTime()) + " |");
                for(int j = 0; j < doctors.size(); j++) {
                    doctorAvailability[i][j] = doctorEntityControllerRemote.doctorAvailableAtTime(doctors.get(j), next6Slots.get(i), todayFormatted);
                    if(doctorAvailability[i][j]) {
                        System.out.print("O |");
                    } else {
                        System.out.print("X |");
                    }
                }
                System.out.println();
            }
            
            Scanner sc = new Scanner(System.in);
            DoctorEntity doctorEntity;
            Time firstFreeSlot = null;
            while(true) {
                try { 
                    long doctorId;
                    while(true) {
                        System.out.print("Enter Doctor ID> ");
                        try {
                            doctorId = sc.nextLong();
                            sc.nextLine();
                            if(doctorId == 0 ) {
                                return;
                            }
                            
                            break;
                        } catch (Exception ex) {
                            System.out.println("Please enter an integer. (Enter 0 to return to Appointment Operations Menu)");
                            sc.nextLine();
                        }
                    }
                    doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
                    boolean isDoctorFree = false;
                    for(int i = 0; i < doctors.size(); i++) {
                        if(doctors.get(i).equals(doctorEntity)) {
                            for(int j = 0; j < next6Slots.size(); j++) {
                                if(doctorAvailability[j][i]) {
                                    isDoctorFree = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(isDoctorFree == true ) {
                        break;
                    } else {
                        System.out.println("Doctor ID : " + doctorId + " has no available slot, please select another doctor");
                    }
                            
                    //break;
                } catch (DoctorNotFoundException ex) {
                    System.out.println("Doctor ID does not exist! Please try again. (Enter 0 to return to Appointment Operations Menu)");
                }
            }
            
            PatientEntity patientEntity;
            while(true) {
                //System.out.println("DEBUG HERE " + loggedInPatient);
                try {
                    if(Objects.isNull(loggedInPatient) ) {
                        System.out.print("Enter Patient Identity Number> ");
                        String ic = sc.nextLine();
                        if(ic.equals("0")) {
                            return;
                        }
                        patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(ic);
                    } else {
                        //System.out.println("loggedPatient is not null" + Objects.isNull(loggedInPatient));
                        patientEntity = loggedInPatient;
                    }
                    
                    for(int i = 0; i < doctors.size(); i++) {
                        if(doctors.get(i).equals(doctorEntity)) {
                            for(int j = 0; j < next6Slots.size(); j++) {
                                if(doctorAvailability[j][i] && patientEntityControllerRemote.patientAvailableAtTime(patientEntity, next6Slots.get(j), todayFormatted)) {
                                    firstFreeSlot = next6Slots.get(j);
                                    break;
                                }
                            }
                        }
                    }

                    if(firstFreeSlot != null) {
                        break;
                    } else {
                        System.out.println("Patient IC : " + patientEntity.getIdentityNumber() + " already has a appointment/consultation for the day that clashes with all the available timings for doctor : " + doctorEntity.getFullName());
                        System.out.println("Please try again!\n");
                        registerWalkInConsultation();
                        
                    }
                    //break;
                } catch (PatientNotFoundException ex) {
                    System.out.println("Patient record not found, please try again!");
                }
            }
            
            int queueNum = getCounter();
            
            try {
                ConsultationEntity newConsultation = new ConsultationEntity(queueNum, patientEntity, doctorEntity, todayFormatted, firstFreeSlot);
                consultationEntityControllerRemote.createConsultationEntity(newConsultation);
                System.out.println(patientEntity.getFullName() + " appointment is confirmed with DR." + doctorEntity.getFullName() + " at " + sdf3.format(firstFreeSlot));
                System.out.println("Queue Number: " + queueNum + ".\n");
            } catch (CreateConsultationException ex) { 
                System.out.println("Error creating consultation");
            }
        }
        
    }
    
    private  int getCounter() {
        int queueNum = 0;
        if(!queueEntityControllerRemote.doesQueueEntityExist(todayFormatted)) {
            QueueEntity newQueue = new QueueEntity(todayFormatted);
            queueEntityControllerRemote.createQueueEntity(newQueue);
        }
        QueueEntity queueEntity;
        try {
            queueEntity = queueEntityControllerRemote.retrieveQueueEntityByDate(todayFormatted);
            queueNum = queueEntity.getCounterAndIncrement();
            queueEntityControllerRemote.updateQueueEntity(queueEntity);
        } catch (QueueNotFoundException ex) {
            System.out.println("Error retrieving queue for day : " + sdf2.format(todayFormatted));
        }
        return queueNum;
    }
    
    private Date fetchToday() throws ParseException {

        //today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Actual date
        //today = Calendar.getInstance().getTime();
        // Fake date : Monday, 2020-05-11 14:00 hrs
        today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-05-14 11:00");
        todayString = sdf2.format(today);
        todayFormatted = sdf2.parse(todayString);
        
        return todayFormatted;
    }

    private Time isClinicOpen() throws ParseException {
        Calendar tempDow = Calendar.getInstance();
        Calendar tempRound = Calendar.getInstance();
        tempRound.setTime(today);
        String cTime = sdf3.format(tempRound.getTime());
        tempDow.setTime(today);
        int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
        Time currentTime;
        Time lastSlot;
        currentTime = new Time(sdf3.parse(cTime.trim()).getTime());
        lastSlot = new Time(sdf3.parse("17:30").getTime());
        if(dayOfWeek == 5) {
            lastSlot = new Time(sdf3.parse("16:30").getTime());
            if(currentTime.after(new Time (sdf3.parse("16:30".trim()).getTime())) ) { 
                System.out.println(" Our last consultation slot for Thursdays is 16:30. The time now is " + currentTime + ". Please come again tomorrow.");
                return null;
            }
        } else if (dayOfWeek == 6) {
            lastSlot = new Time(sdf3.parse("17:00").getTime());
            if(currentTime.after(new Time(sdf3.parse("17:00".trim()).getTime()))) {
                System.out.println(" Our last consultation slot for Fridays is 17:00. The time now is " + currentTime + ". Please come again tomorrow.");
                return null;
            }
        } else if (dayOfWeek >= 2 && dayOfWeek <= 4) {
            lastSlot = new Time(sdf3.parse("17:30").getTime());
            if(dayOfWeek <=4 && currentTime.after(new Time(sdf3.parse("17:30".trim()).getTime()))) {
                System.out.println(" Our last consultation slot for Mondays - Wednesdays is 17:30. The time now is " + currentTime + ". Please come again tomorrow.");
                return null;
            }
        } else if(currentTime.before(new Time(sdf3.parse("08:30".trim()).getTime())) || currentTime.after(new Time(sdf3.parse("18:00".trim()).getTime()))){
            System.out.println("Clinic has yet open, please come back at 08:30. Time now is " + currentTime);
            return null;
        }
        return lastSlot;
    }
    
    private void registerConsultationAppointment() throws ParseException {
        System.out.println("*** CARS :: Registration Operation :: Register Consultation By Appointment ***");
        
        Scanner sc = new Scanner(System.in);
        fetchToday();
        Calendar tempRound = Calendar.getInstance();
        tempRound.setTime(today);
        String cTime = sdf3.format(tempRound.getTime());
        Time currentTime;
        currentTime = new Time(sdf3.parse(cTime.trim()).getTime());
        while(true) {
            try {
                PatientEntity patientEntity;
                if(Objects.isNull(loggedInPatient)) {
                    System.out.print("Enter Patient Identity Number (Enter 0 to return to Appointment Operations Menu)> ");
                    String ic = sc.nextLine();
                    if(ic.equals("0")) {
                        return;
                    }
                   patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(ic);
                } else {
                    patientEntity = loggedInPatient;
                }
                List<AppointmentEntity> fetchAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByPatient(patientEntity);
                ArrayList<AppointmentEntity> patientAppointments = new ArrayList<>();
                
                for(AppointmentEntity appointmentEntity : fetchAppointments) {
                    if(!appointmentEntity.getTime().before(currentTime)) {
                        patientAppointments.add(appointmentEntity);
                    }
                }
                
                if(patientAppointments.size() == 0) {
                    System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
                } else { 
                    System.out.println("Appointments: ");
                    System.out.printf("%-5s%-20s%-15s%-30s", "Id ", "| Date ", "| Time", "| Doctor");
                    System.out.println();
                    for(AppointmentEntity ae : patientAppointments) {
                        System.out.printf("%-5s%-20s%-15s%-30s", ae.getAppointmentId(), "| " + new SimpleDateFormat("yyyy-MM-dd").format(ae.getDate()), "| " + new SimpleDateFormat("HH:mm").format(ae.getTime()), "| " + ae.getDoctorEntity().getFullName());
                        System.out.println();
                    }
                    System.out.println();
                }
                
                while(true) {
                    try {
                        System.out.print("Enter Appointment Id (Enter 0 to return to Appointment Operations Menu)> ");
                        int appointmentId = sc.nextInt();
                        if(appointmentId == 0) {
                            return;
                        }
                        sc.nextLine();
                        AppointmentEntity fetchAppointment = appointmentEntityControllerRemote.retrieveAppointmentById(appointmentId);
                        if(patientAppointments.contains(fetchAppointment)) {
                            try {
                                int queueNum = getCounter();
                                ConsultationEntity newConsultation = new ConsultationEntity(queueNum, fetchAppointment);
                                consultationEntityControllerRemote.createConsultationEntity(newConsultation);
                                System.out.println(patientEntity.getFullName() + " appointment is confirmed with DR." + newConsultation.getDoctorEntity().getFullName() + " at " + sdf3.format(fetchAppointment.getTime()));
                                System.out.println("Queue Number: " + queueNum + ".");
                                //appointmentEntityControllerRemote.deleteAppointmentEntityById(appointmentId);
                                //System.out.println(patientEntity.getFullName() + " appointment with DR." + fetchAppointment.getDoctorEntity().getFullName() + " at " + sdf3.format(fetchAppointment.getTime()) + " on " + sdf2.format(fetchAppointment.getDate()) + " has been cancelled.");
                            } catch (CreateConsultationException ex) {
                                System.out.println("Error occured while creating consultation.");
                            }
                        }
                    } catch (AppointmentNotFoundException ex) {
                        System.out.println("Please input an valid Appointment ID and try again.");
                    }
                    break;
                } 
                break;
            } catch (PatientNotFoundException ex) {
                System.out.println("Patient record not found, please try again!)");
            }
           
       }     
        
        
        
    
    }
    
}
