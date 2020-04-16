/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsclient;

import com.sun.istack.Nullable;
import ejb.session.stateless.AppointmentEntityControllerRemote;
import ejb.session.stateless.DoctorEntityControllerRemote;
import ejb.session.stateless.PatientEntityControllerRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import util.exception.CreateAppointmentException;
import util.exception.DoctorNotFoundException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.PatientNotFoundException;
import util.exception.PatientRemoveAppointmentException;

/**
 *
 * @author gem
 */
public class AppointmentOperationsModule {
    private AppointmentEntityControllerRemote appointmentEntityControllerRemote;
    private PatientEntityControllerRemote patientEntityControllerRemote;
    private DoctorEntityControllerRemote doctorEntityControllerRemote;
    SimpleDateFormat sdf2;
    SimpleDateFormat sdf3;
    
    PatientEntity loggedInPatient;

    public AppointmentOperationsModule() {
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf3 = new SimpleDateFormat("HH:mm");
    }

    public AppointmentOperationsModule(AppointmentEntityControllerRemote appointmentEntityControllerRemote, PatientEntityControllerRemote patientEntityControllerRemote, DoctorEntityControllerRemote doctorEntityControllerRemote, @Nullable PatientEntity loggedInPatient) {
        this();
        this.appointmentEntityControllerRemote = appointmentEntityControllerRemote;
        this.patientEntityControllerRemote = patientEntityControllerRemote;
        this.doctorEntityControllerRemote = doctorEntityControllerRemote;
        this.loggedInPatient = loggedInPatient;
                
    }
    
