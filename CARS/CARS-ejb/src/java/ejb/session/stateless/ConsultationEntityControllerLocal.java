/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConsultationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ConsultationNotFoundException;
import util.exception.CreateConsultationException;
import util.exception.DoctorAddConsultationException;
import util.exception.DoctorRemoveConsultationException;
import util.exception.PatientRemoveConsultationException;

/**
 *
 * @author gem
 */
@Local
public interface ConsultationEntityControllerLocal {

    List<ConsultationEntity> retrieveAllConsultations();

    ConsultationEntity retrieveConsultationById(long consultationId) throws ConsultationNotFoundException;

    void updateConsultationEntity(ConsultationEntity consultationEntity);

    void deleteConsultationEntityById(long consultationId) throws DoctorRemoveConsultationException, PatientRemoveConsultationException;

    long createConsultationEntity(ConsultationEntity parameter) throws CreateConsultationException;
    
}
