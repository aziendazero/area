package it.tredi.auac.bean;

import it.tredi.workflow.bonita6.CookieBonita;

import java.io.Serializable;
import java.util.List;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.session.APISession;

public class WorkflowBean implements Serializable {
	private static final long serialVersionUID = -8761413747503440353L;
	
	private WorkflowUser workflowUser;
	private APISession apiSession;
	private CookieBonita restCookie;
	private CookieBonita adminRestCookie;
	List<ProcessDeploymentInfo> processDeploymentInfos = null;
	
	public WorkflowBean(WorkflowUser workflowUser, APISession apiSession, CookieBonita restCookie, CookieBonita adminRestCookie) {
		this.workflowUser = workflowUser;
		this.apiSession = apiSession;
		this.restCookie = restCookie;
		this.adminRestCookie = adminRestCookie;
	}

	public WorkflowUser getWorkflowUserBean() {
		return workflowUser;
	}

	public APISession getApiSession() {
		return apiSession;
	}

	public void setApiSession(APISession apiSession) {
		this.apiSession = apiSession;
	}

	public CookieBonita getRestCookie() {
		return restCookie;
	}

	public void setRestCookie(CookieBonita restCookie) {
		this.restCookie = restCookie;
	}
	
	public CookieBonita getAdminRestCookie() {
		return adminRestCookie;
	}

	public void setAdminRestCookie(CookieBonita adminRestCookie) {
		this.adminRestCookie = adminRestCookie;
	}

	public List<ProcessDeploymentInfo> getProcessDeploymentInfos() {
		return processDeploymentInfos;
	}

	public void setProcessDeploymentInfos(List<ProcessDeploymentInfo> processDeploymentInfos) {
		this.processDeploymentInfos = processDeploymentInfos;
	}

}
