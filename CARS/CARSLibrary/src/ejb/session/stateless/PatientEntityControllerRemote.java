/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.PatientNotFoundException;

/**
 *
 * @author gem
 */
@Remote
public interface PatientEntityControllerRemote {

    Long createNewPatient(PatientEntity patientEntity);

    List<PatientEntity> retrieveAllPatientEntities();

    PatientEntity retrievePatientEntityById(long patientId);

    void updatePatientEntity(PatientEntity patientEntity);

    void deletePatientEntityById(java.lang.Long patientId);

    PatientEntity retrievePatientEntityByIc(String patientIc) throws PatientNotFoundException;
    
}
