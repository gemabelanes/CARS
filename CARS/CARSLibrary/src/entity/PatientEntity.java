/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import util.exception.PatientAddAppointmentException;
import util.exception.PatientAddConsultationException;
import util.exception.PatientRemoveAppointmentException;
import util.exception.PatientRemoveConsultationException;

/**
 *
 * @author gem
 */
@Entity
public class PatientEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    @Column(nullable=false, unique = true)
    private String identityNumber;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Column(nullable=false)
    private String gender;
    @Column(nullable=false)
    private Integer age;
    @Column(nullable=false)
    private String phoneNumber;
    @Column(nullable=false)
    private String address;
    @Column(nullable=false)
    private String password;
    @Transient
    private String fullName;
    private String salt;
    
    @OneToMany(mappedBy = "patientEntity", fetch = FetchType.EAGER)
    private List<ConsultationEntity> patientConsultations;
    
    @OneToMany(mappedBy = "patientEntity", fetch = FetchType.EAGER)
    private List<AppointmentEntity> patientAppointments;

    public PatientEntity() {
        
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        salt = Base64.getEncoder().encodeToString(bytes);
        setPassword("123456"); //default password
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public PatientEntity(String identityNumber, String firstName, String lastName, String gender, Integer age, String phoneNumber, String address, String password) {
        this();
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        try {
            this.password = createHash(salt + password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating Patient");
        }
        this.fullName = firstName + " " + lastName;
    }

    public boolean verifyPassword(String password) {
        String enteredPassword;
        try {
            enteredPassword = createHash(salt + password);
            return enteredPassword.equals(this.password);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error verifying password");
        }
        return false;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = createHash(salt + password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error setting password");
        }
    }
    
    private String createHash(String password) throws NoSuchAlgorithmException {
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(password.getBytes()); 
            BigInteger temp = new BigInteger(1, md5); 
            String hash = temp.toString(16); 
            while (hash.length() < 32) { 
                hash = "0" + hash; 
            } 
            return hash;
        } catch (NoSuchAlgorithmException e) { 
            throw new NoSuchAlgorithmException("Error hashing password");
        } 
        
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ConsultationEntity> getPatientConsultations() {
        return patientConsultations;
    }

    public void setPatientConsultations(List<ConsultationEntity> patientConsultations) {
        this.patientConsultations = patientConsultations;
    }
    
    public void addConsultation(ConsultationEntity consultationEntity) throws PatientAddConsultationException {
        if(consultationEntity != null && !patientConsultations.contains(consultationEntity)) {
            patientConsultations.add(consultationEntity);
        } else {
            throw new PatientAddConsultationException("Consultation record already exists");
        }
    }
    
    public void removeConsultation(ConsultationEntity consultationEntity) throws PatientRemoveConsultationException {
        if(consultationEntity != null && patientConsultations.contains(consultationEntity)) {
            patientConsultations.remove(consultationEntity);
        } else {
            throw new PatientRemoveConsultationException("Patient ID : " + this.getPatientId()
                                                        + " does not contain Consultation ID" + consultationEntity.getConsultationId());
        }
    }
    
    public void addAppointment(AppointmentEntity appointmentEntity) throws PatientAddAppointmentException {
        
        if(appointmentEntity != null && !patientAppointments.contains(appointmentEntity)) {
            patientAppointments.add(appointmentEntity);
        } else {
            throw new PatientAddAppointmentException("Appointment record already exists");
        }
    }
    
    public void removeAppointment(AppointmentEntity appointmentEntity) throws PatientRemoveAppointmentException {
        if(appointmentEntity != null && patientAppointments.contains(appointmentEntity)) {
            patientAppointments.remove(appointmentEntity);
        } else {
            throw new PatientRemoveAppointmentException("Patient ID : " + this.getPatientId()
                                                        + " does not contain Appointment ID" + appointmentEntity.getAppointmentId());
        }
    }

    public List<AppointmentEntity> getPatientAppointments() {
        return patientAppointments;
    }

    public void setPatientAppointments(List<AppointmentEntity> patientAppointments) {
        this.patientAppointments = patientAppointments;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.patientId != null ? this.patientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PatientEntity)) 
        {
            return false;
        }
        
        PatientEntity other = (PatientEntity) object;
        
        if ((this.patientId == null && other.patientId != null) || (this.patientId != null && !this.patientId.equals(other.patientId))) 
        {
            return false;
        }
        
        return true;
    }
    
    
}
