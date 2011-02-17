package org.recipe.web.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.medicine.recipe.model.Role;
import com.medicine.recipe.model.User;

@ManagedBean(name = "UserBean")
@SessionScoped
public class UserManager extends AbstractManager {

	private boolean createUserPanelVisible = false;
	private boolean loginPanelVisible = true;
	private String passwordAgain;

	private User currentUser;

	@PostConstruct
	public void construct() {
		init();
	}

	@PreDestroy
	public void destroy() {
		currentUser = null;
	}

	public void init() {
		currentUser = new User();
	}

	public String doLogin() {

		try {
			doInTransaction(new PersistenceActionWithoutResult() {

				@SuppressWarnings("unchecked")
				@Override
				public void execute(EntityManager em) {
					Query query = em.createNamedQuery("User.byNameAndPassword");
					query.setParameter("name", currentUser.getName());
					query.setParameter("password", currentUser.getPassword());

					List<User> result = query.getResultList();
					if (result != null && result.size() > 0) {
						currentUser = result.get(0);
					}
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
		}

		if (currentUser.getRole() == Role.ASSISTANT)
			return "viewRecipe";
		else if (currentUser.getRole() == Role.DOCTOR)
			return "editRecipe";
		else
			return "user";
	}

	public String createUser() {
		this.createUserPanelVisible = true;
		this.loginPanelVisible = false;
		return "user";
	}

	public void validatePassword(ActionEvent event) {
		System.out.println("validate password...");
		if (!this.passwordAgain.equals(currentUser.getPassword())) {
			addMessage("createPasswordAgain", "密�?两次输入�?一致",
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public String doCreateUser() {
		System.out.println("doCreateUser...");
		try {

			doInTransaction(new PersistenceActionWithoutResult() {

				@Override
				public void execute(EntityManager em) {
					em.persist(getCurrentUser());
				}
			});
		} catch (ManagerException e) {
			e.printStackTrace();
			return "user";
		}
		this.createUserPanelVisible = false;
		this.loginPanelVisible = true;
		if (getCurrentUser().getRole() == Role.ASSISTANT)
			return "viewRecipe";
		else if (getCurrentUser().getRole() == Role.DOCTOR)
			return "editRecipe";
		else
			return "user";
	}

	public void setLoginPanelVisible(boolean loginPanelVisible) {
		this.loginPanelVisible = loginPanelVisible;
	}

	public boolean isLoginPanelVisible() {
		return loginPanelVisible;
	}

	public void setCreateUserPanelVisible(boolean createUserPanelVisible) {
		this.createUserPanelVisible = createUserPanelVisible;
	}

	public boolean isCreateUserPanelVisible() {
		return createUserPanelVisible;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

	public String getPasswordAgain() {
		return passwordAgain;
	}
}
