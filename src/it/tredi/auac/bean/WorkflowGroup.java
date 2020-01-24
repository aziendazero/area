package it.tredi.auac.bean;

import java.io.Serializable;

public class WorkflowGroup implements Serializable {
	private static final long serialVersionUID = -6875917832066756929L;

	private String name;
	private String displayName;
	
	public WorkflowGroup(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
