package ar.edu.unlp.pasae.tp_integrador.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class NumericPhenotypeValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  private NumericPhenotype phenotype;
  @OneToOne
  private Patient patient;
  private Long value;

  @SuppressWarnings("unused")
  private NumericPhenotypeValue() {
    super();
  }

  public NumericPhenotypeValue(NumericPhenotype phenotype, Long value) {
    super();
    this.setPatient(patient);
    this.setPhenotype(phenotype);
    this.setValue(value);
  }

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

  /**
   * @return the value
   */
  public Long getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Long value) {
    this.value = value;
  }

  /**
   * @return the phenotype
   */
  public NumericPhenotype getPhenotype() {
    return phenotype;
  }

  /**
   * @param phenotype the phenotype to set
   */
  public void setPhenotype(NumericPhenotype phenotype) {
    this.phenotype = phenotype;
  }
}
