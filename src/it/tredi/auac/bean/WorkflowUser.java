package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkflowUser implements Serializable {
	private static final long serialVersionUID = -8761413747503440353L;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private List<WorkflowGroup> workflowGroups = new ArrayList<WorkflowGroup>();
	
	public WorkflowUser(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getFirstname() {
		return firstName;
	}

	public String getLastname() {
		return lastName;
	}

	public List<WorkflowGroup> getWorkflowGroups() {
		return workflowGroups;
	}

	public void addWorkflowGroup(WorkflowGroup workflowGroupBean) {
		getWorkflowGroups().add(workflowGroupBean);
	}

	public void addWorkflowGroups(List<WorkflowGroup> workflowGroupBeans) {
		for(WorkflowGroup wfgroup : workflowGroupBeans)
			getWorkflowGroups().add(wfgroup);
	}
}
