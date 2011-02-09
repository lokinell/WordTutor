package com.medicine.recipe.model;

import com.medicine.recipe.model.AbstractEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="RecipeByPatient", query="select r from Recipe as r where r.patient.id = :patientId")
public class Recipe extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 8709158807539257669L;

	@ManyToOne(cascade = { javax.persistence.CascadeType.PERSIST })
	@JoinColumn(name = "PATIENTID")
	private Patient patient;
	private int dose;

	@Temporal(TemporalType.TIME)
	private Date createDate;
	private String department;
	private String disease;
	private String feeType;
	private BigDecimal price;
	//使用方法
	private String method;

	@OneToMany(targetEntity = MedicineInfo.class, mappedBy = "recipe", cascade = { javax.persistence.CascadeType.PERSIST })
	private Collection<MedicineInfo> medicineInfos;

	public int getDose() {
		return this.dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDisease() {
		return this.disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getFeeType() {
		return this.feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setMedicineInfos(Collection<MedicineInfo> medicineInfos) {
		this.medicineInfos = medicineInfos;
	}

	public Collection<MedicineInfo> getMedicineInfos() {
		return this.medicineInfos;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Patient getPatient() {
		return this.patient;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final Recipe other = (Recipe) obj;
		return this.getId()==other.getId();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.createDate != null ? this.createDate.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Recipe[patient name=" + patient.getName()+"disease:" + disease + "create date"+ createDate.toString() +"]";
	}
}