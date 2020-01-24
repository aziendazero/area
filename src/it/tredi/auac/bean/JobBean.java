package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobBean implements Serializable {
	private static final long serialVersionUID = 3885598592505926493L;
	protected int total;
	protected int current;
	private String type;
	private List<String> msgs = new ArrayList<String>();
	
	public JobBean(String type, int total) {
		this.type = type;
		this.total = total;
		current = 0;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	
	public boolean isCompleted() {
		return current >= total;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void incrementCurrent() {
		current++;
	}

	public List<String> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}
	
}
