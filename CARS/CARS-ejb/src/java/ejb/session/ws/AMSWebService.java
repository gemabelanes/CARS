/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateAppointmentException;
import util.exception.DeleteAppointmentExceptionWs;
import util.exception.DoctorNotFoundException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;
import util.exception.PatientRemoveAppointmentException;

/**
 *
 * @author gem
 */
@WebService(serviceName = "AMSWebService")
@Stateless()
public class AMSWebService {

    @EJB
    private DoctorEntityControllerLocal doctorEntityController;

    @EJB
    private PatientEntityControllerLocal patientEntityController;

    @EJB
    private AppointmentEntityControllerLocal appointmentEntityController;
    
    
    
    @WebMethod(operationName = "patientLogin")
    public PatientEntity patientLogin(@WebParam(name= "identityNumber") String identityNumber,
                                      @WebParam(name = "password") String password) throws InvalidLoginException {
        return patientEntityController.patientLogin(identityNumber, password);
        
    }
    
    @WebMethod(operationName = "retrievePatientByIc")
    public PatientEntity retrievePatientByIc(@WebParam(name = "identityNumber") String identityNumber) throws PatientNotFoundException {
        return patientEntityController.retrievePatientEntityByIc(identityNumber);
    }
    
    
    @WebMethod(operationName = "createNewPatient")
    public Long createNewPatient(@WebParam(name= "identityNumber") String identityNumber,
                                 @WebParam(name = "password") String password,
                                 @WebParam(name = "firstName") String firstName,
                                 @WebParam(name = "lastName") String lastName,
                                 @WebParam(name = "gender") String gender,
                                 @WebParam(name = "age") Integer age,
                                 @WebParam(name = "phoneNumber") String phoneNumber,
                                 @WebParam(name = "address") String address) {
        
        return patientEntityController.createNewPatient(new PatientEntity(identityNumber, firstName, lastName, gender, age, phoneNumber, address, password ));
    
    }
    
    @WebMethod(operationName = "retrieveAppointmentsByPatient") 
    public List<AppointmentEntity> retrieveAppointmentsByPatient(@WebParam(name = "patientEntity")PatientEntity patientEntity) throws PatientNotFoundException {
        PatientEntity retrievePatient = patientEntityController.retrievePatientEntityByIc(patientEntity.getIdentityNumber());
        System.err.println("retrieved patient");
        return appointmentEntityController.retrieveAppointmentsByPatient(retrievePatient);
    }
    
    @WebMethod(operationName = "createAppointmentEntity")
    public long createAppointmentEntity(@WebParam(name = "date") String date,
                                          @WebParam(name = "time") String time,
                                          @WebParam(name = "doctorEntity") DoctorEntity doctorEntity,
                                          @WebParam(name = "patientEntity") PatientEntity patientEntity) throws CreateAppointmentException, ParseException, DoctorNotFoundException, PatientNotFoundException {
        Date dateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Time timeFormatted = new Time(new SimpleDateFormat("HH:mm").parse(time).getTime());
        DoctorEntity fetchDoctor = doctorEntityController.retrieveDoctorEntityById(doctorEntity.getDoctorId());
        PatientEntity fetchPatient = patientEntityController.retrievePatientEntityByIc(patientEntity.getIdentityNumber());
        AppointmentEntity newAppointment = new AppointmentEntity(dateFormatted, time, fetchPatient, fetchDoctor);
        System.out.println("NEWAPPOINTMENT CREATED, ID : " + newAppointment.getAppointmentId());
        return appointmentEntityController.createAppointmentEntity(newAppointment);
    }
    
    @WebMethod(operationName = "deleteAppointmentEntityById")
    public void deleteAppointmentEntityById(@WebParam(name = "appointmentId") long appointmentId) throws DoctorRemoveAppointmentException, PatientRemoveAppointmentException {
        appointmentEntityController.deleteAppointmentEntityById(appointmentId);
    }
    @WebMethod(operationName = "doesPatientExistByIc")
    public boolean doesPatientExistByIc(@WebParam(name = "patientIc") String patientIc) {
        return patientEntityController.doesPatientExistByIc(patientIc);
    }
    
    @WebMethod(operationName = "retrieveAllDoctorEntities") 
    public List<DoctorEntity> retrieveAllDoctorEntities() {
        return doctorEntityController.retrieveAllDoctorEntities();
    }
            
    
    @WebMethod(operationName = "doctorAvailableAtTime")
    public boolean doctorAvailableAtTime(@WebParam(name = "doctorEntity") DoctorEntity doctorEntity,
                                         @WebParam(name = "time")String time,
                                         @WebParam(name = "date")String date) throws ParseException {
        System.err.println("DAAT runs till here");
        return doctorEntityController.doctorAvailableAtTime(doctorEntity, new Time(new SimpleDateFormat("HH:mm").parse(time).getTime()), new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }
    
    @WebMethod(operationName = "patientAvailableAtTime")
    public boolean patientAvailableAtTime(@WebParam(name = "patientEntity") PatientEntity patientEntity,
                                          @WebParam(name = "time")String time,
                                          @WebParam(name = "date")String date) throws ParseException {
        return patientEntityController.patientAvailableAtTime(patientEntity, new Time(new SimpleDateFormat("HH:mm").parse(time).getTime()), new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }
    
    @WebMethod(operationName = "retrieveDoctorEntityById")
    public DoctorEntity retrieveDoctorEntityById(@WebParam(name = "doctorId") long doctorId) throws DoctorNotFoundException {
        return doctorEntityController.retrieveDoctorEntityById(doctorId);
    }
    
    @WebMethod(operationName = "retrieveAppointmentById")
    public AppointmentEntity retrieveAppointmentById(@WebParam(name = "appointmentId") long appointmentId) throws AppointmentNotFoundException {
        return appointmentEntityController.retrieveAppointmentById(appointmentId);
    }
    
    @WebMethod(operationName = "fetchAppointmentsDoctor")
    public DoctorEntity fetchAppointmentsDoctor(@WebParam(name = "appointmentId") long appointmentId) throws AppointmentNotFoundException {
        return appointmentEntityController.fetchAppointmentsDoctor(appointmentId);
    }
}
