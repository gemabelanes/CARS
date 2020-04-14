/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author gem
 */
@Local
public interface PatientEntityControllerLocal {

    Long createNewPatient(PatientEntity patientEntity);

    List<PatientEntity> retrieveAllPatientEntities();

    PatientEntity retrievePatientEntityById(long patientId);

    void updatePatientEntity(PatientEntity patientEntity);

    void deletePatientEntityById(java.lang.Long patientId);

    PatientEntity retrievePatientEntityByIc(String patientIc) throws PatientNotFoundException;

    boolean doesPatientExistByIc(String patientIc);

    PatientEntity patientLogin(String identityNumber, String password) throws InvalidLoginException;

    boolean patientAvailableAtTime(entity.PatientEntity patientEntity, Time time, java.util.Date date);


    
}
