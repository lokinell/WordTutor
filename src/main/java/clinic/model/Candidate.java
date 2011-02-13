package clinic.model;

import clinic.model.Herb;

public class Candidate {
	private boolean selected = false;
	private Herb herb;

	public Candidate(Herb herb) {
		super();
		this.herb = herb;
	}

	public boolean isSelected() {
		return this.selected;
	}
	
	public void setSelected(boolean disabled) {
		this.selected = disabled;
	}

	public void setHerb(Herb herb) {
		this.herb = herb;
	}

	public Herb getHerb() {
		return this.herb;
	}
}