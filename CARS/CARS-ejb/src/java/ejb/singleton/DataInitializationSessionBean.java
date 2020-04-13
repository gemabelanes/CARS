package ejb.session.singleton;

import ejb.session.stateless.StaffEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import entity.StaffEntity;
import entity.PatientEntity;
import entity.DoctorEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;


@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean 
{
    @PersistenceContext(unitName = "CARSLibraryPU")
    private EntityManager em;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private PatientEntityControllerLocal patientEntityControllerLocal;
    @EJB
    private DoctorEntityControllerLocal doctorEntityControllerLocal;

    public DataInitializationSessionBean()
    {
    }

    @PostConstruct
    public void postConstruct()
    {
        Long id = new Long("1");
        StaffEntity staff = em.find(StaffEntity.class, id);
        if (staff == null)
        {
            initializeData();
        }
    }

    private void initializeData()
    {
        staffEntityControllerLocal.createNewStaffEntity(new StaffEntity("Eric", "Some", "manager", "password"));
        staffEntityControllerLocal.createNewStaffEntity(new StaffEntity("Victoria", "Newton", "nurse", "password"));

        patientEntityControllerLocal.createNewPatient(new PatientEntity("S9867027A", "Sarah", "Yi", "F", 22, "93718799", "13, Clementi Road", "DY3ihrBrkt QyJIz6uMD sqA"));
        patientEntityControllerLocal.createNewPatient(new PatientEntity("G1314207T", "Rajesh", "Singh", "M", 36, "93506839", "15, Mountbatten Road", "Qa0Xm0UFdx3HZ6Xs7tyKKQ"));

        doctorEntityControllerLocal.createNewDoctor(new DoctorEntity("Tan", "Ming", "S10011", "BMBS"));
        doctorEntityControllerLocal.createNewDoctor(new DoctorEntity("Clair", "Han", "S41221", "MBBCh"));
        doctorEntityControllerLocal.createNewDoctor(new DoctorEntity("Robert", "Blake", "S58201", "MBBS"));
    }

}
