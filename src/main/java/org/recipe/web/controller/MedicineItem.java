package org.recipe.web.controller;

import com.medicine.recipe.model.Medicine;

public class MedicineItem {
	private boolean disabled = false;
	private Medicine medicine;
	private int quantity = 10;

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public Medicine getMedicine() {
		return this.medicine;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}
}