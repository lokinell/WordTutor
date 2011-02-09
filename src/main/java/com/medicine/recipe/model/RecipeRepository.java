package com.medicine.recipe.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-1-9 Time: 17:30:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class RecipeRepository implements Serializable {
	private static final long serialVersionUID = -7815983043457583514L;
	@Id
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String recipeType;

	public String getRecipeType() {
		return recipeType;
	}

	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}

	private String recipeName;

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
}
