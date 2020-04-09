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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author gem
 */
public class ConsultationEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;
    @Column(nullable=false)
    private Integer queueNum;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private Date date;
    private Time time;
    
    
    @ManyToOne
    private PatientEntity patientEntity;
    @ManyToOne
    private DoctorEntity doctorEntity;

    public ConsultationEntity() {
    }

    public ConsultationEntity(Integer queueNum, PatientEntity patientEntity, DoctorEntity doctorEntity, String password, Date date, Time time) {
        this.queueNum = queueNum;
        this.patientEntity = patientEntity;
        this.doctorEntity = doctorEntity;
        this.password = password;
        this.date = date;
        this.time = time;
    }
    
    //TODO
    /*
    public ConsultationEntity(Integer queueNum, AppointmentEntity appointmentEntity) {
    
    }*/

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
