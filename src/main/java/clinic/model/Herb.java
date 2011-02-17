package clinic.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="medicine") 
@NamedQuery(name="Herb.All", query="select m from Herb as m order by m.alias")
public class Herb implements Serializable {
	private static final long serialVersionUID = 953541820897907770L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
   	@Column(name = "id")
	private Long id;
	
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
	private int shelfLife;

	@Transient
	transient private Statistic statistic = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}
	
	/**
	 * 
	 * @return 0f means that this herb has never been used.
	 */
	public float getMostUsedDose() {
		if(statistic==null)
			return 0f;
		return statistic.getMostUsedDose();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Herb other = (Herb) obj;
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