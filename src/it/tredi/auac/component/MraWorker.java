package it.tredi.auac.component;

import it.tredi.auac.service.MraService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component("mraWorker")
public class MraWorker implements Worker {
 
	protected static Logger logger = Logger.getLogger(MraWorker.class);
 
	@Autowired
	MraService mraService;
 
	public void work() {
        try {
        	mraService.createOrUpdateMraModel();
        }
        catch (Exception e) {
			logger.error(e);
        }
	}
}