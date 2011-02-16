package com.medicine.recipe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MEDICINE_INFO")
@NamedQuery(name="MedicineInfo.byRecipeId", query="select m from MedicineInfo as m where m.recipe.id=:recipeID")
public class MedicineInfo implements Serializable {
	private static final long serialVersionUID = 431022563890432559L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int ID;

	@OneToOne(cascade = { javax.persistence.CascadeType.PERSIST })
	@JoinColumn(name = "MEDCINE_ID")
	private Medicine medicine;

	@ManyToOne(cascade = { javax.persistence.CascadeType.PERSIST })
	@JoinColumn(name = "RECIPE_ID")
	private Recipe recipe;

	@Column(name = "MEDICINE_QUANTITY")
	private int quantity = 5;

	@Column(name = "MEDICINE_PRICE")
	private double price;

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public Medicine getMedicine() {
		return this.medicine;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Recipe getRecipe() {
		return this.recipe;
	}

	public void setID(int iD) {
		this.ID = iD;
	}

	public int getID() {
		return this.ID;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}
}