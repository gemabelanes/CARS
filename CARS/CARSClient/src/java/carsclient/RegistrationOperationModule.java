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
import ejb.session.stateless.QueueEntityControllerRemote;
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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public RegistrationOperationModule() {
    }

    public RegistrationOperationModule(AppointmentEntityControllerRemote appointmentEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, ConsultationEntityControllerRemote consultationEntityControllerRemote, QueueEntityControllerRemote queueEntityControllerRemote) {
        this();
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.consultationEntityControllerRemote = consultationEntityControllerRemote;
        this.queueEntityControllerRemote = queueEntityControllerRemote;
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf3 = new SimpleDateFormat("HH:mm");
    }
    
    
    void registrationMainMenu() throws ParseException {
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("*** CARS :: Registration Operation ***");
            System.out.println();
            System.out.println("1: Register New Patient");
            System.out.println("2: Register Walk-In Consultation");
            System.out.println("3: Register Consultation By Appointment");
            System.out.println("4: Back");
            System.out.println();
            Integer response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    registerNewPatient();
                } else if (response == 2) {
                    registerWalkInConsultation();
                } else if (response == 3) {
                    registerConsultationAppointment();
                } else if (response == 4) {
                    break;
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
        
        // Fake date : Monday, 2020-05-11 14:00 hrs
        Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-05-14 11:00");
        Calendar tempDow = Calendar.getInstance();
        Calendar tempRound = Calendar.getInstance();
        tempRound.setTime(today);
        
        //Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //Calendar tempDow = Calendar.getInstance();
        //Calendar tempRound = Calendar.getInstance();
        
        String cTime = sdf3.format(tempRound.getTime());
        tempDow.setTime(today);
        int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
        Time currentTime;
        Time lastSlot;
        String todayString = sdf2.format(today);
        Date todayFormatted = sdf2.parse(todayString);
        currentTime = new Time(sdf3.parse(cTime.trim()).getTime());
        lastSlot = new Time(sdf3.parse("17:30").getTime());
        if(dayOfWeek == 5 && currentTime.after(new Time (sdf3.parse("16:30".trim()).getTime())) ) {
            lastSlot = new Time(sdf3.parse("16:30").getTime());
            System.out.println(" Our last consultation slot for Thursdays is 16:30. The time now is " + currentTime + ". Please come again tomorrow.");
            return;
        } else if (dayOfWeek == 6 && currentTime.after(new Time(sdf3.parse("17:00".trim()).getTime()))) {
            lastSlot = new Time(sdf3.parse("17:00").getTime());
            System.out.println(" Our last consultation slot for Fridays is 17:00. The time now is " + currentTime + ". Please come again tomorrow.");
            return;
        } else if (dayOfWeek >= 2 && dayOfWeek <=4 && currentTime.after(new Time(sdf3.parse("17:30".trim()).getTime()))) {
            System.out.println(" Our last consultation slot for Mondays - Wednesdays is 17:30. The time now is " + currentTime + ". Please come again tomorrow.");
            return;
        } else if(currentTime.before(new Time(sdf3.parse("08:30".trim()).getTime())) || currentTime.after(new Time(sdf3.parse("18:00".trim()).getTime()))){
            System.out.println("Clinic has yet open, please come back at 08:30. Time now is " + currentTime);
            return;
        } else {
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
                    for(int i = 0; i < doctors.size(); i++) {
                        if(doctors.get(i).equals(doctorEntity)) {
                            for(int j = 0; j < next6Slots.size(); j++) {
                                if(doctorAvailability[j][i]) {
                                    firstFreeSlot = next6Slots.get(j);
                                    break;
                                }
                            }
                        }
                    }
                    if(firstFreeSlot != null ) {
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
                try {
                    System.out.print("Enter Patient Identity Number> ");
                    String ic = sc.nextLine();
                    patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(ic);
                    break;
                } catch (PatientNotFoundException ex) {
                    System.out.println("Patient record not found, please try again!");
                }
            }
            
            
            
            int queueNum = 0;
            if(!queueEntityControllerRemote.doesQueueEntityExist(todayFormatted)) {
                //System.out.println("DAY QUEUE ENTITY DOES NOT EXISTS!");
                QueueEntity newQueue = new QueueEntity(todayFormatted);
                queueEntityControllerRemote.createQueueEntity(newQueue);
            }
            //System.out.println("DAY QUEUE ENTITY ALREADY EXISTS!");
            QueueEntity queueEntity;
            try {
                //System.out.println("DEBUG LINE 1");
                queueEntity = queueEntityControllerRemote.retrieveQueueEntityByDate(todayFormatted);
                //System.out.println("DEBUG LINE 2");
                queueNum = queueEntity.getCounterAndIncrement();
                //System.out.println("DEBUG LINE 3");
                queueEntityControllerRemote.updateQueueEntity(queueEntity);
                //System.out.println("DEBUG LINE 4");
            } catch (QueueNotFoundException ex) {
                System.out.println("Error retrieving queue for day : " + todayString);
            }
            
            try {
                ConsultationEntity newConsultation = new ConsultationEntity(queueNum, patientEntity, doctorEntity, todayFormatted, firstFreeSlot);
                consultationEntityControllerRemote.createConsultationEntity(newConsultation);
                System.out.println(patientEntity.getFullName() + " appointment is confirmed with DR." + doctorEntity.getFullName() + " at " + sdf3.format(firstFreeSlot));
                System.out.println("Queue Number: " + queueNum);
            } catch (CreateConsultationException ex) { 
                System.out.println("Error creating consultation");
            }
        }
        
        
        
        
        
        

    }

    private void registerConsultationAppointment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
