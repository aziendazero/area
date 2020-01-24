package it.tredi.auac.business;

import it.tredi.auac.TipoGenerazioneValutazioneVerificaSrEnum;
import it.tredi.auac.orm.entity.RequisitoInst;

import java.util.HashMap;
import java.util.Map;

public class UpdaterRequisitiInstValutazioneVerificaInfo {
	//public static final String SALUTE_MENTALE_YES = "Y";
	private UpdaterTypeEnum updaterTypeEnum = UpdaterTypeEnum.VALUTAZIONE;
	private String domandaClientid;
	private Map<String, RequisitoInst> classificazioneUdoTemplClientids = new HashMap<String, RequisitoInst>();
	
	public UpdaterRequisitiInstValutazioneVerificaInfo(UpdaterTypeEnum updaterTypeEnum, String domandaClientid) {
		this.updaterTypeEnum = updaterTypeEnum;
		this.domandaClientid = domandaClientid;
	}
	
	public void addRequisito(RequisitoInst requisitoInst) {
		//AUAC_UPDATE_SR_REQ_INST_VAL(DOMANDACLIENTID CHAR, IDREQTEMPLFKCLID CHAR, IDTIPORISPCLID CHAR, TIPOGENSR CHAR, ERRORSTATE OUT NUMBER) AS
		if(
				(updaterTypeEnum == UpdaterTypeEnum.VALUTAZIONE && requisitoInst.getGeneraValutazioneSr() != TipoGenerazioneValutazioneVerificaSrEnum.NESSUNAGGIORNAMENTO)
				||
				(updaterTypeEnum == UpdaterTypeEnum.VALUTAZIONEVERIFICA && requisitoInst.getGeneraValutazioneVerificatoreSr() != TipoGenerazioneValutazioneVerificaSrEnum.NESSUNAGGIORNAMENTO)
		) {
			if(!classificazioneUdoTemplClientids.containsKey(requisitoInst.getClientid())) {
				classificazioneUdoTemplClientids.put(requisitoInst.getClientid(), requisitoInst);
			}
		}
	}

	public Map<String, RequisitoInst> getClassificazioneUdoTemplClientids() {
		return classificazioneUdoTemplClientids;
	}

	public UpdaterTypeEnum getUpdaterTypeEnum() {
		return updaterTypeEnum;
	}

	public String getDomandaClientid() {
		return domandaClientid;
	}
}
