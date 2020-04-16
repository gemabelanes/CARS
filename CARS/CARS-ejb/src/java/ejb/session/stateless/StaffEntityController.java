/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author gem
 */
@Stateless
@Local(StaffEntityControllerLocal.class)
@Remote(StaffEntityControllerRemote.class)
public class StaffEntityController implements StaffEntityControllerRemote, StaffEntityControllerLocal {
    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;
    
    public StaffEntityController() {
    }

    @Override
    public long createNewStaffEntity(StaffEntity staffEntity) {
        entityManager.persist(staffEntity);
        entityManager.flush();
        return staffEntity.getStaffId();
    }

    @Override
    public List<StaffEntity> retrieveAllStaffEntity() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s ORDER BY s.staffId");
        return query.getResultList();
    }

    @Override
    public StaffEntity retrieveStaffByStaffId(long staffId) throws StaffNotFoundException {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if(staffEntity == null){
            throw new StaffNotFoundException("Staff ID : " + staffId + " does not exist!");
        } else {
            return staffEntity;
        }
    }
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            
            if(staffEntity.verifyPassword(password))
            {
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void updateStaffEntity(StaffEntity staffEntity) {
        entityManager.merge(staffEntity);
    }

    @Override
    public void deleteStaffEntityById(long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        entityManager.remove(staffEntity);
        entityManager.flush();
    }

    @Override
    public boolean doesUsernameExist(String username) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        System.err.println("QUERY SIZE IS : " + query.getResultList().size());
        if(query.getResultList().isEmpty() || query.getResultList() == null ) {
            return false;
        }
        return true;
    }
    
    
}
