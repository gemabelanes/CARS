/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import util.exception.DoctorAddAppointmentException;
import util.exception.DoctorAddConsultationException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.DoctorRemoveConsultationException;

/**
 *
 * @author gem
 */
@Entity
public class DoctorEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Column(nullable=false, unique = true)
    private String registration;
    @Column(nullable=false)
    private String qualifications;
    @Transient
    private String fullName;
    
    @OneToMany(mappedBy = "doctorEntity", fetch = FetchType.EAGER)
    private List<ConsultationEntity> doctorConsultations;
    
    @OneToMany(mappedBy = "doctorEntity", fetch = FetchType.EAGER)
    private List<AppointmentEntity> doctorAppointments;
    
    public DoctorEntity(){
    }

    public DoctorEntity(String firstName, String lastName, String registration, String qualifications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.registration = registration;
        this.qualifications = qualifications;
        this.fullName = firstName + " " + lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.fullName = firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.doctorId != null ? this.doctorId.hashCode() : 0);
        
        return hash;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof DoctorEntity)) 
        {
            return false;
        }
        
        DoctorEntity other = (DoctorEntity) object;
        
        if ((this.doctorId == null && other.doctorId != null) || (this.doctorId != null && !this.doctorId.equals(other.doctorId))) 
        {
            return false;
        }
        
        return true;
    }

    public List<ConsultationEntity> getDoctorConsultations() {
        return doctorConsultations;
    }

    public void setDoctorConsultations(List<ConsultationEntity> doctorConsultations) {
        this.doctorConsultations = doctorConsultations;
    }

    public List<AppointmentEntity> getDoctorAppointments() {
        return doctorAppointments;
    }

    public void setDoctorAppointments(List<AppointmentEntity> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }
    
    
    
    public List<ConsultationEntity> getConsultations() {
        return doctorConsultations;
    }
    
    public void setConsultations(List<ConsultationEntity> consultations) {
        this.doctorConsultations = consultations;
    }
    
    public void addConsultation(ConsultationEntity consultationEntity) throws DoctorAddConsultationException {
        if(consultationEntity != null && !doctorConsultations.contains(consultationEntity)) {
            doctorConsultations.add(consultationEntity);
        } else {
            throw new DoctorAddConsultationException("Consultation record already exists");
        }
    }
    
    public void addAppointment(AppointmentEntity appointmentEntity) throws DoctorAddAppointmentException {
        //doctorAppointments.add(appointmentEntity);
        if(appointmentEntity != null && !doctorAppointments.contains(appointmentEntity)) {
            doctorAppointments.add(appointmentEntity);
        } else {
            throw new DoctorAddAppointmentException("Appointment record already exists");
        }
    }
    
    
    public void removeConsultation(ConsultationEntity consultationEntity) throws DoctorRemoveConsultationException {
        if(consultationEntity != null && this.doctorConsultations.contains(consultationEntity)) {
            this.doctorConsultations.remove(consultationEntity);
        } else {
            throw new DoctorRemoveConsultationException("Doctor ID : " + this.getDoctorId()
                                                        + " does not contain Consultation ID" + consultationEntity.getConsultationId());
        }
    }
    
    public void removeAppointment(AppointmentEntity appointmentEntity) throws DoctorRemoveAppointmentException {
        if(appointmentEntity != null && this.doctorAppointments.contains(appointmentEntity)) {
            this.doctorAppointments.remove(appointmentEntity);
        } else {
            throw new DoctorRemoveAppointmentException("Doctor ID : " + this.getDoctorId()
                                                        + " does not contain Appointment ID" + appointmentEntity.getAppointmentId());
        }
    }

}
