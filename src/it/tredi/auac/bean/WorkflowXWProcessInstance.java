package it.tredi.auac.bean;

import java.io.Serializable;

public class WorkflowXWProcessInstance implements Serializable {
	private static final long serialVersionUID = -7344853741726143251L;

	private long processInstanceId = 0;
	private long processDefinitionId = 0;
	private String processDefinitionName;
	private String processDefinitionVersion;
	private String operatore;
	private String ruolo;
	private String data;
	private String ora;
	
	public WorkflowXWProcessInstance(long processInstanceId, long processDefinitionId) {
		this.processInstanceId = processInstanceId;
		this.processDefinitionId = processDefinitionId;
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

}
