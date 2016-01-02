package it.vbigiani.imageresizer;

public enum GameType {
	BG2("Baldur's Gate 2", new int[][] {{38, 60}, {110, 170}, {210, 330}}),
	BG2_EE("Baldur's Gate 2 - Enhanced Edition", new int[][] {{54, 84}, {169, 266}, {210, 330}}),
	MANUAL("Set Manually");
	
	private GameType(String description, int[][] values) {
		this.description = description;
		editable  = false;
		this.values = values;
	}

	private GameType(String description) {
		this.description = description;
		editable = true;
	}
	
	private String description;

	private boolean editable;
	
	private int[][] values;

	public String getDescription() {
		return description;
	}

	public boolean isEditable() {
		return editable;
	}
	
	public int[][] getValues() {
		return values;
	}

	@Override
	public String toString() {
		return description;
	}
}
