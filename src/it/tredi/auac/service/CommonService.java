package it.tredi.auac.service;


import it.tredi.auac.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.FlowExecutionException;

@Service
public class CommonService {
	private static final Logger log = Logger.getLogger(CommonService.class);
	
	@Value("${abilitaPerformanceMonitor}")
	private String abilitaPerformanceMonitor;

	@Value("${casLogoutUrl}")
	private String casLogoutUrl;

	public CommonService() {
		//do nothing
	}
	
	public String printExceptionMessage(FlowExecutionException flowExecutionException) {
		log.error("Si e' verificato un errore (printExceptionMessage).", flowExecutionException);
		Throwable bE = findBusinessException(flowExecutionException);
		return bE == null? flowExecutionException.getMessage() : bE.getMessage();
	}

	public String printExceptionStackTrace(FlowExecutionException flowExecutionException) {
        ByteArrayOutputStream printStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(printStream);
        flowExecutionException.printStackTrace(ps);
        return new String(printStream.toByteArray()); 
	}	
	
	
    private Throwable findBusinessException(FlowExecutionException flowExecutionException) {
		Throwable cause = flowExecutionException.getCause();
		while (cause != null) {
		    if (cause instanceof BusinessException) {
		    	return (BusinessException) cause;
		    }
		    if (cause.getCause() == null)
		    	return cause;
		    cause = cause.getCause();
		}
		return null;
    }

	public String getAbilitaPerformanceMonitor() {
		return abilitaPerformanceMonitor;
	}

	public void setAbilitaPerformanceMonitor(String abilitaPerformanceMonitor) {
		this.abilitaPerformanceMonitor = abilitaPerformanceMonitor;
	}
    
	public boolean abilitaPerformanceMonitor() {
		return "S".equals(abilitaPerformanceMonitor);
	}

	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}
}
