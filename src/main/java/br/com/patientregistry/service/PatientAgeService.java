package br.com.patientregistry.service;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
@Stateless public class PatientAgeService {
  @PersistenceContext(unitName="patientRegistryPU") private EntityManager entityManager;
  public int calculateAge(Long patientId){StoredProcedureQuery query=entityManager.createStoredProcedureQuery("P_PATIENT_AGE").registerStoredProcedureParameter("p_patient_id",Long.class,ParameterMode.IN).registerStoredProcedureParameter("p_age_years",Integer.class,ParameterMode.OUT).setParameter("p_patient_id",patientId);query.execute();Number age=(Number)query.getOutputParameterValue("p_age_years");return age.intValue();}
}
