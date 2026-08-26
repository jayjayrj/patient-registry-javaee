package br.com.patientregistry.persistence;
import br.com.patientregistry.domain.Patient;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;
@Stateless public class PatientDAO {
  @PersistenceContext(unitName="patientRegistryPU") private EntityManager entityManager;
  public Patient save(Patient patient){if(patient.getId()==null){entityManager.persist(patient);return patient;}return entityManager.merge(patient);}
  public List<Patient> findAll(){return entityManager.createQuery("select p from Patient p order by p.fullName",Patient.class).getResultList();}
  public Patient findById(Long id){return entityManager.find(Patient.class,id);}
  public void delete(Patient patient){Patient managed=entityManager.contains(patient)?patient:entityManager.merge(patient);entityManager.remove(managed);}
}
