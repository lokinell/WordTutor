package clinic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import org.richfaces.model.selection.SimpleSelection;

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

	// The index of the selected herb 
	private int herbIndex;

	// The selected herb
	private Herb filterHerb;
	
	// The filter entered by users
	private String filter = "";

	// The list of the buttons to be updated
	private Set<Integer> updates = new HashSet<Integer>();
	
	private SimpleSelection selection = new SimpleSelection();

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
	
	public void onAddSelectedHerb() {

		if (this.filterHerb != null && !this.filterHerb.isSelected()) {
			this.filterHerb.setSelected(true);
			Drug drug = new Drug(this.filterHerb);
			prescription.addDrug(drug);
			
			// Specify the row to update
			int index = herbManager.getHerbs().indexOf(this.filterHerb);
			updates.clear();
			updates.add(new Integer(index));
			
			this.filterHerb = null;
			this.filter = null;
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
	
	public String createPrescription(){
		return "prescribe";
	}
	
	public List<Prescription> getRecipesByPatient() throws ManagerException {
		return null;
	}
	
	
	public int getPrescriptDrugsIndex(){
		return prescription.getDrugs().size()-1;
	}

	
	public void onRemoveDrug() {
		Drug drug = prescription.removeDrug(herbIndex);
		drug.getHerb().setSelected(false);
		// removeButtons.remove(herbIndex);
		// Specify the row to update
		int index = herbManager.getHerbs().indexOf(drug.getHerb());
		updates.clear();
		updates.add(new Integer(index));

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
	}

	public String onSave() {
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
			return null;
		}
		
		
		List<Drug> drugs = prescription.getDrugs();
		for (Drug drug : drugs) {
			herbManager.doStatistic(drug.getHerb());
		}
		
		return "viewRecipe";
		
	}

	public void onReturn() {

	}

	public void setFilterHerb(Herb filterHerb) {
		this.filterHerb = filterHerb;
	}

	public Herb getFilterHerb() {
		return filterHerb;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}

	public void setSelection(SimpleSelection selection) {
		this.selection = selection;
	}

	public SimpleSelection getSelection() {
		return selection;
	}
	
	@SuppressWarnings("rawtypes")
	public String takeSelection() {
		Iterator iter = getSelection().getKeys();
		Object key = iter.next();
		int index = Integer.parseInt(key.toString());
		/*selectedRecipe = recipesOfSelectedPatient.get(index);
		try {
			List<MedicineInfo> mInfos = doInTransaction(new PersistenceAction<List<MedicineInfo>>() {

				@SuppressWarnings("unchecked")
				@Override
				public List<MedicineInfo> execute(EntityManager em) {
					Query q = em.createNamedQuery("MedicineInfo.byRecipeId");
					q.setParameter("recipeID", selectedRecipe.getId());
					return q.getResultList();
				}
			});
			
			setCurrentMedicineInfos(mInfos);
		} catch (ManagerException e) {
			e.printStackTrace();
		}*/
		
		return null;
	}
}
