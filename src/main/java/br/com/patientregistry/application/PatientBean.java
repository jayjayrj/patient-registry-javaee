package br.com.patientregistry.application;
import br.com.patientregistry.domain.Patient;
import br.com.patientregistry.persistence.PatientDAO;
import br.com.patientregistry.service.PatientAgeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.*;
import java.io.Serializable;
import java.util.List;
import org.primefaces.event.RowEditEvent;
@Named @ViewScoped public class PatientBean implements Serializable {
  private static final long serialVersionUID=1L;
  @Inject private PatientDAO patientDAO; @Inject private PatientAgeService ageService;
  private List<Patient> patients; private Patient patient;
  @PostConstruct public void init(){patient=new Patient();loadPatients();}
  public void save(){patientDAO.save(patient);message("Paciente salvo",patient.getFullName());patient=new Patient();loadPatients();}
  public void onRowEdit(RowEditEvent<Patient> event){Patient edited=event.getObject();patientDAO.save(edited);message("Cadastro atualizado",edited.getFullName());loadPatients();}
  public void delete(Patient selected){patientDAO.delete(selected);message("Paciente removido",selected.getFullName());loadPatients();}
  public void calculateAge(Patient selected){int age=ageService.calculateAge(selected.getId());message("Idade calculada",selected.getFullName()+": "+age+" anos");}
  private void loadPatients(){patients=patientDAO.findAll();}
  private void message(String summary,String detail){FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,summary,detail));}
  public List<Patient> getPatients(){return patients;} public Patient getPatient(){return patient;} public void setPatient(Patient patient){this.patient=patient;}
}
