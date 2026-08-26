package br.com.patientregistry.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
@Entity @Table(name="PATIENT")
public class Patient implements Serializable {
  private static final long serialVersionUID=1L;
  @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="patientSequence") @SequenceGenerator(name="patientSequence",sequenceName="SEQ_PATIENT",allocationSize=1) private Long id;
  @NotBlank(message="Informe o nome completo") @Size(min=3,max=120,message="O nome deve ter entre 3 e 120 caracteres") @Column(name="FULL_NAME",nullable=false,length=120) private String fullName;
  @NotNull(message="Informe a data de nascimento") @Past(message="A data de nascimento deve estar no passado") @Column(name="DATE_OF_BIRTH",nullable=false) private LocalDate dateOfBirth;
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getFullName(){return fullName;} public void setFullName(String fullName){this.fullName=fullName==null?null:fullName.trim();}
  public LocalDate getDateOfBirth(){return dateOfBirth;} public void setDateOfBirth(LocalDate dateOfBirth){this.dateOfBirth=dateOfBirth;}
  @Override public boolean equals(Object object){if(this==object)return true;if(!(object instanceof Patient))return false;Patient other=(Patient)object;return id!=null&&Objects.equals(id,other.id);}
  @Override public int hashCode(){return getClass().hashCode();}
}
