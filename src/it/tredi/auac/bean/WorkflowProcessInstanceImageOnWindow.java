package it.tredi.auac.bean;
import java.io.Serializable;

public class WorkflowProcessInstanceImageOnWindow implements Serializable {
	private static final long serialVersionUID = 6951313753422029433L;

	//private long processInstanceId = 0;
	private String imageWorkflowUrl;
	private boolean state = false;
	
	/*
	public WorkflowProcessInstanceImageOnWindow(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	*/

	public String getImageWorkflowUrl() {
		return imageWorkflowUrl;
	}

	public void setImageWorkflowUrl(String imageWorkflowUrl) {
		this.imageWorkflowUrl = imageWorkflowUrl;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
