/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import cars.xmladapters.DateAdapter;
import cars.xmladapters.SQLTimeAdapter;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
    //@XmlElement(name = "consultationDate")
    //@XmlJavaTypeAdapter(DateAdapter.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date consultationDate;
    //@XmlElement(name = "consultationTime")
    ///@XmlJavaTypeAdapter(SQLTimeAdapter.class)
    @Column(nullable=false)
    private String consultationTime;
    
    
    
    @ManyToOne
    private PatientEntity patientEntity;
    @ManyToOne
    private DoctorEntity doctorEntity;

    public ConsultationEntity() {
    }

    public ConsultationEntity(Integer queueNum, PatientEntity patientEntity, DoctorEntity doctorEntity, Date date, String time) {
        this.queueNum = queueNum;
        this.patientEntity = patientEntity;
        this.doctorEntity = doctorEntity;
        this.consultationDate = date;
        this.consultationTime = time;
    }
    
    public ConsultationEntity(Integer queueNum, AppointmentEntity appointmentEntity) {
        this.queueNum = queueNum;
        this.patientEntity = appointmentEntity.getPatientEntity();
        this.doctorEntity = appointmentEntity.getDoctorEntity();
        this.consultationDate = appointmentEntity.getDate();
        this.consultationTime = appointmentEntity.getTime();
    
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

     @XmlTransient
    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

     @XmlTransient
    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
    }

    public Date getDate() {
        return consultationDate;
    }

    public void setDate(Date date) {
        this.consultationDate = date;
    }

    public String getTime() {
        return consultationTime;
    }

    public void setTime(String time) {
        this.consultationTime = time;
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
