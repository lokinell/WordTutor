package clinic.model;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.recipe.web.controller.AbstractManager;
import org.recipe.web.controller.ManagerException;

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

		return herbs;
	}

	public void setHerbs(List<Herb> herbs) {
		this.herbs = herbs;
	}
	
}
