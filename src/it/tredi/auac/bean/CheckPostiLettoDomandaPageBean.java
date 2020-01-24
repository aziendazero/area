package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.UdoInst;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckPostiLettoDomandaPageBean implements Serializable {

	private static final long serialVersionUID = 2700884567597815958L;
	private Map<String, CheckPostiLettoDomandaPageBeanEntity> checkPostiLettoDomandaPageBeanEntityL;

	public CheckPostiLettoDomandaPageBean() {
		checkPostiLettoDomandaPageBeanEntityL = new HashMap<String, CheckPostiLettoDomandaPageBeanEntity>();
	}
	
	public void addUdoInst(UdoInst udoInst) {
		//Nota del 13/06/2017 vengono considerate solo le udo che hanno discipline in quanto non interessano i posti letti non associati alle discipline
		//Se la udo è modulo i posti letto sono agganciati alla discilina
		//Se la udo non è modulo i posti letto nei fattori produttivi sono relativi alla unica disciplina inserita

		//Modifica del 13/06/2017 per mostrare la sotto lista della somma dei posti letto per ogni disciplina indipendentemente dal fatto che la Udo sia modulo oppure no
		if("Y".equalsIgnoreCase(udoInst.getFlagModulo())) {
			//Controllo le discipline
			if(udoInst.getDisciplineUdoInstsInfo() != null) {
				for(DisciplinaUdoInstInfo discInfo : udoInst.getDisciplineUdoInstsInfo()) {
					addDisciplinaUdoInstInfo(discInfo);
				}
			}
		} else {
			// 02/04/2019 per le nuove domande i posti letto saranno agganciati solo alle DisciplineUdoInstsInfo anche quando modulo='N' 
			// in tal caso la DisciplineUdoInstsInfo non sara' agganciata alla disciplina ma al Ambito, quindi
			if(isAmbito(udoInst.getDisciplineUdoInstsInfo())) {
				if(udoInst.getDisciplineUdoInstsInfo() != null) {
					for(DisciplinaUdoInstInfo discInfo : udoInst.getDisciplineUdoInstsInfo()) {
						addDisciplinaUdoInstInfo(discInfo);
					}
				}
			} else {
				// 02/04/2019 mi comporto come prima 
				if(udoInst.getFattProdUdoInstsInfo() != null && !udoInst.getFattProdUdoInstsInfo().isEmpty()) {
					// 02/04/2019 mi comporto come prima solo se ci sono dei fattori produttivi 
					if(udoInst.getDisciplineUdoInstsInfo() != null && udoInst.getDisciplineUdoInstsInfo().size() == 1) {
						DisciplinaUdoInstInfo disciplinaUdoInstInfo = udoInst.getDisciplineUdoInstsInfo().get(0);
						CheckPostiLettoDomandaPageBeanEntity checkPostiLettoDomandaPageBeanEntity = checkPostiLettoDomandaPageBeanEntityL.get(disciplinaUdoInstInfo.getId());
						if(checkPostiLettoDomandaPageBeanEntity == null) {
							checkPostiLettoDomandaPageBeanEntity = new CheckPostiLettoDomandaPageBeanEntity(disciplinaUdoInstInfo.getId(), disciplinaUdoInstInfo.getCodice(), disciplinaUdoInstInfo.getDescr());
							checkPostiLettoDomandaPageBeanEntityL.put(disciplinaUdoInstInfo.getId(), checkPostiLettoDomandaPageBeanEntity);
						}
						for(FattProdUdoInstInfo fattProdInfo : udoInst.getFattProdUdoInstsInfo()) {
							checkPostiLettoDomandaPageBeanEntity.addPostiletto(fattProdInfo);
						}
					}
				} else {
					if(udoInst.getDisciplineUdoInstsInfo() != null) {
						for(DisciplinaUdoInstInfo discInfo : udoInst.getDisciplineUdoInstsInfo()) {
							addDisciplinaUdoInstInfo(discInfo);
						}
					}					
				}
			}
		}
	}
	
	private boolean isAmbito(List<DisciplinaUdoInstInfo> discipineUdoInstInfo) {
		if(discipineUdoInstInfo != null) {
			for(DisciplinaUdoInstInfo discUdo : discipineUdoInstInfo) {
				if(!discUdo.isDisciplina())
					return true;
			}
		}
		return false;
	}

	private void addDisciplinaUdoInstInfo(DisciplinaUdoInstInfo disciplinaUdoInstInfo) {
		if(disciplinaUdoInstInfo.getPostiLetto() != null) {
			CheckPostiLettoDomandaPageBeanEntity obj = checkPostiLettoDomandaPageBeanEntityL.get(disciplinaUdoInstInfo.getId());
			if(obj == null)
				checkPostiLettoDomandaPageBeanEntityL.put(disciplinaUdoInstInfo.getId(), new CheckPostiLettoDomandaPageBeanEntity(disciplinaUdoInstInfo));
			else
				obj.addPostiletto(disciplinaUdoInstInfo);
		}
	}
	
	public Collection<CheckPostiLettoDomandaPageBeanEntity> getCheckPostiLettoDomandaPageBeanEntityList() {
		return checkPostiLettoDomandaPageBeanEntityL.values();
	}

	public long getPostiLettoTotaliAutorizzati() {
		BigDecimal postiLettoTotaliAutorizzati = new BigDecimal(0);
		for(CheckPostiLettoDomandaPageBeanEntity checkPostiLettoDomandaPageBeanEntity : getCheckPostiLettoDomandaPageBeanEntityList()) {
			postiLettoTotaliAutorizzati = postiLettoTotaliAutorizzati.add(checkPostiLettoDomandaPageBeanEntity.getPostiLettoEntity().getPostiLettoTotaliAutorizzati());
		}
		return postiLettoTotaliAutorizzati.longValue();
	}

	public long getPostiLettoTotaliAccreditati() {
		BigDecimal postiLettoTotaliAccreditati = new BigDecimal(0);
		for(CheckPostiLettoDomandaPageBeanEntity checkPostiLettoDomandaPageBeanEntity : getCheckPostiLettoDomandaPageBeanEntityList()) {
			postiLettoTotaliAccreditati = postiLettoTotaliAccreditati.add(checkPostiLettoDomandaPageBeanEntity.getPostiLettoEntity().getPostiLettoTotaliAccreditati());
		}
		return postiLettoTotaliAccreditati.longValue();
	}
}
