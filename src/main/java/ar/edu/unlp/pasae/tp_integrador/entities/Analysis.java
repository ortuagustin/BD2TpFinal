package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
@SuppressWarnings("unused")
public class Analysis {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date date;
	private AnalysisState state;
	@NotEmpty
	private String description;
	@NotEmpty
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Patient> patients = new HashSet<Patient>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Snp> snps = new ArrayList<Snp>();
	@ManyToOne
	private Phenotype phenotype;
	private Long cutoffValue;

	public static final class AnalysisBuilder {
		private Date date;
		private AnalysisState state;
		private Pathology pathology;
		private String description;
		private Collection<Patient> patients = new HashSet<Patient>();
		private Phenotype phenotype;
		private Long cutoffValue;

		private AnalysisBuilder() {}

		public AnalysisBuilder addPhenotype(final Phenotype phenotype) {
			this.phenotype = phenotype;
			return this;
		}

		public AnalysisBuilder addCutoffValue(final Long cutoffValue) {
			this.cutoffValue = cutoffValue;
			return this;
		}

		public AnalysisBuilder addPathology(final Pathology pathology) {
			this.pathology = pathology;
			return this;
		}

		public AnalysisBuilder addPatients(final Set<Patient> patients) {
			this.patients = patients;
			return this;
		}

		public AnalysisBuilder addDate(final Date date) {
			this.date = date;
			return this;
		}

		public AnalysisBuilder addState(final AnalysisState state) {
			this.state = state;
			return this;
		}

		public Analysis createAnalysis() {
			final Analysis analysis = new Analysis(this.date, this.state, this.description, this.patients, this.phenotype, this.cutoffValue);

			return analysis;
		}

		public AnalysisBuilder addDescription(String description) {
			this.description = description;
			return this;
		}
	}

	public static final AnalysisBuilder builder() {
		return new AnalysisBuilder();
	}

	public Analysis(Long id, Date date, AnalysisState state, String description, Set<Patient> patients,
			Pathology pathology, Phenotype phenotype, Long cutoffValue) {
		this(date, state, description, patients, phenotype, cutoffValue);
		this.setId(id);
	}

	public Analysis(Date date, AnalysisState state, String description, Collection<Patient> patients, Phenotype phenotype,
			Long cutoffValue) {
		super();
		this.setDate(date);
		this.setState(state);
		this.setPatients(patients);
		this.setDescription(description);
		this.setPhenotype(phenotype);
		this.setCutoffValue(cutoffValue);
	}

	private Analysis() {
		super();
		this.setState(AnalysisState.PENDING);
	}

	public Collection<AnalysisGroup> getAnalysisGroups() {
		Collection<AnalysisGroup> groups = this.phenotype.getAnalysisGroups(this);

		return groups;
	}

	public Optional<AnalysisGroup> getAnalysisGroup(String phenotypeValue) {
		return this.getAnalysisGroups().stream().filter(each -> each.getPhenotype().equals(phenotypeValue)).findFirst();
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the state
	 */
	public AnalysisState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(AnalysisState state) {
		this.state = state;
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
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the snps
	 */
	public List<Snp> getSnps() {
		return this.snps;
	}

	/**
	 * @param snps the snps to set
	 */
	public void setSnps(Collection<Snp> snps) {
		this.snps.clear();
		this.snps.addAll(snps);
	}

	/**
	 * @return the patients
	 */
	public Set<Patient> getPatients() {
		return this.patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(Collection<Patient> patients) {
		this.patients.clear();
		this.patients.addAll(patients);
	}

	/**
	 * @return the Phenotype
	 */
	public Phenotype getPhenotype() {
		return this.phenotype;
	}

	/**
	 * @param phenotype the phenotype to set
	 */
	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}

	/**
	 * @return the cutoffValue
	 */
	public Long getCutoffValue() {
		return cutoffValue;
	}

	/**
	 * @param cutoffValue the cutoffValue to set
	 */
	public void setCutoffValue(Long cutoffValue) {
		this.cutoffValue = cutoffValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}