/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author gem
 */
@Stateless
@Local(DoctorEntityControllerLocal.class)
@Remote(DoctorEntityControllerRemote.class)
public class DoctorEntityController implements DoctorEntityControllerRemote, DoctorEntityControllerLocal {

    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;

    public DoctorEntityController() {
    }

    @Override
    public Long createNewDoctor(DoctorEntity doctorEntity) {
            entityManager.persist(doctorEntity);
            entityManager.flush();
            return doctorEntity.getDoctorId();
    }

    @Override
    public List<DoctorEntity> retrieveAllDoctorEntities() {
        Query query = entityManager.createQuery("SELECT d FROM DoctorEntity d ORDER BY d.doctorId");
        return query.getResultList();
    }

    @Override
    public DoctorEntity retrieveDoctorEntityById(long doctorId) throws DoctorNotFoundException {
        DoctorEntity doctorEntity = entityManager.find(DoctorEntity.class, doctorId);
        
        if(doctorEntity == null) {
            throw new DoctorNotFoundException("Doctor ID " + doctorId + " does not exist!");
        } else {
            return doctorEntity;
        }
    }

    @Override
    public void updateDoctorEntity(DoctorEntity doctorEntity) {
        entityManager.merge(doctorEntity);
    }

    @Override
    public void deleteDoctorEntityById(long doctorId) {
        DoctorEntity doctorEntity = entityManager.find(DoctorEntity.class, doctorId);
        entityManager.remove(doctorEntity);
        entityManager.flush();
    }
    
    
    
    
    
    
}
