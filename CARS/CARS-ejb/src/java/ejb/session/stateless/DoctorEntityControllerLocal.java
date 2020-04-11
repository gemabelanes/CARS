/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.sql.Time;
import java.util.List;
import javax.ejb.Local;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author gem
 */
@Local
public interface DoctorEntityControllerLocal {

    Long createNewDoctor(DoctorEntity doctorEntity);

    List<DoctorEntity> retrieveAllDoctorEntities();

    DoctorEntity retrieveDoctorEntityById(long doctorId) throws DoctorNotFoundException;

    void updateDoctorEntity(DoctorEntity doctorEntity);

    void deleteDoctorEntityById(long doctorId);

    boolean doctorAvailableAtTime(DoctorEntity doctorEntity, Time time);

    
}
