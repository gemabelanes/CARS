/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConsultationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ConsultationNotFoundException;
import util.exception.CreateConsultationException;
import util.exception.DoctorAddConsultationException;
import util.exception.DoctorRemoveConsultationException;
import util.exception.PatientRemoveConsultationException;

/**
 *
 * @author gem
 */
@Remote
public interface ConsultationEntityControllerRemote {

    List<ConsultationEntity> retrieveAllConsultations();

    ConsultationEntity retrieveConsultationById(long consultationId) throws ConsultationNotFoundException;

    void updateConsultationEntity(ConsultationEntity consultationEntity);

    void deleteConsultationEntityById(long consultationId)  throws DoctorRemoveConsultationException, PatientRemoveConsultationException;

    long createConsultationEntity(ConsultationEntity parameter) throws CreateConsultationException;
    
}
