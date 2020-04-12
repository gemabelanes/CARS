/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.QueueEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.QueueNotFoundException;

/**
 *
 * @author gem
 */
@Stateless
@Local(QueueEntityControllerLocal.class)
@Remote(QueueEntityControllerRemote.class)
public class QueueEntityController implements QueueEntityControllerRemote, QueueEntityControllerLocal {
    @PersistenceContext(unitName = "CARSLibraryPU")
    private javax.persistence.EntityManager entityManager;

    public QueueEntityController() {
    }

    @Override
    public long createQueueEntity(QueueEntity queueEntity) {
       entityManager.persist(queueEntity);
       entityManager.flush();
       
       return queueEntity.getQueueId();
    }

    @Override
    public boolean doesQueueEntityExist(Date date) {
        Query query = entityManager.createQuery("SELECT q FROM QueueEntity q WHERE q.queueDate = :inQueueDate");
        query.setParameter("inQueueDate", date);
        
        if(query.getResultList().isEmpty() || query.getResultList() == null) {
            System.err.println("QUERY SIZE : " + query.getResultList().size());
            return false;
        }
        
        return true;
    }
    
    

    @Override
    public Integer retrieveQueueNumber(QueueEntity queueEntity) {
        System.err.println("Running returnQueue");
        int returnQueue = queueEntity.getCounter();
        System.err.println("Running icrementCounter");
        queueEntity.incrementCounter();
        System.err.println("updating QueueEntity");
        updateQueueEntity(queueEntity);
        System.err.println("Returning");
        return returnQueue;
    }

    @Override
    public QueueEntity retrieveQueueEntityByDate(Date date) throws QueueNotFoundException {
        Query query = entityManager.createQuery("SELECT q FROM QueueEntity q WHERE q.queueDate = :inQueueDate");
        query.setParameter("inQueueDate", date);
        
        try {
            QueueEntity queueEntity = (QueueEntity)query.getSingleResult();
            return queueEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new QueueNotFoundException("Queue for " + new SimpleDateFormat("yyyy-MM-dd").format(date) + " has not yet been created" );
                    
        }
    }

    @Override
    public void updateQueueEntity(QueueEntity queueEntity) {
        entityManager.merge(queueEntity);
    }

    @Override
    public void deleteQueueEntity(QueueEntity queueEntity) {
        QueueEntity removeEntity = entityManager.find(QueueEntity.class, queueEntity.getQueueId());
        entityManager.remove(removeEntity);
        entityManager.flush();
    }
    
    
    
    
    
}
