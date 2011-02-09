package com.medicine.recipe.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="USER")
@NamedQueries({
	@NamedQuery(name="User.byNameAndPassword", query="select u from User as u where u.name = :name and u.password = :password"),
	@NamedQuery(name="User.byName", query="select u from User as u where u.name = :name")
})
public class User extends AbstractEntity {

	private static final long serialVersionUID = 5129619473562307960L;
	
	private String name;
	private String password;
	@Enumerated(EnumType.ORDINAL)
	private Role role;
	
	public User(){};

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		
		return this.getId()==other.getId();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "User[name=" + name + "password="+password+ ",role=" + role + "]";
	}
	
}
