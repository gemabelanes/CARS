/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateAppointmentException;
import util.exception.DoctorAddAppointmentException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.PatientAddAppointmentException;
import util.exception.PatientRemoveAppointmentException;

/**
 *
 * @author gem
 */
@Stateless
@Local(AppointmentEntityControllerLocal.class)
@Remote(AppointmentEntityControllerRemote.class)
public class AppointmentEntityController implements AppointmentEntityControllerRemote, AppointmentEntityControllerLocal {
    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;

    public AppointmentEntityController() {
    }

    @Override
    public long createAppointmentEntity(AppointmentEntity appointmentEntity) throws CreateAppointmentException {
        try {
            DoctorEntity doctorEntity = appointmentEntity.getDoctorEntity();
            PatientEntity patientEntity = appointmentEntity.getPatientEntity();
            doctorEntity.addAppointment(appointmentEntity);
            patientEntity.addAppointment(appointmentEntity);
            entityManager.persist(appointmentEntity);
            entityManager.merge(patientEntity);
            entityManager.merge(doctorEntity);
            entityManager.flush();
            return appointmentEntity.getAppointmentId();
        } catch (DoctorAddAppointmentException | PatientAddAppointmentException ex) {
            throw new CreateAppointmentException("Unable to create appointment");
        }
    }
    
    

    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) {
        /*AppointmentEntity toDelete = entityManager.find(AppointmentEntity.class, appointmentEntity.getAppointmentId());
        entityManager.remove(toDelete);
        entityManager.persist(appointmentEntity);
        entityManager.flush();*/
        entityManager.merge(appointmentEntity);
    }

    @Override
    public void deleteAppointmentEntityById(long appointmentId) throws DoctorRemoveAppointmentException, PatientRemoveAppointmentException{
        AppointmentEntity appointmentEntity = entityManager.find(AppointmentEntity.class,appointmentId);
        DoctorEntity doctorEntity = entityManager.find(DoctorEntity.class,appointmentEntity.getDoctorEntity().getDoctorId());
        PatientEntity patientEntity = entityManager.find(PatientEntity.class, appointmentEntity.getPatientEntity().getPatientId());
        doctorEntity.removeAppointment(appointmentEntity);
        patientEntity.removeAppointment(appointmentEntity);
        entityManager.merge(doctorEntity);
        entityManager.merge(patientEntity);
        entityManager.remove(appointmentEntity);
        entityManager.flush();
    }

    @Override
    public List<AppointmentEntity> retrieveAllAppointments() {
        Query query = entityManager.createQuery("SELECT a FROM AppointmentEntity a ORDER BY a.appointmentId");
        return query.getResultList();
    }

    @Override
    public AppointmentEntity retrieveAppointmentById(long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = entityManager.find(AppointmentEntity.class, appointmentId);
        if(appointmentEntity == null) {
            throw new AppointmentNotFoundException("Appointment ID : " + appointmentId + " does not exist!");
        } else {
            return appointmentEntity;
        }
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByDoctorDate(DoctorEntity doctorEntity, Date date) {
        List<AppointmentEntity> tempList = doctorEntity.getDoctorAppointments();
        ArrayList<AppointmentEntity> doctorDateAppointments = new ArrayList<>();
        
        for(AppointmentEntity appointmentEntity : tempList) {
            if(appointmentEntity.getDate().equals(date)) {
                doctorDateAppointments.add(appointmentEntity);
            }
        }
        
        return doctorDateAppointments;
        
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByDoctor(DoctorEntity doctorEntity) {
       DoctorEntity fetchDoctor = entityManager.find(DoctorEntity.class, doctorEntity.getDoctorId());
       fetchDoctor.getDoctorAppointments().size();
       return doctorEntity.getDoctorAppointments();
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByPatient(PatientEntity patientEntity) {
        PatientEntity fetchPatient = entityManager.find(PatientEntity.class, patientEntity.getPatientId());
        fetchPatient.getPatientAppointments().size();
        //entityManager.merge(patientEntity);
        //patientEntity.getPatientAppointments().size();
        return fetchPatient.getPatientAppointments();
    }


    
    
    
    
    
    
    
    
    
    
    
}
