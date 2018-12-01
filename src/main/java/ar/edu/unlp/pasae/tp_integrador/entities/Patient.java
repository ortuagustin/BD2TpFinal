package ar.edu.unlp.pasae.tp_integrador.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String surname;
	@NotEmpty
	@Pattern(regexp = "\\d{7,8}")
	private String dni;
	@Email
	private String email;
	@OneToOne
	private CustomUser user;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Genotype> genotypes = new ArrayList<Genotype>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<NumericPhenotypeValue> numericPhenotypes = new HashSet<NumericPhenotypeValue>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CategoricPhenotypeValue> categoricPhenotypes = new HashSet<CategoricPhenotypeValue>();

	public static final class PatientBuilder {
		private String name;
		private String surname;
		private String dni;
		private String email;
		private CustomUser user;
		private Collection<Genotype> genotypes = new ArrayList<Genotype>();
		private Set<NumericPhenotypeValue> numericPhenotypes = new HashSet<NumericPhenotypeValue>();
		private Set<CategoricPhenotypeValue> categoricPhenotypes = new HashSet<CategoricPhenotypeValue>();

		private PatientBuilder() {
		}

		public PatientBuilder addNumericPhenotypes(final Set<NumericPhenotypeValue> numericPhenotypes) {
			this.numericPhenotypes = numericPhenotypes;
			return this;
		}

		public PatientBuilder addCategoricPhenotypes(final Set<CategoricPhenotypeValue> categoricPhenotypes) {
			this.categoricPhenotypes = categoricPhenotypes;
			return this;
		}

		public PatientBuilder addGenotypes(final Collection<Genotype> genotypes) {
			this.genotypes = genotypes;
			return this;
		}

		public PatientBuilder addName(final String name) {
			this.name = name;
			return this;
		}

		public PatientBuilder addUser(final CustomUser user) {
			this.user = user;
			return this;
		}

		public PatientBuilder addSurname(final String surname) {
			this.surname = surname;
			return this;
		}

		public PatientBuilder addDni(final String dni) {
			this.dni = dni;
			return this;
		}

		public PatientBuilder addEmail(final String email) {
			this.email = email;
			return this;
		}

		public Patient createPatient() {
			final Patient patient = new Patient(this.name, this.surname, this.dni, this.user);
			patient.setNumericPhenotypes(this.numericPhenotypes);
			patient.setCategoricPhenotypes(this.categoricPhenotypes);
			patient.setGenotypes(this.genotypes);
			patient.setEmail(this.email);

			return patient;
		}
	}

	public static final PatientBuilder builder() {
		return new PatientBuilder();
	}

	public Patient() {
		super();
	}

	public Patient(String name, String surname, String dni, CustomUser user) {
		super();
		this.setName(name);
		this.setSurname(surname);
		this.setDni(dni);
		this.setEmail(email);
		this.setUser(user);
	}

	/**
	 * Adds the genotype to the collection
	 *
	 * @param genotype the genotype to add
	 *
	 * @return this
	 */
	public Patient addGenotype(Genotype genotype) {
		this.getGenotypes().add(genotype);

		return this;
	}

	/**
	 * Adds the phenotype to the collection
	 *
	 * @param phenotype the Phenotype to add
	 *
	 * @return this
	 */
	public Patient addPhenotype(NumericPhenotypeValue phenotype) {
		phenotype.setPatient(this);
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
	public Patient addPhenotype(CategoricPhenotypeValue phenotype) {
		phenotype.setPatient(this);
		this.getCategoricPhenotypes().add(phenotype);

		return this;
	}

	/**
	 * @return the genotype
	 */
	public Collection<Genotype> getGenotypes() {
		return genotypes;
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
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @return the dni
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the user
	 */
	public CustomUser getUser() {
		return user;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param genotypes the genotype list to set
	 */
	public void setGenotypes(Collection<Genotype> genotypes) {
		this.genotypes.clear();
		this.genotypes.addAll(genotypes);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @param dni the dni to set
	 */
	public void setDni(String dni) {
		this.dni = dni;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(CustomUser user) {
		this.user = user;
	}

	/**
	 * @return the categoricPhenotypes
	 */
	public Set<CategoricPhenotypeValue> getCategoricPhenotypes() {
		return categoricPhenotypes;
	}

	/**
	 * @param categoricPhenotypes the categoricPhenotypes to set
	 */
	public void setCategoricPhenotypes(Set<CategoricPhenotypeValue> categoricPhenotypes) {
		for (CategoricPhenotypeValue each : categoricPhenotypes) {
			each.setPatient(this);
		}

		this.categoricPhenotypes = categoricPhenotypes;
	}

	/**
	 * @return the numericPhenotypes
	 */
	public Set<NumericPhenotypeValue> getNumericPhenotypes() {
		return numericPhenotypes;
	}

	/**
	 * @param numericPhenotypes the numericPhenotypes to set
	 */
	public void setNumericPhenotypes(Set<NumericPhenotypeValue> numericPhenotypes) {
		for (NumericPhenotypeValue each : numericPhenotypes) {
			each.setPatient(this);
		}

		this.numericPhenotypes = numericPhenotypes;
	}

	public Boolean hasCategoricPhenotypeValue(CategoricPhenotype categoricPhenotype, String value) {
		Optional<CategoricPhenotypeValue> phenotype = this.findCategoricPhenotypeValue(categoricPhenotype);

		if (phenotype.isPresent()) {
			return phenotype.get().getValue().equals(value);
		}

		return false;
	}

	private Optional<CategoricPhenotypeValue> findCategoricPhenotypeValue(CategoricPhenotype categoricPhenotype) {
		return this.categoricPhenotypes.stream().filter(each -> each.getPhenotype().equals(categoricPhenotype)).findFirst();
	}

	public Long numericPhenotypeValue(NumericPhenotype numericPhenotype) {
		NumericPhenotypeValue phenotypeValue = this.findNumericPhenotypeValue(numericPhenotype);

		return phenotypeValue.getValue();
	}

	private NumericPhenotypeValue findNumericPhenotypeValue(NumericPhenotype numericPhenotype) {
		return this.numericPhenotypes.stream().filter(each -> each.getPhenotype().equals(numericPhenotype)).findFirst().get();
	}
}