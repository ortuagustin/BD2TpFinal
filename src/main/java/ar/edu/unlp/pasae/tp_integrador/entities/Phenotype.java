package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.Collection;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PHENOTYPE_KIND")
public abstract class Phenotype {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotEmpty
  private String name;

  public Phenotype(Long id, String name) {
    super();
    this.setId(id);
    this.setName(name);
  }

  public Phenotype(String name) {
    super();
    this.setId(id);
    this.setName(name);
  }

  protected Phenotype() {
    super();
  }

  /**
   * Checks if the value to assign is valid in the Phenotype
   *
   * @param vale the value to set
   *
   * @return True if the validation passes; False otherwise
   */
  public abstract Boolean validate(String value);

  public abstract String getKind();

  public abstract Collection<AnalysisGroup> getAnalysisGroups(Analysis analysis);

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
}