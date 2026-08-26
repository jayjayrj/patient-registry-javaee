package br.com.patientregistry.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import jakarta.persistence.*;
import java.lang.reflect.Field;
import org.junit.jupiter.api.*;
import org.mockito.*;
class PatientAgeServiceTest {
  @Mock EntityManager entityManager; @Mock StoredProcedureQuery query; PatientAgeService service;
  @BeforeEach void setUp() throws Exception {MockitoAnnotations.openMocks(this);service=new PatientAgeService();Field field=PatientAgeService.class.getDeclaredField("entityManager");field.setAccessible(true);field.set(service,entityManager);when(entityManager.createStoredProcedureQuery("P_PATIENT_AGE")).thenReturn(query);when(query.registerStoredProcedureParameter(anyString(),any(),any())).thenReturn(query);when(query.setParameter(anyString(),any())).thenReturn(query);}
  @Test void invokesOracleProcedureAndReturnsAge(){when(query.getOutputParameterValue("p_age_years")).thenReturn(34);assertEquals(34,service.calculateAge(10L));verify(query).setParameter("p_patient_id",10L);verify(query).execute();verify(query).getOutputParameterValue("p_age_years");}
}
