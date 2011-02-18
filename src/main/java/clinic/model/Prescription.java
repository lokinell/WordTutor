package clinic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A {@link Prescription} consists of a number of {@link Herb}s called {@link Drug}s.
 * @author Yunpeng
 */
@Entity
public class Prescription extends AbstractEntity {	
	private static final long serialVersionUID = 5601455389966295993L;

	@OneToMany(mappedBy = "prescription", cascade = { javax.persistence.CascadeType.PERSIST })
	private List<Drug> drugs = new ArrayList<Drug>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date creation;
    
	public List<Drug> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<Drug> drugs) {
		this.drugs = drugs;
	}
	
	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public void addDrug(Drug drug) {
		drug.setPrescription(this);
		drugs.add(drug);
	}
	
	public Drug removeDrug(int index) {
		Drug drug = drugs.remove(index);
		if (drug!=null)
			drug.setPrescription(null);
		return drug;
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
		
		final Prescription other = (Prescription) obj;
		return this.getId()==other.getId();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.creation != null ? this.creation.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		String date = creation != null ? creation.toString() : "unknown";
		return "Prescription[create date: " + date +"]";
	}

}
