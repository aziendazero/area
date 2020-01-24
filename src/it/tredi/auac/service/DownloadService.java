package it.tredi.auac.service;


import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.context.ExternalContext;

import it.tredi.auac.bean.CompareUdoModelBean;
import it.tredi.auac.bean.CompareUdoModelPageBean;
import it.tredi.auac.bean.LoginPageBean;
import it.tredi.auac.dao.UdoInstDao;
import it.tredi.auac.dao.UdoModelDao;
import it.tredi.auac.dao.UtenteDao;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UtenteModel;

@Service
public class DownloadService {
	private static final Logger log = Logger.getLogger(DownloadService.class);

	@Autowired
	UtenteDao utenteDao;

	@Autowired
	private UdoModelDao udoModelDao;
	
	@Autowired
	private UdoInstDao udoInstDao;
	
	public DownloadService() {
		//do nothing
		log.info("DownloadService constructor");
	}
	
	public LoginPageBean checkCas(MessageContext messageContext, ExternalContext context, String redirectUrl, String clientid, String defaultRedirectUrl) throws Exception {
		LoginPageBean loginPageBean = new LoginPageBean();
		loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_NOUSERONDB);
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getNativeRequest();
		String remoteUser = httpServletRequest.getRemoteUser();
		
		UtenteModel utenteModel = null;		
		utenteModel = utenteDao.checkUserByUsernameCas(remoteUser); //check login e password su RDBMS

		if (utenteModel == null) { //utente non presente su db
			loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_NOUSERONDB);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("login").defaultText("Utente non autorizzato ad accedere all'applicativo").build());
			return loginPageBean;
		}
		loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_LOGGED);
		return loginPageBean;
	}

		
	public CompareUdoModelPageBean getUdoModelCompare(String clientId) {
		
		UdoModel udoModel = udoModelDao.findByClientId(clientId);
		List<UdoInst> udoInstL = udoInstDao.getUdoInstByUdoModelClientId(clientId);
		UdoInst udoInst = null;
		
		if(udoInstL.size() > 0)
			udoInst = udoInstL.get(udoInstL.size() - 1);

		CompareUdoModelPageBean compareUdoModelPageBean = new CompareUdoModelPageBean();

		if(udoInst != null) {
			CompareUdoModelBean compareUdoModelBean = new CompareUdoModelBean(udoInst);
			compareUdoModelBean.setUdoModel(udoModel);
			compareUdoModelPageBean.getCompareUdoBeanL().add(compareUdoModelBean);
		}
    	
		return compareUdoModelPageBean;
	}
	
}
