package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class NumericPhenotype extends Phenotype {
  public static final class NumericPhenotypeBuilder {
		private String name;

		private NumericPhenotypeBuilder() {
		}

		public NumericPhenotypeBuilder addName(final String name) {
			this.name = name;
			return this;
		}

		public NumericPhenotype createPhenotype() {
      final NumericPhenotype phenotype = new NumericPhenotype(this.name);

			return phenotype;
		}
	}

	public static final NumericPhenotypeBuilder builder() {
		return new NumericPhenotypeBuilder();
	}

  public NumericPhenotype(Long id, String name) {
    super(id, name);
  }

  public NumericPhenotype(String name) {
    super(name);
  }

  protected NumericPhenotype() {
    super();
  }

  @Override
  public Boolean validate(String value) {
    try {
      Float.parseFloat(value);
      return true;
    } catch (Exception NumberFormatException) {
      return false;
    }
  }

  @Override
  public String getKind() {
    return "Numeric";
  }

  @Override
  public Collection<AnalysisGroup> getAnalysisGroups(Analysis analysis) {
    Collection<AnalysisGroup> groups = new ArrayList<>();
    Collection<Patient> higher = new ArrayList<>();
    Collection<Patient> lower = new ArrayList<>();
    Long cutoffValue = analysis.getCutoffValue();

    for (Patient patient : analysis.getPatients()) {
      if (patient.numericPhenotypeValue(this) >= cutoffValue) {
        higher.add(patient);
      } else {
        lower.add(patient);
      }
    }

    groups.add(new AnalysisGroup(">=", higher));
    groups.add(new AnalysisGroup("<", lower));

    return groups;
  }
}