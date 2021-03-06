package clinic.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * This class is designed to wrap a {@link Drug} object so that its data
 * can be displayed and edited by JSF components.
 * @author Yunpeng
 */
public class DrugFace {
	private Drug drug;
	
	public DrugFace(Drug drug) {
		super();
		this.drug = drug;
	}
	
	public boolean isToEdit() {
		return drug.getDose() == 0F;
	}

	public boolean isToSelect() {
		return drug.getDose() != 0F;
	}
	
	public List<String> suggestDoses(Object filter){
		List<String> doses = new ArrayList<String>();
		Statistic statistic = drug.getHerb().getStatistic();	
		boolean used = false;
		DecimalFormat format = new DecimalFormat();
		if (statistic != null) {
			List<Frequence> frequences = statistic.getFrequencies();
			for (Frequence frequence: frequences) {
				String label = format.format(frequence.getDose());
				doses.add(label);
				if(drug.getDose()==frequence.getDose())
					used = true;
			}
		}
		
		// Add the dose of the current drug if it is new:
		if (!used && drug.getDose()!=0F) {
			String label = format.format(drug.getDose());
			doses.add(label);
		}

		Collections.sort(doses);
		return doses;
	}

	public List<SelectItem> getDoses() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		Statistic statistic = drug.getHerb().getStatistic();	
		boolean used = false;
		DecimalFormat format = new DecimalFormat();
		if (statistic != null) {
			List<Frequence> frequences = statistic.getFrequencies();
			for (Frequence frequence: frequences) {
				String label = format.format(frequence.getDose());
				SelectItem item = new SelectItem(label, label);
				items.add(item);
				if(drug.getDose()==frequence.getDose())
					used = true;
			}
		}
		
		// Add the dose of the current drug if it is new:
		if (!used && drug.getDose()!=0F) {
			String label = format.format(drug.getDose());
			SelectItem item = new SelectItem(label, label);
			items.add(item);
		}

		Collections.sort(items, new Comparator<SelectItem>(){
			@Override
			public int compare(SelectItem o1, SelectItem o2) {
				return ((String)o1.getValue()).compareTo((String)o2.getValue()); 
			}		
		});
		
		return items;
	}
	
	public String getDose(){
		if (drug.getDose()==0f)
			return "";
		DecimalFormat format = new DecimalFormat();
		return format.format(drug.getDose());
	}
	
	public void setDose(String dose) {
		if(dose==null || dose.equals(""))
		    drug.setDose(0f);
		else
			try {
				drug.setDose(Float.parseFloat(dose));
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
	}
}