    void appointmentMainMenu() throws ParseException {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("*** CARS :: Appointment Operation ***");
            System.out.println();
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back");
            System.out.println();
            Integer response = -1;
            
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

    private void viewPatientAppointments() {
       Scanner sc = new Scanner(System.in);
       System.out.println("*** CARS :: Appointment Operation :: View Patient Appointments ***\n");
            
       while(true) {
            try {
                PatientEntity patientEntity;
                if(Objects.isNull(loggedInPatient)) {
                    System.out.print("Enter Patient Identity Number > ");
                    String ic = sc.nextLine();
                    if(ic.equals("0")) {
                        break;
                    }
                    patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(ic);
                } else {
                    patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(loggedInPatient.getIdentityNumber());
                }
                System.out.println();
                List<AppointmentEntity> patientAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByPatient(patientEntity);
                
                if(patientAppointments.size() == 0) {
                    System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
                    return;
                } else { 
                    System.out.printf("%-5s%-20s%-15s%-30s", "Id ", "| Date ", "| Time", "| Doctor");
                    System.out.println();
                    for(AppointmentEntity ae : patientAppointments) {
                        System.out.printf("%-5s%-20s%-15s%-30s", ae.getAppointmentId(), "| " + sdf2.format(ae.getDate()), "| " + ae.getTime(), "| " + ae.getDoctorEntity().getFullName());
                        System.out.println();
                    }
                }
                System.out.println();
                break;
            } catch (PatientNotFoundException ex) {
                System.out.println("Patient record not found, please try again! ");
            }
           
       }
       System.out.println();
       
    }

    private void addAppointment() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CARS :: Appointment Operation :: Add Appointment ***\n");
        
        List<DoctorEntity> doctors = doctorEntityControllerRemote.retrieveAllDoctorEntities();
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
                doctorEntity = doctorEntityControllerRemote.retrieveDoctorEntityById(doctorId);
                break;
            } catch (DoctorNotFoundException ex) {
                System.out.println("Doctor ID does not exist! Please try again. ");
            }
        }
      
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //System.out.println("Todays day is : " + new SimpleDateFormat("EEEE").format(today));
        while(true) {
            while(true) {
                try {
                    System.out.print("Enter Date > ");
                    String selectedTime = sc.nextLine().trim();
                    if(selectedTime.equals("0")) {
                        return;
                    }
                    selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTime);
                    Calendar tempDow = Calendar.getInstance();
                    tempDow.setTime(selectedDate);
                    int dayOfWeek = tempDow.get(Calendar.DAY_OF_WEEK);
                    System.out.println();
                    //System.out.println("DayOfWeek is " + new SimpleDateFormat("EEEE").format(selectedDate));
                    if(dayOfWeek == 7 || dayOfWeek == 1) {
                        System.out.println("The clinic is closed on weekends, please enter another date. ");
                    } else {
                        break;
                    }
                } catch (ParseException ex) {
                    System.out.println("Invalid Date Format, enter in yyyy-mm-dd. ");
                    //Logger.getLogger(ReservationOperationModule.class.getName()).log(Level.SEVERE, null, ex);
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
                if(doctorEntityControllerRemote.doctorAvailableAtTime(doctorEntity, tempTime3, selectedDate)) {
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
        
        while (true) {
            try {
                System.out.println("Availability for " + doctorEntity.getFullName() + " on " + sdf2.format(selectedDate));
                for(Time time : timeSlots) {
                    System.out.print(sdf3.format(time) + " ");
                }
                System.out.println();
                System.out.println();
                System.out.print("Enter Time > ");
                String sTime = sc.nextLine().trim();
                if(sTime.equals("0")) {
                    return;
                }
                Time selectedTime = new Time(sdf3.parse(sTime).getTime());
                if(timeSlots.contains(selectedTime)) {
                    while (true) {
                        System.out.print("Enter Patient Identity Number > ");
                        String patientIc = sc.nextLine();
                        if(patientIc.equals("0")) {
                            return;
                        }
                        try {
                            PatientEntity patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(patientIc);
                            if(patientEntityControllerRemote.patientAvailableAtTime(patientEntity, selectedTime, selectedDate)) {
                                AppointmentEntity newAppointment = new AppointmentEntity(selectedDate, sdf3.format(selectedTime), patientEntity, doctorEntity);
                                long createNewAppointment = appointmentEntityControllerRemote.createAppointmentEntity(newAppointment);
                                System.out.println(patientEntity.getFullName() + " appointment with DR." + doctorEntity.getFullName() + " at " + sdf3.format(selectedTime) + " on " + sdf2.format(selectedDate) + " has been added.\n");
                                break;
                            } else {
                                System.out.println("Patient already has an existing appointment at " + sdf3.format(selectedTime) + ". Please try again.");
                                break;
                                
                            }
                        } catch (PatientNotFoundException ex) {
                            System.out.println("Patient IC : " + patientIc + " does not exist! Please try again. ");
                        } catch (CreateAppointmentException ex) {
                            System.out.println("Error creating new appointment");
                        }
                    }
                    break;
                } else {
                    System.out.println("Please enter a valid option. ");
                }
            } catch (ParseException ex) {
                System.out.println("Invalid Time Format, enter in hh:mm. ");
            }
        } 
    }

    private void cancelAppointment() throws ParseException {
        System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment *** \n");
        
        Scanner sc = new Scanner(System.in);
        Date today = Calendar.getInstance().getTime();
        Time time;
        String cTime = sdf3.format(today);
        time = new Time(sdf3.parse(cTime).getTime());
        
        while(true) {
            try {
                PatientEntity patientEntity;
                if(Objects.isNull(loggedInPatient)) {
                    System.out.print("Enter Patient Identity Number > ");
                    String ic = sc.nextLine();
                    if(ic.equals("0")) {
                        return;
                    }
                   patientEntity = patientEntityControllerRemote.retrievePatientEntityByIc(ic);
                } else {
                    patientEntity = loggedInPatient;
                }
                System.out.println();
                
                List<AppointmentEntity> fetchAppointments = appointmentEntityControllerRemote.retrieveAppointmentsByPatient(patientEntity);
                ArrayList<AppointmentEntity> patientAppointments = new ArrayList<>();
                
                if(fetchAppointments.size() == 0) 
                {
                    System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
                    return;
                } else {
                    
                    for(AppointmentEntity ae : fetchAppointments) {
                        Time appointmentTime = new Time(sdf3.parse(ae.getTime()).getTime());
                        if(ae.getDate().compareTo(today) > 0 || (sdf2.format(ae.getDate()).equals(sdf2.format(ae.getDate()))&& !time.after(appointmentTime))) {
                            patientAppointments.add(ae);
                        }
                    }

                    if(patientAppointments.size() == 0) {
                        System.out.println("No appointment records found for patient : " + patientEntity.getIdentityNumber());
                        return;
                    } else { 
                        System.out.println("Appointments: ");
                        System.out.printf("%-5s%-20s%-15s%-30s", "Id ", "| Date ", "| Time", "| Doctor");
                        System.out.println();
                        for(AppointmentEntity ae : patientAppointments) {
                            if(ae.getDate().after(today)) {
                                System.out.printf("%-5s%-20s%-15s%-30s", ae.getAppointmentId(), "| " + sdf2.format(ae.getDate()), "| " + ae.getTime(), "| " + ae.getDoctorEntity().getFullName());
                                System.out.println();
                            }
                        }
                        System.out.println();
                    }
                }
                
                while(true) {
                    try {
                        System.out.print("Enter Appointment Id > ");
                        int appointmentId = sc.nextInt();
                        if(appointmentId == 0) {
                            return;
                        }
                        sc.nextLine();
                        AppointmentEntity fetchAppointment = appointmentEntityControllerRemote.retrieveAppointmentById(appointmentId);
                        if(patientAppointments.contains(fetchAppointment)) {
                            try {
                                appointmentEntityControllerRemote.deleteAppointmentEntityById(appointmentId);
                                System.out.println(patientEntity.getFullName() + " appointment with DR." + fetchAppointment.getDoctorEntity().getFullName() + " at " + fetchAppointment.getTime() + " on " + sdf2.format(fetchAppointment.getDate()) + " has been cancelled.");
                            } catch (DoctorRemoveAppointmentException | PatientRemoveAppointmentException ex) {
                                System.out.println("Error occured while deleting appointment record.");
                            }
                        }
                    } catch (AppointmentNotFoundException ex) {
                        System.out.println("Appointment does not exist! Please try again.");
                    }
                    break;
                } 
                System.out.println();
                break;
            } catch (PatientNotFoundException ex) {
                System.out.println("Patient record not found, please try again!");
            }
           
       }     
        
    }
    
    
}
