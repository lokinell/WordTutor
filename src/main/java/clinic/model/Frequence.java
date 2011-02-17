package clinic.model;

public class Frequence {
	private float dose;
	private int count = 0;
		
	public Frequence(float dose, int count) {
		super();
		this.dose = dose;
		this.count = count;
	}

	public float getDose() {
		return dose;
	}
	
	public void setDose(float dose) {
		this.dose = dose;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void increment() {
		count++;
	}
}
