package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.UdoInst;

import java.io.Serializable;
import java.math.BigDecimal;

public class CompareUdoBean implements Serializable {
	private static final long serialVersionUID = -5611111001371452471L;
	private UdoInst udoInst;
	private UdoInst oldUdoInst = null;
	
	public CompareUdoBean(UdoInst udoInst) {
		this.udoInst = udoInst;
	}
	
	public UdoInst getUdoInst() {
		return udoInst;
	}
	public void setUdoInst(UdoInst udoInst) {
		this.udoInst = udoInst;
	}
	public UdoInst getOldUdoInst() {
		return oldUdoInst;
	}
	public void setOldUdoInst(UdoInst oldUdoInst) {
		this.oldUdoInst = oldUdoInst;
	}
	
	public boolean isChange() {
		if(oldUdoInst == null)
			return false;
		return isDescrChange() || isDenominazioneSedeChange() || isCodiceUlssTerritorialeChange() || isCodiceStruttDenominChange()
				|| isDenominazioneUoChange() || isStabilimentoChange() || isBloccoChange() || isPianoChange() || isProgressivoChange()
				|| isDirettoreSanitarioChange() || isDisciplineChange() || isBrancheChange() || isFattoriProduttiviChange();
	}

	public boolean isDescrChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getDescr(), oldUdoInst.getDescr());
		}
		return false;
	}
	
	public boolean isDenominazioneSedeChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getDenominazioneSede(), oldUdoInst.getDenominazioneSede());
		}
		return false;
	}

	public boolean isCodiceUlssTerritorialeChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getCodiceUlssTerritoriale(), oldUdoInst.getCodiceUlssTerritoriale());
		}
		return false;
	}

	public boolean isCodiceStruttDenominChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getCodiceStruttDenomin(), oldUdoInst.getCodiceStruttDenomin());
		}
		return false;
	}

	public boolean isDenominazioneUoChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getDenominazioneUo(), oldUdoInst.getDenominazioneUo());
		}
		return false;
	}

	public boolean isStabilimentoChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getStabilimento(), oldUdoInst.getStabilimento());
		}
		return false;
	}

	public boolean isBloccoChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getBlocco(), oldUdoInst.getBlocco());
		}
		return false;
	}

	public boolean isPianoChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getPiano(), oldUdoInst.getPiano());
		}
		return false;
	}
	
	public boolean isProgressivoChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getProgressivo(), oldUdoInst.getProgressivo());
		}
		return false;
	}
	
	public boolean isDirettoreSanitarioChange() {
		if(oldUdoInst != null) {
			return isFieldsChanged(udoInst.getDirSanitarioCogn(), oldUdoInst.getDirSanitarioCogn()) 
					|| isFieldsChanged(udoInst.getDirSanitarioNome(), oldUdoInst.getDirSanitarioNome())
					|| isFieldsChanged(udoInst.getDirSanitarioCfisc(), oldUdoInst.getDirSanitarioCfisc());
		}
		return false;
	}

	public boolean isDisciplineChange() {
		if(oldUdoInst != null) {
			if(udoInst.getDisciplineUdoInstsInfo() == null) {
				if(oldUdoInst.getDisciplineUdoInstsInfo() == null)
					return false;
				else
					return true;
			} else {
				if(oldUdoInst.getDisciplineUdoInstsInfo() == null)
					return true;
			}
			if(udoInst.getDisciplineUdoInstsInfo().size() != oldUdoInst.getDisciplineUdoInstsInfo().size())
				return true;
			//Ora devo confrontare tutti i valori
			boolean found = false;
			for(DisciplinaUdoInstInfo disc : udoInst.getDisciplineUdoInstsInfo()) {
				found = false;
				for(DisciplinaUdoInstInfo oldDisc : oldUdoInst.getDisciplineUdoInstsInfo()) {
					if(isFieldsEquals(disc.getCodice(), oldDisc.getCodice())) {
						//Se hanno lo stesso codice effettuo il confronto
						if(isFieldsChanged(disc.getDescr(), oldDisc.getDescr()) || isFieldsChanged(disc.getPostiLetto(), oldDisc.getPostiLetto()) 
									|| isFieldsChanged(disc.getPostiLettoExtra(), oldDisc.getPostiLettoExtra()) || isFieldsChanged(disc.getPostiLettoObi(), oldDisc.getPostiLettoObi()))
							return true;
						found = true;
					}
				}
				if(!found) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isBrancheChange() {
		if(oldUdoInst != null) {
			if(udoInst.getBrancheUdoInstsInfo() == null) {
				if(oldUdoInst.getBrancheUdoInstsInfo() == null)
					return false;
				else
					return true;
			} else {
				if(oldUdoInst.getBrancheUdoInstsInfo() == null)
					return true;
			}
			if(udoInst.getBrancheUdoInstsInfo().size() != oldUdoInst.getBrancheUdoInstsInfo().size())
				return true;
			//Ora devo confrontare tutti i valori
			boolean found = false;
			for(BrancaUdoInstInfo bran : udoInst.getBrancheUdoInstsInfo()) {
				found = false;
				for(BrancaUdoInstInfo oldBran : oldUdoInst.getBrancheUdoInstsInfo()) {
					if(isFieldsEquals(bran.getCodice(), oldBran.getCodice())) {
						//Sse hanno lo stesso codice effettuo il confronto
						if(isFieldsChanged(bran.getDescr(), oldBran.getDescr()))
							return true;
						found = true;
					}
				}
				if(!found) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isFattoriProduttiviChange() {
		if(oldUdoInst != null) {
			if(udoInst.getFattProdUdoInstsInfo() == null) {
				if(oldUdoInst.getFattProdUdoInstsInfo() == null)
					return false;
				else
					return true;
			} else {
				if(oldUdoInst.getFattProdUdoInstsInfo() == null)
					return true;
			}
			if(udoInst.getFattProdUdoInstsInfo().size() != oldUdoInst.getFattProdUdoInstsInfo().size())
				return true;
			//Ora devo confrontare tutti i valori
			boolean found = false;
			for(FattProdUdoInstInfo fatProd : udoInst.getFattProdUdoInstsInfo()) {
				found = false;
				for(FattProdUdoInstInfo oldFatProd : oldUdoInst.getFattProdUdoInstsInfo()) {
					if(isFieldsEquals(fatProd.getTipo(), oldFatProd.getTipo()) && isFieldsEquals(fatProd.getTipologiaFattProd(), oldFatProd.getTipologiaFattProd())) {
						//Se sono dello stesso tipo effettuo il confronto
						if(isFieldsChanged(fatProd.getValore(), oldFatProd.getValore()) || isFieldsChanged(fatProd.getValore2(), oldFatProd.getValore2()) || isFieldsChanged(fatProd.getValore3(), oldFatProd.getValore3()))
							return true;
						found = true;
					}
				}
				if(!found) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isFieldsChanged(String value, String oldValue) {
		if(value == null || value.isEmpty()) {
			if(oldValue == null || oldValue.isEmpty())
				return false;
			else
				return true;
		} else {
			if(oldValue == null || oldValue.isEmpty())
				return true;
		}
		return !value.equalsIgnoreCase(oldValue);
	}
	
	private boolean isFieldsChanged(BigDecimal value, BigDecimal oldValue) {
		if(value == null) {
			if(oldValue == null)
				return false;
			else
				return true;
		} else {
			if(oldValue == null)
				return true;
		}
		return value.compareTo(oldValue) != 0;
	}

	private boolean isFieldsEquals(String value, String oldValue) {
		if(value == null || value.isEmpty()) {
			if(oldValue == null || oldValue.isEmpty())
				return true;
			else
				return false;
		} else {
			if(oldValue == null || oldValue.isEmpty())
				return false;
		}
		return value.equalsIgnoreCase(oldValue);
	}
/*
        csvWriter.write("Denominazione");//Denominazione Udo
        //Struttura giuridica NON CONFRONTARE sarebbe il titolare
        csvWriter.write("Sede Operativa");//Sede Operativa
        csvWriter.write("Cod. ULSS");//Cod Ulss
        csvWriter.write("Codice struttura/Denominazione");//nella richiesta viene chiamato "Cod Flussi Ministeriali"
        csvWriter.write("Unità Operativa");//Unita' operativa
        
        //Stabilimento/Blocco/Piano/Progressivo
        csvWriter.write("Edificio");//k Stabilimento
        csvWriter.write("Blocco");//k
        csvWriter.write("Piano");//k
        csvWriter.write("Progressivo");//k
        
        csvWriter.write("Direttore Sanitario");//Direttore Sanitario (Nome/Cognome/Codice Fiscale)
        csvWriter.write("Discipline");//Discipline
        csvWriter.write("Branche/Attività (cod \"spe\")");//Branche
		csvWriter.write("Fattori produttivi");//Fattori produttivi
        //Prestazioni

 */
}
