package it.tredi.auac.bean;

import java.io.Serializable;

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;

public class WorkflowExecuteTaskPageBean implements Serializable {
	private static final long serialVersionUID = 5681955584427095627L;
	private HumanTaskInstance humanTaskInstance;
	private String taskFormUrl;
	
	public HumanTaskInstance getHumanTaskInstance() {
		return humanTaskInstance;
	}
	public void setHumanTaskInstance(HumanTaskInstance humanTaskInstance) {
		this.humanTaskInstance = humanTaskInstance;
	}
	public String getTaskFormUrl() {
		return taskFormUrl;
	}
	public void setTaskFormUrl(String taskFormUrl) {
		this.taskFormUrl = taskFormUrl;
	}
}
