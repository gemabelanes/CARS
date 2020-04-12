/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.QueueEntity;
import java.util.Date;
import javax.ejb.Local;
import util.exception.QueueNotFoundException;

/**
 *
 * @author gem
 */
@Local
public interface QueueEntityControllerLocal {

    long createQueueEntity(QueueEntity queueEntity);

    boolean doesQueueEntityExist(Date date);

    Integer retrieveQueueNumber(QueueEntity queueEntity);

    QueueEntity retrieveQueueEntityByDate(Date date) throws QueueNotFoundException;

    void updateQueueEntity(QueueEntity parameter);

    void deleteQueueEntity(QueueEntity queueEntity);
    
}
