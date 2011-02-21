package clinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries ({
	@NamedQuery(name="Drugs.byPrescription", query="select m from Drug as m where m.prescription = :prescription"),
	@NamedQuery(name="Drugs.byHerb", query="select m from Drug as m where m.herb = :herb")
})
public class Drug extends AbstractEntity {
	private static final long serialVersionUID = -5160654970742215520L;
	
	@ManyToOne
	@JoinColumn(name="presentation")
	private Prescription prescription;

	@ManyToOne
	@JoinColumn(name="herb")
	private Herb herb;

	@Column(name="dose")
	private float dose = 0F;

	public Drug() {
		super();
	}
	
	public Drug(Herb herb, float dose) {
		super();
		this.herb = herb;
		this.dose = dose;
	}

	public Drug(Herb herb) {
		this(herb, herb.getMostUsedDose());
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

	public float getDose() {
		return dose;
	}

	public void setDose(float dose) {
		this.dose = dose;
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
		return "Drug[" + herb.getName() + ", " + dose + "]";
	}

}