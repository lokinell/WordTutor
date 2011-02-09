package org.recipe.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.richfaces.model.selection.SimpleSelection;

import com.medicine.recipe.model.Medicine;
import com.medicine.recipe.model.MedicineInfo;
import com.medicine.recipe.model.Patient;
import com.medicine.recipe.model.Recipe;

@ManagedBean(name = "recipeBean")
@SessionScoped
public class RecipeManager extends AbstractManager {

	private Patient selectedPatient;
	private SimpleSelection selection = new SimpleSelection();
	private List<MedicineItem> recipeMedicines = new ArrayList<MedicineItem>();
	private List<MedicineInfo> currentMedicineInfos = new ArrayList<MedicineInfo>();
	private List<Recipe> recipesOfSelectedPatient = new ArrayList<Recipe>();
	private Recipe selectedRecipe = new Recipe();
	private Medicine currentMedicine;
	private MedicineManager medicineBean;
	private int doses = 5;

	public String newRecipe() {
		System.out.println("new recipe...");
		return "editRecipe";
	}

	public List<Recipe> getRecipesByPatient() throws ManagerException {
		this.recipeMedicines.clear();
		recipesOfSelectedPatient.clear();
		if (this.selectedPatient != null) {
			recipesOfSelectedPatient = doInTransaction(new PersistenceAction<List<Recipe>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Recipe> execute(EntityManager em) {
					Query q = em.createNamedQuery("RecipeByPatient");
					q.setParameter("patientId", selectedPatient.getId());
					return q.getResultList();
				}
			});
		} 
		
		return recipesOfSelectedPatient;
	}

	public String removeMedicineAction() {
		@SuppressWarnings("rawtypes")
		Map requestParameterMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(requestParameterMap.get("index")
				.toString());
		((MedicineItem) this.recipeMedicines.get(index)).setDisabled(false);
		this.recipeMedicines.remove(index);
		return null;
	}

	public String addMedicineAction() {
		if (this.medicineBean == null) {
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			this.medicineBean = ((MedicineManager) externalContext
					.getSessionMap().get("medicineBean"));
		}
		List<MedicineItem> medicineItems = this.medicineBean.getMedicineItems();
		@SuppressWarnings("rawtypes")
		Map requestParameterMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(requestParameterMap.get("index")
				.toString());
		MedicineItem item = (MedicineItem) medicineItems.get(index);
		item.setDisabled(true);
		this.recipeMedicines.add(item);
		return null;
	}

	public String clearAction() {
		this.recipeMedicines.clear();
		if (this.medicineBean != null) {
			List<MedicineItem> medicineItems = this.medicineBean
					.getMedicineItems();
			for (MedicineItem item : medicineItems) {
				item.setDisabled(false);
			}
		}

		return null;
	}

	public String saveRecipeAction() {
		try {
			doInTransaction(new PersistenceActionWithoutResult() {

				private Collection<MedicineInfo> medicinesOfRecipe = new ArrayList<MedicineInfo>();

				@Override
				public void execute(EntityManager em) {
					Recipe recipe = new Recipe();
					recipe.setCreateDate(new Date());
					recipe.setPatient(em.find(Patient.class,
							getSelectedPatient().getId()));

					for (MedicineItem item : getRecipeMedicines()) {
						MedicineInfo mInfo = new MedicineInfo();
						mInfo.setMedicine(em.find(Medicine.class, item
								.getMedicine().getId()));
						mInfo.setQuantity(item.getQuantity());
						mInfo.setPrice(item.getMedicine().getUnitPrice()
								.doubleValue()
								* item.getQuantity());
						mInfo.setRecipe(recipe);
						this.medicinesOfRecipe.add(mInfo);
					}

					recipe.setMedicineInfos(this.medicinesOfRecipe);
					recipe.setDose(getDoses());
					em.persist(recipe);
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}

		this.doses = 5;
		clearAction();
		return "viewRecipe";
	}

	public String backAction() {
		return "viewRecipe";
	}

	public void setSelectedPatient(Patient selectedPatient) {
		this.selectedPatient = selectedPatient;
	}

	public Patient getSelectedPatient() {
		return selectedPatient;
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
		selectedRecipe = recipesOfSelectedPatient.get(index);
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
		}
		
		return null;
	}
	
	

	public void setRecipeMedicines(List<MedicineItem> recipeMedicines) {
		this.recipeMedicines = recipeMedicines;
	}

	public List<MedicineItem> getRecipeMedicines() {
		return recipeMedicines;
	}

	public void setCurrentMedicine(Medicine currentMedicine) {
		this.currentMedicine = currentMedicine;
	}

	public Medicine getCurrentMedicine() {
		return currentMedicine;
	}

	public void setDoses(int doses) {
		this.doses = doses;
	}

	public int getDoses() {
		return doses;
	}

	public void setCurrentMedicineInfos(List<MedicineInfo> currentMedicineInfos) {
		this.currentMedicineInfos = currentMedicineInfos;
	}

	public List<MedicineInfo> getCurrentMedicineInfos() {
		return currentMedicineInfos;
	}

	public void setSelectedRecipe(Recipe selectedRecipe) {
		this.selectedRecipe = selectedRecipe;
	}

	public Recipe getSelectedRecipe() {
		return selectedRecipe;
	}
}
