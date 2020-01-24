package it.tredi.auac.bean;

import it.tredi.workflow.bonita6.WorkflowProcessInstanceInfo;

import java.io.Serializable;

public class WorkflowProcessInstance implements Serializable {
	private static final long serialVersionUID = -7344853741726143251L;

	private long processInstanceId = 0;
	private long processDefinitionId = 0;
	private String processDefinitionName;
	private String processDefinitionVersion;
	private Integer processMajorVersion;
	private Integer processMinorVersion;
	private String operatore;
	private String ruolo;
	private String data;
	private String ora;
	private WorkflowProcessInstanceInfo workflowProcessInstanceInfo;
	private String overviewFormUrl;
	private String imageWorkflowUrl;
	
	public WorkflowProcessInstance(WorkflowXWProcessInstance wxwpi) {
		this.processInstanceId = wxwpi.getProcessInstanceId();
		this.processDefinitionId = wxwpi.getProcessDefinitionId();
		this.processDefinitionName = wxwpi.getProcessDefinitionName();
		this.processDefinitionVersion = wxwpi.getProcessDefinitionVersion();
		this.operatore = wxwpi.getOperatore();
		this.ruolo = wxwpi.getRuolo();
		this.data = wxwpi.getData();
		this.ora = wxwpi.getOra();
		majorMinorVersionSetting();
	}
	
	private void majorMinorVersionSetting() {
		if(this.processDefinitionVersion != null) {
			String[] versions = this.processDefinitionVersion.split("\\.");
			if(versions.length == 2) {
				//casi possibili
				//1.2_test_02
				//1.2
				//3.0
				this.processMajorVersion = Integer.valueOf(versions[0]);
				String[] minorVersions = versions[1].split("_");
				this.processMinorVersion = Integer.valueOf(minorVersions[0]);				
			} else if (versions.length == 3) {
				//casi possibili
				//3.0.test_02
				this.processMajorVersion = Integer.valueOf(versions[0]);
				this.processMinorVersion = Integer.valueOf(versions[1]);				
			} else {
				this.processMajorVersion = 0;
				this.processMinorVersion = 0;
			}
		} else {
			this.processMajorVersion = 0;
			this.processMinorVersion = 0;
		}
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public long getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(long processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Integer getProcessMajorVersion() {
		return processMajorVersion;
	}

	public void setProcessMajorVersion(Integer processMajorVersion) {
		this.processMajorVersion = processMajorVersion;
	}

	public Integer getProcessMinorVersion() {
		return processMinorVersion;
	}

	public void setProcessMinorVersion(Integer processMinorVersion) {
		this.processMinorVersion = processMinorVersion;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(String processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}
	
	@Override
	public String toString() {
		return this.processDefinitionName + " [" + this.processDefinitionVersion + "]";
	}

	public WorkflowProcessInstanceInfo getWorkflowProcessInstanceInfo() {
		return workflowProcessInstanceInfo;
	}

	public void setWorkflowProcessInstanceInfo(
			WorkflowProcessInstanceInfo workflowProcessInstanceInfo) {
		this.workflowProcessInstanceInfo = workflowProcessInstanceInfo;
	}
	
	public String getOverviewFormUrl() {
		return overviewFormUrl;
	}

	public void setOverviewFormUrl(String overviewFormUrl) {
		this.overviewFormUrl = overviewFormUrl;
	}

	public String getImageWorkflowUrl() {
		return imageWorkflowUrl;
	}

	public void setImageWorkflowUrl(String imageWorkflowUrl) {
		this.imageWorkflowUrl = imageWorkflowUrl;
	}

}
