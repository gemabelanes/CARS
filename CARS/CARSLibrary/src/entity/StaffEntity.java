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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;

    public StaffEntity() {
    }

    public StaffEntity(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        try {
            this.password = createHash(password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating Staff");
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
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
            this.password = createHash(password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StaffEntity.class.getName()).log(Level.SEVERE, null, ex);
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
