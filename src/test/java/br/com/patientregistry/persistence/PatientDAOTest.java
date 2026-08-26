package br.com.patientregistry.persistence;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import br.com.patientregistry.domain.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import org.mockito.*;
class PatientDAOTest {
  @Mock EntityManager entityManager; @Mock TypedQuery<Patient> query; PatientDAO dao;
  @BeforeEach void setUp() throws Exception {MockitoAnnotations.openMocks(this);dao=new PatientDAO();Field field=PatientDAO.class.getDeclaredField("entityManager");field.setAccessible(true);field.set(dao,entityManager);}
  @Test void persistsNewPatient(){Patient patient=patient(null);assertSame(patient,dao.save(patient));verify(entityManager).persist(patient);verify(entityManager,never()).merge(any());}
  @Test void mergesExistingPatient(){Patient patient=patient(7L);Patient merged=patient(7L);when(entityManager.merge(patient)).thenReturn(merged);assertSame(merged,dao.save(patient));verify(entityManager).merge(patient);}
  @Test void listsPatientsAlphabetically(){List<Patient> expected=List.of(patient(1L));when(entityManager.createQuery("select p from Patient p order by p.fullName",Patient.class)).thenReturn(query);when(query.getResultList()).thenReturn(expected);assertEquals(expected,dao.findAll());}
  @Test void removesAttachedPatient(){Patient patient=patient(2L);when(entityManager.contains(patient)).thenReturn(true);dao.delete(patient);verify(entityManager).remove(patient);}
  private Patient patient(Long id){Patient p=new Patient();p.setId(id);p.setFullName("Ana Silva");p.setDateOfBirth(LocalDate.of(1990,1,1));return p;}
}
