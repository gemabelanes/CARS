/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author gem
 */
@Remote
public interface DoctorEntityControllerRemote  {

    Long createNewDoctor(DoctorEntity doctorEntity);

    List<DoctorEntity> retrieveAllDoctorEntities();

    DoctorEntity retrieveDoctorEntityById(long doctorId) throws DoctorNotFoundException;
    
    void updateDoctorEntity(DoctorEntity doctorEntity);

    boolean doctorAvailableAtTime(DoctorEntity doctorEntity, Time time, Date date);

    void deleteDoctorEntityById(long doctorId);

    Boolean doesDoctorExistByRegistration(String registration);
    
}
