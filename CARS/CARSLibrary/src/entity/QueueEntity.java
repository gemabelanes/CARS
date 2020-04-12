/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author gem
 */
@Entity
public class QueueEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date queueDate;
    @Column(nullable = false)
    private int counter;

    public QueueEntity() {
    }

    public int getCounter() {
        return counter;
    }
    
    public int getCounterAndIncrement() {
        int returnValue = counter;
        counter++;
        return returnValue;
    }
    
    public void incrementCounter() {
        counter++;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public QueueEntity(Date date) {
        this();
        this.queueDate = date;
        this.counter = 1;
    }

    public Date getDate() {
        return queueDate;
    }

    public void setDate(Date date) {
        this.queueDate = date;
    }

    public Long getQueueId() {
        return queueId;
    }

    public void setQueueId(Long id) {
        this.queueId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (queueId != null ? queueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QueueEntity)) {
            return false;
        }
        QueueEntity other = (QueueEntity) object;
        if ((this.queueId == null && other.queueId != null) || (this.queueId != null && !this.queueId.equals(other.queueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QueueEntity[ id=" + queueId + " ]";
    }
    
}
