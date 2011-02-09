package com.medicine.recipe.model;

import com.medicine.recipe.model.AbstractEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="Medicine.All", query="select m from Medicine as m order by m.alias")
public class Medicine extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 8480075138561497108L;

	@Column(name = "NAME")
	private String name;

	@Column(name = "NAME_ALIAS")
	private String alias;

	@Column(name = "QUANTITY")
	private int quantity;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "UNIT_PRICE")
	private BigDecimal unitPrice;

	@Temporal(TemporalType.DATE)
	@Column(name = "PRODUCT_DATE")
	private Date productDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "BUY_DATE")
	private Date buyDate;

	@Column(name = "SHELF_LIFE")
	//单位是"月"
	private int shelfLife;

	@OneToOne(mappedBy = "medicine", cascade = { javax.persistence.CascadeType.PERSIST })
	private MedicineInfo medicineInfo;

	public Date getBuyDate() {
		return this.buyDate;
	}

	public String getName() {
		return this.name;
	}

	public Date getProductDate() {
		return this.productDate;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public int getShelfLife() {
		return this.shelfLife;
	}

	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	public void setQuantity(int count) {
		this.quantity = count;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setMedicineInfo(MedicineInfo medicineInfo) {
		this.medicineInfo = medicineInfo;
	}

	public MedicineInfo getMedicineInfo() {
		return this.medicineInfo;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Medicine other = (Medicine) obj;
		if ((this.name == null) ? (other.name != null) : !this.name
				.equals(other.name)) {
			return false;
		}
		
	    
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Medicine[name=" + name + ",productDate=" + productDate + "]";
	}

}