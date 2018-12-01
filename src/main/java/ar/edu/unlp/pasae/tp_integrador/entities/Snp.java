package ar.edu.unlp.pasae.tp_integrador.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document
public class Snp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String snp;
	private Double statistical;
	private Double pvalue;

	public Snp(String snp, Double statistical, Double pvalue) {
		super();
		this.setSnp(snp);
		this.setStatistical(statistical);
		this.setPvalue(pvalue);
	}

	public Snp(Long id, String snp, Double statistical, Double pvalue) {
		this(snp, statistical, pvalue);
		this.setId(id);
	}

	public Snp() {
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
	 * @return the pvalue
	 */
	public Double getPvalue() {
		return pvalue;
	}

	/**
	 * @param pvalue the pvalue to set
	 */
	public void setPvalue(Double pvalue) {
		this.pvalue = pvalue;
	}

	/**
	 * @return the statistical
	 */
	public Double getStatistical() {
		return statistical;
	}

	/**
	 * @param statistical the statistical to set
	 */
	public void setStatistical(Double statistical) {
		this.statistical = statistical;
	}

	/**
	 * @return the snp
	 */
	public String getSnp() {
		return snp;
	}

	/**
	 * @param snp the snp to set
	 */
	public void setSnp(String snp) {
		this.snp = snp;
	}
}