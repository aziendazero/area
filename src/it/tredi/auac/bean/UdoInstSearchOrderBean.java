package it.tredi.auac.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import it.tredi.auac.UdoUoInstForListOrderTypeEnum;

public class UdoInstSearchOrderBean implements Serializable {
	private static final long serialVersionUID = -6195415746226487393L;
	private static final Logger log = Logger.getLogger(UdoInstSearchOrderBean.class);

	//campi per Ricerca udo
	private String subStringDenominazioneDaCercare;
	private String subStringTipoUdoDaCercare;
	private String subStringBrancaDaCercare;
	private String subStringDisciplinaDaCercare;
	private String areaDisciplineDaCercare;
	private String classificazioneUdoTemplDaCercare;
	private String subStringSedeOperativaDaCercare;
	private String subStringUODaCercare;
	private String subStringCodiceUnivocoDaCercare;
	private String subStringCodiceUlssDaCercare;
	private String subStringCodiceExUlssDaCercare;
	private String subStringDirettoreDaCercare;	
	private String subStringStabilimento;
	private String subStringBlocco;
	private String subStringPiano;
	private String subStringProgressivo;
	private boolean showOnlyUdoAssigned;
	private boolean showOnlyUdoAssignedVerifica;
	private boolean showOnlyCongruenzaSenzaEsito;
	private String dataScadenzaStringDa;
	private String dataScadenzaStringA;
	private boolean showOnlyUdoConRequisiti;
	
	//Contiene il modo in cui e' ordinata la lista delle UdoUoInstForList
	private UdoUoInstForListOrderTypeEnum udoUoInstForListOrderType = UdoUoInstForListOrderTypeEnum.Gerarchico;

	public String getSubStringDenominazioneDaCercare() {
		return subStringDenominazioneDaCercare;
	}

	public void setSubStringDenominazioneDaCercare(
			String subStringDenominazioneDaCercare) {
		this.subStringDenominazioneDaCercare = subStringDenominazioneDaCercare;
	}

	public String getSubStringTipoUdoDaCercare() {
		return subStringTipoUdoDaCercare;
	}

	public void setSubStringTipoUdoDaCercare(String subStringTipoUdoDaCercare) {
		this.subStringTipoUdoDaCercare = subStringTipoUdoDaCercare;
	}

	public String getSubStringBrancaDaCercare() {
		return subStringBrancaDaCercare;
	}

	public void setSubStringBrancaDaCercare(String subStringBrancaDaCercare) {
		this.subStringBrancaDaCercare = subStringBrancaDaCercare;
	}

	public String getSubStringDisciplinaDaCercare() {
		return subStringDisciplinaDaCercare;
	}

	public void setSubStringDisciplinaDaCercare(String subStringDisciplinaDaCercare) {
		this.subStringDisciplinaDaCercare = subStringDisciplinaDaCercare;
	}

	public String getSubStringSedeOperativaDaCercare() {
		return subStringSedeOperativaDaCercare;
	}

	public void setSubStringSedeOperativaDaCercare(
			String subStringSedeOperativaDaCercare) {
		this.subStringSedeOperativaDaCercare = subStringSedeOperativaDaCercare;
	}

	public String getSubStringUODaCercare() {
		return subStringUODaCercare;
	}

	public void setSubStringUODaCercare(String subStringUODaCercare) {
		this.subStringUODaCercare = subStringUODaCercare;
	}

	public String getSubStringCodiceUnivocoDaCercare() {
		return subStringCodiceUnivocoDaCercare;
	}

	public void setSubStringCodiceUnivocoDaCercare(
			String subStringCodiceUnivocoDaCercare) {
		this.subStringCodiceUnivocoDaCercare = subStringCodiceUnivocoDaCercare;
	}

	public String getSubStringCodiceUlssDaCercare() {
		return subStringCodiceUlssDaCercare;
	}

	public void setSubStringCodiceUlssDaCercare(String subStringCodiceUlssDaCercare) {
		this.subStringCodiceUlssDaCercare = subStringCodiceUlssDaCercare;
	}

	public String getSubStringCodiceExUlssDaCercare() {
		return subStringCodiceExUlssDaCercare;
	}

	public void setSubStringCodiceExUlssDaCercare(String subStringCodiceExUlssDaCercare) {
		this.subStringCodiceExUlssDaCercare = subStringCodiceExUlssDaCercare;
	}

