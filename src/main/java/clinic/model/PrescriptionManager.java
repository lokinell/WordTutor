package clinic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	public PrescriptionManager() {
		super();
	}

	@ManagedProperty(value="#{herbManager}")
	private HerbManager herbManager;

	public HerbManager getHerbManager() {
		return herbManager;
	}

	public void setHerbManager(HerbManager herbManager) {
		this.herbManager = herbManager;
	}

	// The prescription being edited;
	private Prescription prescription = new Prescription();
	
	private List<Candidate> candidates;
	
	
	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public List<Candidate> getCandidates() {
		if (candidates==null) {
			List<Herb> herbs = getHerbManager().getHerbs();
			candidates = new ArrayList<Candidate>();
			for (Herb herb : herbs) {
				Candidate candidate = new Candidate(herb);
				candidates.add(candidate);
			}
		}
		return candidates;
	}
	
	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public Candidate findCandidate(Herb herb) {
		if (candidates!=null) {
			for (Candidate candidate : candidates) {
				if (candidate.getHerb()==herb)
					return candidate;

				if (candidate.getHerb().equals(herb))
					return candidate;
			}
		}
		return null;
	}

	public void unselectAll() {
		if (candidates==null) {
			candidates = new ArrayList<Candidate>();
			for (Candidate candidate : candidates) {
				candidate.setSelected(false);				
			}
		}		
	}

	public void onAddDrug() {
		@SuppressWarnings("rawtypes")
		Map requestParameterMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(requestParameterMap.get("index")
				.toString());
		Candidate candidate = candidates.get(index);
		candidate.setSelected(true);
		Drug drug = new Drug(prescription, candidate.getHerb(), 10);
		prescription.addDrug(drug);
	}
	
	public void onRemoveDrug() {
		@SuppressWarnings("rawtypes")
		Map requestParameterMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(requestParameterMap.get("index")
				.toString());
		Drug drug = prescription.removeDrug(index);
		Candidate candidate = findCandidate(drug.getHerb());
		candidate.setSelected(false);		
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
