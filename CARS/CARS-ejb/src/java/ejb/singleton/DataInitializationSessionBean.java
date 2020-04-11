/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.singleton;

import ejb.session.stateless.ConsultationEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gem
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {
    @PersistenceContext(unitName = "CARSLibraryPU")
    private EntityManager em;
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private PatientEntityControllerLocal patientEntityControllerLocal;
    @EJB
    private DoctorEntityControllerLocal doctorEntityControllerLocal;
    @EJB
    private ConsultationEntityControllerLocal consultationEntityController;

    public DataInitializationSessionBean() {

    }

    @PostConstruct
    public void postConstruct() {
        Long id = new Long("1");
        StaffEntity staff = em.find(StaffEntity.class, id);
        if(staff == null)
        {
            initializeData();
        }
       
    }

    //after appointment id, check if date is today. register consultation
    private void initializeData() {
        StaffEntity staff1 = new StaffEntity();
        StaffEntity staff2 = new StaffEntity();

        staff1.setFirstName("Linda");
        staff1.setLastName("Chua");
        staff1.setPassword("password");
        staff1.setStaffId((long) 1);
        staff1.setUsername("manager");

        staff2.setStaffId((long) 2);
        staff2.setFirstName("Barbara");
        staff2.setLastName("Durham");
        staff2.setUsername("nurse");
        staff2.setPassword("password");

        DoctorEntity doctor1 = new DoctorEntity();
        DoctorEntity doctor2 = new DoctorEntity();
        DoctorEntity doctor3 = new DoctorEntity();

        doctor1.setFirstName("Peter");
        doctor1.setLastName("Lee");
        doctor1.setRegistration("S18018");
        doctor1.setDoctorId((long) 1);
        doctor1.setQualifications("MBBS");

        doctor2.setFirstName("Cindy");
        doctor2.setLastName("Leong");
        doctor2.setRegistration("S64921");
        doctor2.setDoctorId((long) 2);
        doctor2.setQualifications("BMedSc");

        doctor3.setFirstName("Matthew");
        doctor3.setLastName("Liu");
        doctor3.setRegistration("S38101");
        doctor3.setDoctorId((long) 3);
        doctor3.setQualifications("MBBS");

        PatientEntity patient1 = new PatientEntity();
        PatientEntity patient2 = new PatientEntity();

        patient1.setPatientId((long) 1);
        patient1.setIdentityNumber("S7483027A");
        patient1.setFirstName("Tony");
        patient1.setLastName("Teo");
        patient1.setGender("Male");
        patient1.setAge(44);
        patient1.setPhoneNumber("87297373");
        patient1.setAddress("11 Tampines Ave 3");
        //patient1.setIdentityNumber("123456");
        patient1.setPassword("password");

        patient2.setPatientId((long) 2);
        patient2.setIdentityNumber("S8381028X");
        patient2.setFirstName("Wendy");
        patient2.setLastName("Tan");
        patient2.setGender("Female");
        patient2.setAge(35);
        patient2.setPhoneNumber("97502837");
        patient2.setAddress("15 Computing Dr");
        //patient2.setIdentityNumber("123456");
        patient2.setPassword("password");

        staffEntityControllerLocal.createNewStaffEntity(staff1);
        staffEntityControllerLocal.createNewStaffEntity(staff2);
        patientEntityControllerLocal.createNewPatient(patient2);
        patientEntityControllerLocal.createNewPatient(patient1);
        doctorEntityControllerLocal.createNewDoctor(doctor3);
        doctorEntityControllerLocal.createNewDoctor(doctor2);
        doctorEntityControllerLocal.createNewDoctor(doctor1);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
