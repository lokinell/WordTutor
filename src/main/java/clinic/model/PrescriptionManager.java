package clinic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.recipe.web.controller.AbstractManager;
import org.recipe.web.controller.ManagerException;

import clinic.model.Prescription;

@ManagedBean(name = "prescriptionManager")
@ApplicationScoped
public class PrescriptionManager extends AbstractManager {
	
	@ManagedProperty(value="#{herbManager}")
	private HerbManager herbManager;

	// The prescription being edited;
	private Prescription prescription = new Prescription();	

	private int herbIndex;
	
	private Set<Integer> updates = new HashSet<Integer>();
	
	public PrescriptionManager() {
		super();
	}

	public HerbManager getHerbManager() {
		return herbManager;
	}

	public int getHerbIndex() {
		return herbIndex;
	}

	public void setHerbIndex(int herbIndex) {
		this.herbIndex = herbIndex;
	}

	public void setHerbManager(HerbManager herbManager) {
		this.herbManager = herbManager;
	}
	
	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	
	public Set<Integer> getUpdates() {
		return updates;
	}

	public void setUpdates(Set<Integer> updates) {
		this.updates = updates;
	}

	public void unselectAll() {
		for (Herb herb: herbManager.getHerbs()) {
			herb.setSelected(false);				
		}
	}

	public void onAddDrug() {
		Herb herb = herbManager.getHerbs().get(herbIndex);
		herb.setSelected(true);
		Drug drug = new Drug(herb);
		prescription.addDrug(drug);
		
		// Specify the row to update
		updates.clear();
		updates.add(new Integer(herbIndex));
	}
	
	public void onRemoveDrug() {
		Drug drug = prescription.removeDrug(herbIndex);
		drug.getHerb().setSelected(false);		

		// Specify the row to update
		// updates.clear();
		// updates.add(new Integer(herbIndex));
	}

	public List<DrugFace> getDrugs() {
		List<DrugFace> wrappers = new ArrayList<DrugFace>();
		for (Drug drug : prescription.getDrugs()) {
			DrugFace wrapper = new DrugFace(drug);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
	public void onClear() {
		
	}
	
	public void onSave() {
		try {
			doInTransaction(new PersistenceActionWithoutResult() {
				@Override
				public void execute(EntityManager em) {
					prescription.setCreation(new Date());
					em.persist(prescription);
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}		
	}
		
	public void onReturn() {
		
	}
}
