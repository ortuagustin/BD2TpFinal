package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class CategoricPhenotype extends Phenotype {
  @ElementCollection
  @NotEmpty
  private Map<Long, String> values = new HashMap<Long, String>();

  public static final class CategoricPhenotypeBuilder {
    private String name;
    private Map<Long, String> values = new HashMap<Long, String>();

    private CategoricPhenotypeBuilder() {
    }

    public CategoricPhenotypeBuilder addValues(final Map<Long, String> values) {
      this.values = values;

      return this;
    }

    public CategoricPhenotypeBuilder addName(final String name) {
      this.name = name;

      return this;
    }

    public CategoricPhenotype createPhenotype() {
      final CategoricPhenotype phenotype = new CategoricPhenotype(this.name, this.values);

      return phenotype;
    }
  }

  public static final CategoricPhenotypeBuilder builder() {
    return new CategoricPhenotypeBuilder();
  }

  public CategoricPhenotype(String name, Map<Long, String> values) {
    super(name);
    this.setValues(values);
  }

  public CategoricPhenotype(Long id, String name, Map<Long, String> values) {
    super(id, name);
    this.setValues(values);
  }

  protected CategoricPhenotype() {
    super();
  }

  /**
   * @return the values
   */
  public Map<Long, String> getValues() {
    return values;
  }

  /**
   * @param values the values to Map
   */
  public void setValues(Map<Long, String> values) {
    this.values = values;
  }

  @Override
  public Boolean validate(String value) {
    return this.getValues().containsValue(value);
  }

  @Override
  public String getKind() {
    return "Categoric";
  }

  @Override
  public Collection<AnalysisGroup> getAnalysisGroups(Analysis analysis) {
    Collection<AnalysisGroup> groups = new ArrayList<>();

    for (String each : this.getValues().values()) {
      groups.add(new AnalysisGroup(each, this.patientsWithPhenotypeValue(each, analysis.getPatients().stream())));
    }

    return groups;
  }

  private Collection<Patient> patientsWithPhenotypeValue(String each, Stream<Patient> patients) {
    return patients.filter(patient -> patient.hasCategoricPhenotypeValue(this, each)).collect(Collectors.toList());
  }
}