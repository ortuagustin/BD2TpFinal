package ar.edu.unlp.pasae.tp_integrador.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
@SuppressWarnings("unused")
public class Genotype {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * posicion en el genoma para este "valor"
   */
  @NotNull
  private Integer snp;

  /**
  * El valor del genotipo en si, es decir, los dos alelos (A, D, T, G)
   */
  @NotEmpty
  private String value;

  public Genotype(Long id, Integer snp, String value) {
    super();
    this.setId(id);
    this.setSnp(snp);
    this.setValue(value);
  }

  public Genotype(Integer snp, String value) {
    super();
    this.setSnp(snp);
    this.setValue(value);
  }

  /**
   * @return el valor del alelo heredado del padre
   */
  public String getFatherValue() {
    Character c = this.getValue().charAt(0);

    return c.toString();
  }

  /**
   * @return el valor del alelo heredado de la madre
   */
  public String getMotherValue() {
    Character c = this.getValue().charAt(1);

    return c.toString();
  }

  private Genotype() {
    super();
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the snp
   */
  public Integer getSnp() {
    return snp;
  }

  /**
   * @param snp the snp to set
   */
  public void setSnp(Integer snp) {
    this.snp = snp;
  }
}