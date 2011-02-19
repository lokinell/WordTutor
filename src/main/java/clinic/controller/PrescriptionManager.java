package clinic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import clinic.model.Drug;
import clinic.model.DrugFace;
import clinic.model.Herb;
import clinic.model.Prescription;

@ManagedBean(name = "prescriptionManager")
@SessionScoped
public class PrescriptionManager extends AbstractManager {

	@ManagedProperty(value = "#{herbManager}")
	private HerbManager herbManager;

	// The prescription being edited;
	private Prescription prescription = new Prescription();

	private int herbIndex;

	private Herb filterHerb;

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

	public List<Herb> suggest(Object filter) {
		List<Herb> filteds = new ArrayList<Herb>();
		String FILTER = ((String) filter).toUpperCase();
		if (FILTER.length() > 0) {
			for (Herb herb : herbManager.getHerbs()) {
				if (herb.getAlias().startsWith(FILTER) && !herb.isSelected())
					filteds.add(herb);
			}
		}
		return filteds;
	}

	public void unselectAll() {
		for (Herb herb : herbManager.getHerbs()) {
			herb.setSelected(false);
		}
	}

	public void onAddDrugFromFilter() {
		if (this.filterHerb != null && !this.filterHerb.isSelected()) {
			this.filterHerb.setSelected(true);
			Drug drug = new Drug(this.filterHerb);
			prescription.addDrug(drug);

			// add herb button client id
			StringBuilder builder = new StringBuilder("herbGrid");
			int idx = herbManager.getHerbs().indexOf(this.filterHerb);
			builder.append(idx / 400 + 1);// tab id
			builder.append(":");
			builder.append(idx);
			builder.append(":herbBtn");
			removeButtons.add(builder.toString());
		}
	}

	public void onAddDrug() {
		Herb herb = herbManager.getHerbs().get(herbIndex);
		if (!herb.isSelected()) {
			herb.setSelected(true);
			Drug drug = new Drug(herb);
			prescription.addDrug(drug);

			// Specify the row to update
			updates.clear();
			updates.add(new Integer(herbIndex));
		}
	}

	private List<String> removeButtons = new ArrayList<String>();

	public void onAddDrugActionListener(ActionEvent event) {
		String herbButtonId = event.getComponent().getClientId();
		removeButtons.add(herbButtonId);
	}

	public List<String> getHerbButtonId() {
		return removeButtons;
	}

	public void onRemoveDrug() {
		Drug drug = prescription.removeDrug(herbIndex);
		drug.getHerb().setSelected(false);
		removeButtons.remove(herbIndex);
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
		List<Drug>	drugs = prescription.getDrugs();
		for (Drug drug : drugs) {
			drug.getHerb().setSelected(false);
		}
		
		drugs.clear();
		removeButtons.clear();
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

	public void setFilterHerb(Herb filterHerb) {
		this.filterHerb = filterHerb;
	}

	public Herb getFilterHerb() {
		return filterHerb;
	}
}
