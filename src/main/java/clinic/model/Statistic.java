package clinic.model;

import java.util.ArrayList;
import java.util.List;

public class Statistic {
	private List<Frequence> frequencies = new ArrayList<Frequence>();

	public List<Frequence> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(List<Frequence> frequencies) {
		this.frequencies = frequencies;
	}

	public void count(float dose) {
		for (Frequence frequence: frequencies) {
			if (frequence.getDose() == dose) {
				frequence.increment();
				return;
			}		
		}
		frequencies.add(new Frequence(dose, 1));
	}
	
	public float getMostUsedDose() {
		float dose = 0f;
		float count = 0;
		for (Frequence frequence: frequencies) {
			if (frequence.getCount() > count) {
				dose = frequence.getDose();
				count = frequence.getCount();
			}		
		}
		return dose;
	}
	
	@Override
	public String toString() {
		String string = "{";
		for (Frequence frequence: frequencies) {
			string += "["+frequence.getDose()+","+frequence.getCount()+"]";
		}
		string+="}";
		return string;
	}
}
