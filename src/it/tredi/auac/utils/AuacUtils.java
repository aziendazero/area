package it.tredi.auac.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.TipoUdoUtenteTempl;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UtenteModel;

public final class AuacUtils {
	private AuacUtils() { // private constructor
        
    }
	
	/**
	 * 
	 * @param bonitaUsername
	 * @return AuacUserInfo ricavato dal bonitaUsername, null se il formato del bonitaUsername non è riconosciuto
	 */
	public final static AuacUserInfo getAuacUserInfo(String bonitaUsername) {
		AuacUserInfo ui = null;
		int pos = bonitaUsername.indexOf("_");
		if(pos != -1) {
			String ruoloB = bonitaUsername.substring(0, pos);
			//System.out.println("ruoloB: " + ruoloB);
			if("regione".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("REGIONE");
				ui.setUsername(bonitaUsername.substring(pos + 1));
			} else if("optit".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("OPERATORE_TITOLARE");
				pos = bonitaUsername.indexOf("_", pos + 1);
				ui.setUsername(bonitaUsername.substring(pos + 1));				
			} else if("optitlt".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("OPERATORE_TITOLARE_IN_LETTURA");
				pos = bonitaUsername.indexOf("_", pos + 1);
				ui.setUsername(bonitaUsername.substring(pos + 1));				
			} else if("verif".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("VERIFICATORE");
				ui.setUsername(bonitaUsername.substring(pos + 1));
			} else if("tit".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("TITOLARE");
				pos = bonitaUsername.indexOf("_", pos + 1);
				ui.setUsername(bonitaUsername.substring(pos + 1));				
			} else if("admin".equals(ruoloB)) {
				ui = new AuacUserInfo();
				ui.setBonitausername(bonitaUsername);
				ui.setRuolo("AMMINISTRATORE");
				ui.setUsername(bonitaUsername.substring(pos + 1));				
			}
		}
		return ui;
	}
	
	public final static boolean requisitoIsSoglia(RequisitoInst req) {
		if (req.getRequisitoTempl().getTipoRisposta() != null && req.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia")) {
			return true;
		}
		return false;
	}

	public final static boolean requisitoIsSiNo(RequisitoInst req) {
		if (req.getRequisitoTempl().getTipoRisposta() != null && req.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No")) {
			return true;
		}
		return false;
	}
	
	public final static boolean requisitoIsTitolo(RequisitoInst req) {
		if (req.getRequisitoTempl().getTipoRisposta() != null && req.getRequisitoTempl().getTipoRisposta().getNome().equalsIgnoreCase("titolo")) {
			return true;
		}
		return false;
	}

	public final static BigDecimal convertValutazioneVerificaToBigDecimal(String valutazione) throws ParseException {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		//String pattern = "#,##0.0#";
		String pattern = "0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		return (BigDecimal) decimalFormat.parse(valutazione.trim());
	}
	
	public final static String getArea(RequisitoInst requisito) {
		String toRet = null;
		Matcher matcher = Pattern.compile("\\d+").matcher(requisito.getRequisitoTempl().getNome());
		if(matcher.find()) {
			toRet = matcher.group();
			if(toRet.length() > 1) {
				if(toRet.charAt(0) == '0') {
					//Se sono almeno 2 caratteri e il primo è uno 0 lo elimino
					toRet = toRet.substring(1);
				}
			}
		}
		return toRet;
	}
	
	public final static boolean checkRegionUserCanEditEsito(UdoUoInstForList udoUoInstForList, UtenteModel currentUtenteModel, List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente, List<UdoUoInstForList> udoUoInstForL) {
		if(udoUoInstForList.isUdo()) {
			return checkRegionUserCanEditEsito(udoUoInstForList.getUdoInst(), currentUtenteModel, tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente);
		} else if (udoUoInstForList.isUo()) {
			//devo controllare se l'utente corrente puo' modificare l'esito di una qualsiasi delle udo figlie della uo corrente
			for(UdoUoInstForList uuifl : udoUoInstForL) {
				if(uuifl.isUdo() && uuifl.getUoClientId().equals(udoUoInstForList.getUoClientId()) && checkRegionUserCanEditEsito(uuifl.getUdoInst(), currentUtenteModel, tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente))
					return true;
			}
		}
		return false;
	}
	
	public final static boolean checkRegionUserCanEditEsito(UdoInst udoInst, UtenteModel currentUtenteModel, List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente) {
		if(tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente != null) {
			for(TipoUdoUtenteTempl tipoUdoUtenteTempl : tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente) {
				if(currentUtenteModel.getClientid().equals(tipoUdoUtenteTempl.getUtenteModel().getClientid()) && udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClientid().equals(tipoUdoUtenteTempl.getTipoUdo22Templ().getClientid()))
					return true;
			}
		}
		return false;
	}
}
