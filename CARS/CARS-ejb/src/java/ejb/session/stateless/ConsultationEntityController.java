/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConsultationEntity;
import entity.DoctorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ConsultationNotFoundException;
import util.exception.CreateConsultationException;
import util.exception.DoctorAddConsultationException;
import util.exception.DoctorRemoveConsultationException;
import util.exception.PatientAddConsultationException;
import util.exception.PatientRemoveConsultationException;

/**
 *
 * @author gem
 */
@Stateless
@Local(ConsultationEntityControllerLocal.class)
@Remote(ConsultationEntityControllerRemote.class)
public class ConsultationEntityController implements ConsultationEntityControllerRemote, ConsultationEntityControllerLocal {
    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;
    
    public ConsultationEntityController() {
    }

    
    @Override
    public long createConsultationEntity(ConsultationEntity consultationEntity) throws CreateConsultationException {
        try {
            DoctorEntity doctorEntity = consultationEntity.getDoctorEntity();
            PatientEntity patientEntity = consultationEntity.getPatientEntity();
            doctorEntity.addConsultation(consultationEntity);
            patientEntity.addConsultation(consultationEntity);
            entityManager.persist(consultationEntity);
            entityManager.flush();
            return consultationEntity.getConsultationId();
        } catch (DoctorAddConsultationException | PatientAddConsultationException ex) {
            throw new CreateConsultationException("Unable to create consultation");
        }
        
        
    }
    
    
    @Override
    public List<ConsultationEntity> retrieveAllConsultations() {
        Query query = entityManager.createQuery("SELECT c FROM ConsultationEntity c ORDER BY c.consultationId");
        return query.getResultList();
    }

    @Override
    public ConsultationEntity retrieveConsultationById(long consultationId) throws ConsultationNotFoundException{
        ConsultationEntity consultationEntity = entityManager.find(ConsultationEntity.class,consultationId);
        
        if(consultationEntity == null) {
            throw new ConsultationNotFoundException("Consultation ID : " + consultationId + " does not exist!");
        } else {
            return consultationEntity;
        }
    }

    @Override
    public void updateConsultationEntity(ConsultationEntity consultationEntity) {
        entityManager.merge(consultationEntity);
    }

    @Override
    public void deleteConsultationEntityById(long consultationId) throws DoctorRemoveConsultationException, PatientRemoveConsultationException {
        ConsultationEntity consultationEntity = entityManager.find(ConsultationEntity.class,consultationId);
        DoctorEntity doctorEntity = consultationEntity.getDoctorEntity();
        PatientEntity patientEntity = consultationEntity.getPatientEntity();
        doctorEntity.removeConsultation(consultationEntity);
        patientEntity.removeConsultation(consultationEntity);
        entityManager.remove(consultationEntity);
        entityManager.flush();

        
    }
    
}
