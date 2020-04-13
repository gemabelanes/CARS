/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ConsultationEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author gem
 */
@Stateless
public class PatientEntityController implements PatientEntityControllerRemote, PatientEntityControllerLocal {

    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;

    public PatientEntityController() {
    }

    @Override
    public Long createNewPatient(PatientEntity patientEntity) {
        entityManager.persist(patientEntity);
        entityManager.flush();
        return patientEntity.getPatientId();
    }

    @Override
    public List<PatientEntity> retrieveAllPatientEntities() {
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p ORDER BY p.patientId");
        return query.getResultList();
    }

    @Override
    public PatientEntity retrievePatientEntityById(long patientId) {
        return null;
    }

    @Override
    public void updatePatientEntity(PatientEntity patientEntity) {
        entityManager.merge(patientEntity);
    }

    @Override
    public void deletePatientEntityById(java.lang.Long patientId) {
        PatientEntity patientEntity = entityManager.find(PatientEntity.class, patientId);
        entityManager.remove(patientEntity);
        entityManager.flush();
    }

    @Override
    public PatientEntity retrievePatientEntityByIc(String patientIc) throws PatientNotFoundException {
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p WHERE p.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", patientIc);
        
        try {
            
            PatientEntity returnPatient = (PatientEntity)query.getSingleResult();
            return returnPatient;
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new PatientNotFoundException("Patient IC : " + patientIc + " does not exist!");
        }
    }
    
    

    @Override
    public boolean doesPatientExistByIc(String patientIc) {
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p WHERE p.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", patientIc);
        System.err.println("QUERY SIZE IS : " + query.getResultList().size());
        if(query.getResultList().isEmpty() || query.getResultList() == null ) {
            return false;
        }
        return true;
    }

    @Override
    public boolean patientAvailableAtTime(PatientEntity patientEntity, Time time, Date date) {
        PatientEntity fetchPatient = entityManager.find(PatientEntity.class, patientEntity.getPatientId());
        
        fetchPatient.getPatientAppointments();
        fetchPatient.getPatientConsultations();
        
        List<ConsultationEntity> patientConsultations = fetchPatient.getPatientConsultations();
        List<AppointmentEntity> patientAppointments = fetchPatient.getPatientAppointments();
        
        for(ConsultationEntity consultationEntity : patientConsultations) {
            if(consultationEntity.getDate().equals(date) && consultationEntity.getTime().equals(time)) {
                return false;
            }
        }

        for(AppointmentEntity appointmentEntity : patientAppointments) {
            if(appointmentEntity.getDate().equals(date) && appointmentEntity.getTime().equals(time)) {
                return false;
            }
        }
        return true;
        
        
    }

    @Override
    public PatientEntity patientLogin(String username, String password) throws InvalidLoginException {
        try {
            PatientEntity patientEntity = retrievePatientEntityByIc(username);
            
            if(patientEntity.verifyPassword(password)) {
                return patientEntity;
            } else {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
            
        } catch (PatientNotFoundException ex) {
            throw new InvalidLoginException("Patient does not exist");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