	public String getSubStringDirettoreDaCercare() {
		return subStringDirettoreDaCercare;
	}

	public void setSubStringDirettoreDaCercare(String subStringDirettoreDaCercare) {
		this.subStringDirettoreDaCercare = subStringDirettoreDaCercare;
	}

	public String getSubStringStabilimento() {
		return subStringStabilimento;
	}

	public void setSubStringStabilimento(String subStringStabilimento) {
		this.subStringStabilimento = subStringStabilimento;
	}

	public String getSubStringBlocco() {
		return subStringBlocco;
	}

	public void setSubStringBlocco(String subStringBlocco) {
		this.subStringBlocco = subStringBlocco;
	}

	public String getSubStringPiano() {
		return subStringPiano;
	}

	public void setSubStringPiano(String subStringPiano) {
		this.subStringPiano = subStringPiano;
	}

	public String getSubStringProgressivo() {
		return subStringProgressivo;
	}

	public void setSubStringProgressivo(String subStringProgressivo) {
		this.subStringProgressivo = subStringProgressivo;
	}

	public boolean isShowOnlyUdoAssigned() {
		return showOnlyUdoAssigned;
	}

	public void setShowOnlyUdoAssigned(boolean showOnlyUdoAssigned) {
		this.showOnlyUdoAssigned = showOnlyUdoAssigned;
	}

	public boolean isShowOnlyUdoAssignedVerifica() {
		return showOnlyUdoAssignedVerifica;
	}

	public void setShowOnlyUdoAssignedVerifica(boolean showOnlyUdoAssignedVerifica) {
		this.showOnlyUdoAssignedVerifica = showOnlyUdoAssignedVerifica;
	}

	public boolean isShowOnlyCongruenzaSenzaEsito() {
		return showOnlyCongruenzaSenzaEsito;
	}

	public void setShowOnlyCongruenzaSenzaEsito(boolean showOnlyCongruenzaSenzaEsito) {
		this.showOnlyCongruenzaSenzaEsito = showOnlyCongruenzaSenzaEsito;
	}

	public UdoUoInstForListOrderTypeEnum getUdoUoInstForListOrderType() {
		return udoUoInstForListOrderType;
	}

	public void setUdoUoInstForListOrderType(
			UdoUoInstForListOrderTypeEnum udoUoInstForListOrderType) {
		this.udoUoInstForListOrderType = udoUoInstForListOrderType;
	}

	public String getDataScadenzaStringDa() {
		return dataScadenzaStringDa;
	}

	public void setDataScadenzaStringDa(String dataScadenzaStringDa) {
		this.dataScadenzaStringDa = dataScadenzaStringDa;
	}

	public String getDataScadenzaStringA() {
		return dataScadenzaStringA;
	}

	public void setDataScadenzaStringA(String dataScadenzaStringA) {
		this.dataScadenzaStringA = dataScadenzaStringA;
	}

	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
	public Date getDataScadenzaDa() {
		if(dataScadenzaStringDa != null && !dataScadenzaStringDa.isEmpty()) {
			try {
				return df.parse(dataScadenzaStringDa);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Impossibile convertire il campo dataScadenzaStringDa in data", e);
			}
		}
		return null;
	}

	public Date getDataScadenzaA() {
		if(dataScadenzaStringA != null && !dataScadenzaStringA.isEmpty()) {
			try {
				return df.parse(dataScadenzaStringA);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Impossibile convertire il campo dataScadenzaStringA in data", e);
			}
		}
		return null;
	}

	public String getAreaDisciplineDaCercare() {
		return areaDisciplineDaCercare;
	}

	public void setAreaDisciplineDaCercare(String areaDisciplineDaCercare) {
		this.areaDisciplineDaCercare = areaDisciplineDaCercare;
	}

	public String getClassificazioneUdoTemplDaCercare() {
		return classificazioneUdoTemplDaCercare;
	}

	public void setClassificazioneUdoTemplDaCercare(String classificazioneUdoTemplDaCercare) {
		this.classificazioneUdoTemplDaCercare = classificazioneUdoTemplDaCercare;
	}

	public boolean isShowOnlyUdoConRequisiti() {
		return showOnlyUdoConRequisiti;
	}

	public void setShowOnlyUdoConRequisiti(boolean showOnlyUdoConRequisiti) {
		this.showOnlyUdoConRequisiti = showOnlyUdoConRequisiti;
	}
}
