/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import cars.xmladapters.DateAdapter;
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
public class AppointmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate;
    @Column(nullable=false)
    private String appointmentTime;
    
    @ManyToOne
    private PatientEntity patientEntity;
    @ManyToOne
    private DoctorEntity doctorEntity;
    
    private boolean consumed = false;

    public AppointmentEntity() {
    }

    public AppointmentEntity(Date date, String time, PatientEntity patientEntity, DoctorEntity doctorEntity) {
        this.appointmentDate = date;
        this.appointmentTime = time;
        this.patientEntity = patientEntity;
        this.doctorEntity = doctorEntity;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed() {
        consumed = true;
    }

    
    
    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getDate() {
        return appointmentDate;
    }

    public void setDate(Date date) {
        this.appointmentDate = date;
    }
    
    public String getTime() {
        return appointmentTime;
    }
    
    public void setTime(String time) {
        this.appointmentTime = time;
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
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.appointmentId != null ? this.appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AppointmentEntity)) 
        {
            return false;
        }
        
        AppointmentEntity other = (AppointmentEntity) object;
        
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) 
        {
            return false;
        }
        
        return true;
    }
    
    
}
