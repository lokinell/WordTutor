package clinic.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.richfaces.model.selection.SimpleSelection;

import clinic.model.Patient;

@ManagedBean(name = "patientBean")
@SessionScoped
public class PatientManager extends AbstractManager {
	private Patient currentPatient;
	private SimpleSelection selection;
	private boolean disabled = true;
	private List<Patient> patientList;
	
	@ManagedProperty(name="prescriptionManager", value="#{prescriptionManager}")
	private PrescriptionManager prescriptionManager;

	@PostConstruct
	public void construct() {
		init();
	}

	private void init() {
		currentPatient = new Patient();
		setSelection(new SimpleSelection());
	}

	@PreDestroy
	public void destroy() {
		currentPatient = null;
		setSelection(null);
	}

	public void setCurrentPatient(Patient currentPatient) {
		this.currentPatient = currentPatient;
	}

	public Patient getCurrentPatient() {
		return currentPatient;
	}

	public void registerActionListener(ActionEvent event) {
		try {
			doInTransaction(new PersistenceAction<Patient>() {

				@Override
				public Patient execute(EntityManager em) {
					em.persist(currentPatient);
					return currentPatient;
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}
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
		if (iter.hasNext()) {
			setDisabled(false);
			Object key = iter.next();
			int index = Integer.parseInt(key.toString());
			Patient patient = getPatients().get(index);
			setCurrentPatient(patient);

			
			//prescriptionManager.setSelectedPatient(currentPatient);
			//recipeManager.getCurrentMedicineInfos().clear();
		}
		return null;
	}

	public List<Patient> getPatients() {
		try {
			patientList = doInTransaction(new PersistenceAction<List<Patient>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Patient> execute(EntityManager em) {
					Query q = em.createNamedQuery("Patient.all");
					return q.getResultList();
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
			patientList = new ArrayList<Patient>();
		}
		return patientList;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setPrescriptionManager(PrescriptionManager prescriptionManager) {
		this.prescriptionManager = prescriptionManager;
	}

	public PrescriptionManager getPrescriptionManager() {
		return prescriptionManager;
	}

}
