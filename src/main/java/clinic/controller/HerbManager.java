package clinic.controller;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import clinic.model.Drug;
import clinic.model.Herb;
import clinic.model.Statistic;

@ManagedBean(name = "herbManager")
@ApplicationScoped
public class HerbManager extends AbstractManager {
	private List<Herb> herbs;
	
	public HerbManager() {
		super();
	}

	public List<Herb> getHerbs() {
		if (herbs!=null)
			return herbs;
		
		try {
			herbs = doInTransaction(new PersistenceAction<List<Herb>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Herb> execute(EntityManager em) {
					Query q = em.createNamedQuery("Herb.All");
					return q.getResultList();
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		
		// Do statistics
		for(Herb herb : herbs)
			doStatistic(herb);

		return herbs;
	}

	public void setHerbs(List<Herb> herbs) {
		this.herbs = herbs;
	}
	
	private class FindDrugs implements PersistenceAction<List<Drug>> {
		private Herb herb;
		public FindDrugs(Herb herb) {
			super();
			this.herb = herb;
		}
		@SuppressWarnings("unchecked")
		@Override
		public List<Drug> execute(EntityManager em) {
			Query q = em.createNamedQuery("Drugs.byHerb");
			q.setParameter("herb", herb);
			return q.getResultList();
		}
	}
	
	public void doStatistic(Herb herb) {
		List<Drug> drugs = null;
		try {
			drugs = doInTransaction(new FindDrugs(herb));
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		if (drugs==null || drugs.isEmpty())
			return;
		
		Statistic statistic = new Statistic();
		for (Drug drug : drugs) {
			statistic.count(drug.getDose());
		}
		herb.setStatistic(statistic);
	}
	
}
