package clinic.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQuery(name="Drugs.byPrescriptionID", query="select m from Drug as m where m.prescription.id=:prescriptionID")
public class Drug extends AbstractEntity {
	private static final long serialVersionUID = -5160654970742215520L;
	
	@ManyToOne
	@JoinColumn(name="presentation")
	private Prescription prescription;

	@ManyToOne
	@JoinColumn(name="herb")
	private Herb herb;

	@Column
	private int doses = 0;

	public Drug() {
		super();
	}
	
	public Drug(Prescription prescription, Herb herb, int doses) {
		super();
		this.herb = herb;
		this.prescription = prescription;
		this.doses = doses;
	}

	public Herb getHerb() {
		return herb;
	}

	public void setHerb(Herb herb) {
		this.herb = herb;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public int getDoses() {
		return doses;
	}

	public void setDoses(int doses) {
		this.doses = doses;
	}

	public Integer getDosesInteger() {
		return new Integer (doses);
	}

	public void setDosesInteger(Integer doses) {
		this.doses = doses.intValue();
	}
	
	public String getDosesString() {
		return Integer.toString(doses);
	}

	public void setDosesString(String doses) {
		if(doses==null)
			throw new IllegalArgumentException("The doses argument is null!");
		try {
			this.doses = Integer.parseInt(doses);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this)
			return true;
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final Drug other = (Drug) obj;
		return this.getId()==other.getId();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.prescription != null ? this.prescription.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Drug[herb: "+ herb.getName() +"]";
	}

}