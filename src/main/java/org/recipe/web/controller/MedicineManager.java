package org.recipe.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.medicine.recipe.model.Medicine;

@ManagedBean(name = "medicineBean")
@SessionScoped
public class MedicineManager extends AbstractManager {
	private List<Medicine> medicines;
	private List<MedicineItem> medicineItems;

	@PostConstruct
	public void construct() {
		init();
	}

	private void init() {
		getMedicines();
	}

	@PreDestroy
	public void destroy() {
		setMedicines(null);
	}

	public List<MedicineItem> getMedicineItems() {
		if (medicines != null && medicineItems==null) {
			medicineItems = new ArrayList<MedicineItem>();
			for (Medicine medicine : medicines) {
				MedicineItem item = new MedicineItem();
				item.setMedicine(medicine);
				medicineItems.add(item);
			}
		}

		return medicineItems;
	}

	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}

	public List<Medicine> getMedicines() {
		try {
			medicines = doInTransaction(new PersistenceAction<List<Medicine>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Medicine> execute(EntityManager em) {
					Query q = em.createNamedQuery("Medicine.All");
					return q.getResultList();
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}

		return medicines;
	}
}
