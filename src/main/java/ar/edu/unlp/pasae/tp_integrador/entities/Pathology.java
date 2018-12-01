package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class Pathology {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String name;
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<NumericPhenotype> numericPhenotypes = new HashSet<NumericPhenotype>();
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<CategoricPhenotype> categoricPhenotypes = new HashSet<CategoricPhenotype>();

	public static final class PathologyBuilder {
		private String name;
		private Set<NumericPhenotype> numericPhenotypes = new HashSet<NumericPhenotype>();
		private Set<CategoricPhenotype> categoricPhenotypes = new HashSet<CategoricPhenotype>();

		private PathologyBuilder() {
		}

		public PathologyBuilder addNumericPhenotypes(final Set<NumericPhenotype> numericPhenotypes) {
			this.numericPhenotypes = numericPhenotypes;
			return this;
		}

		public PathologyBuilder addCategoricPhenotypes(final Set<CategoricPhenotype> categoricPhenotypes) {
			this.categoricPhenotypes = categoricPhenotypes;
			return this;
		}

		public PathologyBuilder addName(final String name) {
			this.name = name;
			return this;
		}

		public Pathology createPathology() {
			final Pathology pathology = new Pathology(this.name);
			pathology.setNumericPhenotypes(this.numericPhenotypes);
			pathology.setCategoricPhenotypes(this.categoricPhenotypes);

			return pathology;
		}
	}

	public static final PathologyBuilder builder() {
		return new PathologyBuilder();
	}

	public Pathology(Long id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}

	public Pathology(String name) {
		super();
		this.setName(name);
	}

	public Pathology() {
		super();
	}

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

	/**
	 * Adds the phenotype to the collection
	 *
	 * @param phenotype the Phenotype to add
	 *
	 * @return this
	 */
	public Pathology addPhenotype(NumericPhenotype phenotype) {
		this.getNumericPhenotypes().add(phenotype);

		return this;
	}

	/**
	 * Adds the phenotype to the collection
	 *
	 * @param phenotype the Phenotype to add
	 *
	 * @return this
	 */
	public Pathology addPhenotype(CategoricPhenotype phenotype) {
		this.getCategoricPhenotypes().add(phenotype);

		return this;
	}

	/**
	 * @return the categoricPhenotypes
	 */
	public Set<CategoricPhenotype> getCategoricPhenotypes() {
		return categoricPhenotypes;
	}

	/**
	 * @param categoricPhenotypes the categoricPhenotypes to set
	 */
	public void setCategoricPhenotypes(Set<CategoricPhenotype> categoricPhenotypes) {
		this.categoricPhenotypes.clear();
		this.categoricPhenotypes.addAll(categoricPhenotypes);
	}

	/**
	 * @return the numericPhenotypes
	 */
	public Set<NumericPhenotype> getNumericPhenotypes() {
		return numericPhenotypes;
	}

	/**
	 * @param numericPhenotypes the numericPhenotypes to set
	 */
	public void setNumericPhenotypes(Set<NumericPhenotype> numericPhenotypes) {
		this.numericPhenotypes.clear();
		this.numericPhenotypes.addAll(numericPhenotypes);
	}
}