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
import java.util.Base64;
import java.util.Objects;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author gem
 */
@Entity
public class StaffEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Column(nullable=false, unique = true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Transient
    private String fullName;
    private String salt;

    public StaffEntity() {
        
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        salt = Base64.getEncoder().encodeToString(bytes);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public StaffEntity(String firstName, String lastName, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.fullName = firstName + " " + lastName;
        try {
            this.password = createHash(salt + password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating Staff");
        }
        
        
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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = createHash(salt + password);
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error setting password");
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.staffId != null ? this.staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StaffEntity)) 
        {
            return false;
        }
        
        StaffEntity other = (StaffEntity) object;
        
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) 
        {
            return false;
        }
        
        return true;
    }    
    
}
