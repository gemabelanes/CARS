/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateAppointmentException;
import util.exception.DoctorRemoveAppointmentException;
import util.exception.PatientRemoveAppointmentException;

/**
 *
 * @author gem
 */
@Local
public interface AppointmentEntityControllerLocal {

    long createAppointmentEntity(AppointmentEntity appointmentEntity) throws CreateAppointmentException;

    void updateAppointmentEntity(AppointmentEntity appointmentEntity);

    void deleteAppointmentEntityById(long appointmentId) throws DoctorRemoveAppointmentException, PatientRemoveAppointmentException;

    List<AppointmentEntity> retrieveAllAppointments();

    AppointmentEntity retrieveAppointmentById(long appointmentId) throws AppointmentNotFoundException;

    List<AppointmentEntity> retrieveAppointmentsByDoctorDate(DoctorEntity doctorEntity, Date date);

    List<AppointmentEntity> retrieveAppointmentsByDoctor(DoctorEntity doctorEntity);

    List<AppointmentEntity> retrieveAppointmentsByPatient(PatientEntity patientEntity);
    
}
