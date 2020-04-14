/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gem
 */
@Entity
public class ConsultationEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;
    @Column(nullable=false)
    private Integer queueNum;
    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(nullable=false)
    private Time time;
    
    
    
    @ManyToOne
    private PatientEntity patientEntity;
    @ManyToOne
    private DoctorEntity doctorEntity;

    public ConsultationEntity() {
    }

    public ConsultationEntity(Integer queueNum, PatientEntity patientEntity, DoctorEntity doctorEntity, Date date, Time time) {
        this.queueNum = queueNum;
        this.patientEntity = patientEntity;
        this.doctorEntity = doctorEntity;
        this.date = date;
        this.time = time;
    }
    
    public ConsultationEntity(Integer queueNum, AppointmentEntity appointmentEntity) {
        this.queueNum = queueNum;
        this.patientEntity = appointmentEntity.getPatientEntity();
        this.doctorEntity = appointmentEntity.getDoctorEntity();
        this.date = appointmentEntity.getDate();
        this.time = appointmentEntity.getTime();
    
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Integer getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(Integer queueNum) {
        this.queueNum = queueNum;
    }

    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.consultationId != null ? this.consultationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConsultationEntity)) 
        {
            return false;
        }
        
        ConsultationEntity other = (ConsultationEntity) object;
        
        if ((this.consultationId == null && other.consultationId != null) || (this.consultationId != null && !this.consultationId.equals(other.consultationId))) 
        {
            return false;
        }
        
        return true;
    }
}
