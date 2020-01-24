package it.tredi.auac.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import it.tredi.auac.TipoPostiLettoEnum;
import it.tredi.auac.orm.entity.BindUdoDisciplina;
import it.tredi.auac.orm.entity.BrancaTempl;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoModel;

@Repository("udoModelDao")
public class UdoModelDao extends AbstractJpaDao<UdoModel> {
	private static final Logger log = Logger.getLogger(UdoModelDao.class);
	
	public UdoModelDao() {
		super();
		setClazz(UdoModel.class);
	}

	/*public List<UdoModel>findValidateByTitolare(TitolareModel titolareModel) {
		Query q = entityManager.createQuery("select u from UdoModel u join u.sedeOperModel s join s.strutturaModel st join st.titolareModel t" +
				" where t = :titolareModel and u.idUnivoco is not null");
		q.setParameter("titolareModel", titolareModel);
	    return (List<UdoModel>)q.getResultList();
	}*/
	
	@SuppressWarnings("unchecked")
	public List<UdoModel>findValidateByTitolareSenzaProcedimentiInCorso(TitolareModel titolareModel, String denominazioneFiltro, 
			String tipoUdoFiltro, String brancaFiltro, String disciplinaFiltro, String sedeOperativaFiltro, String UOFiltro,
			String direttoreFiltro, String codiceUnivocoFiltro, 
			String codiceUlssFiltro, String codiceExUlssFiltro) {
		//String queryDaEseguire ="select u from UdoModel u join u.sedeOperModel s join s.strutturaModel st join st.titolareModel t" +
		//		" where t = :titolareModel and u.idUnivoco is not null and u.procedimentoInCorso = :procInCorso";
		String queryDaEseguire ="select u from UdoModel u join u.uoModel uo join uo.titolareModel t" +
				" where t = :titolareModel and u.validata = :validata and u.procedimentoInCorso = :procInCorso";
		if(denominazioneFiltro != null && denominazioneFiltro.length() > 0)
			queryDaEseguire += " and lower(u.descr) like lower('%" + denominazioneFiltro + "%')";
		if(tipoUdoFiltro != null && tipoUdoFiltro.length() > 0)
			queryDaEseguire += " and lower(u.tipoUdoTempl.descr) like lower('%" + tipoUdoFiltro + "%')";
		if(sedeOperativaFiltro != null && sedeOperativaFiltro.length() > 0)
			queryDaEseguire += " and lower(u.sedeOperModel.denominazione) like lower('%" + sedeOperativaFiltro + "%')";
		if(UOFiltro != null && UOFiltro.length() > 0){
			queryDaEseguire += " and lower(u.uoModel.denominazione) like lower('%" + UOFiltro + "%')";
		}
		if(direttoreFiltro != null && direttoreFiltro.length() > 0){
			queryDaEseguire += " and concat(lower(u.dirSanitarioCogn), ' ', lower(u.dirSanitarioNome)) like lower('%" + direttoreFiltro + "%')";
		}
		if(codiceUnivocoFiltro != null && codiceUnivocoFiltro.length() > 0){
			queryDaEseguire += " and lower(u.idUnivoco) like lower('%" + codiceUnivocoFiltro + "%')";
		}
		if(codiceUlssFiltro != null && codiceUlssFiltro.length() > 0) {
			queryDaEseguire += " and lower(u.codiceAziendaUlss) like lower('%" + codiceUlssFiltro + "%')";
		}
		if(codiceExUlssFiltro != null && codiceExUlssFiltro.length() > 0) {
			queryDaEseguire += " and lower(u.codiceUlssPrecedente) like lower('%" + codiceExUlssFiltro + "%')";
		}
		
		queryDaEseguire += " ORDER BY uo.denominazione";

		Query q = entityManager.createQuery(queryDaEseguire);
		q.setParameter("titolareModel", titolareModel);
		q.setParameter("procInCorso", "N");
		q.setParameter("validata", "Y");
		List<UdoModel> listaUdo = q.getResultList();
		
		//TODO questa cosa fa schifo, dovrebbe essere fatta per bene la join (da migliorare)
		if((brancaFiltro != null && brancaFiltro.length() > 0)||(disciplinaFiltro != null && disciplinaFiltro.length() > 0)){
			List<UdoModel> listaUdoFiltrata = new ArrayList<UdoModel>();
			boolean mettiEntrambi = false;
			boolean messaBranca = false;
			if((brancaFiltro != null && brancaFiltro.length() > 0)&&(disciplinaFiltro != null && disciplinaFiltro.length() > 0)){
				mettiEntrambi = true;
			}
			for (UdoModel udo:listaUdo){
				messaBranca = false;
				if (brancaFiltro != null && brancaFiltro.length() > 0)
					for(BrancaTempl branca:udo.getBrancaTempls()){
						if(branca.getDescr().toLowerCase().contains(brancaFiltro.toLowerCase())){
							if (mettiEntrambi)
								messaBranca = true;
							else
								listaUdoFiltrata.add(udo);
							break;
						}	
					}
				if (disciplinaFiltro != null && disciplinaFiltro.length() > 0 && !listaUdoFiltrata.contains(udo))
					for(BindUdoDisciplina disciplinaBind:udo.getBindUdoDisciplinas()){
						if(disciplinaBind.getDisciplinaTempl().getDescr().toLowerCase().contains(disciplinaFiltro.toLowerCase())){
							if (mettiEntrambi)
								if(messaBranca)
									listaUdoFiltrata.add(udo);
								else;
							else
								listaUdoFiltrata.add(udo);
							break;
						}	
					}
			}
			listaUdo = listaUdoFiltrata;
		}
			
		
	    return listaUdo;
	}
	
	public UdoModel findByClientId(String clientId) {
		Query q = entityManager.createQuery("select u from UdoModel u where u.clientid = :clientid");
		q.setParameter("clientid", clientId);
		//Pezza per evitare che cancellando una UdoInst di una domanda riportata in BOZZA si ottenga una eccezione
		//cercando la relativa UdoModel che in realta e' stata cancellata quando la domanda era in "PROCEDIMENTO AVVIATO"
	    try {
		    return (UdoModel)q.getSingleResult();
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare la UdoModel con clientid: " + clientId, ex);
	    	return null;
	    }
	}
	
	public List<DisciplinaUdoInstInfo> findDisciplinaUdoInstInfoByTitolareForRichiesti(TitolareModel titolare){
		List<DisciplinaUdoInstInfo> toRet = new ArrayList<DisciplinaUdoInstInfo>();
		Query q = entityManager.createNativeQuery("SELECT dt.CLIENTID,dt.CODICE,dt.DESCR,rd.DENOMINAZIONE" + 
				",SUM(bud.POSTI_LETTO),SUM(bud.POSTI_LETTO_ACC),SUM(bud.POSTI_LETTO_EXTRA),rd.ORDINE, dt.ORDINE " + 
				"FROM UDO_MODEL udo " + 
				"INNER JOIN UO_MODEL_TITOLARE_UNION umt ON umt.ID_UO = udo.ID_UO and umt.PROVENIENZA = udo.PROVENIENZA_UO " + 
				"INNER JOIN BIND_UDO_DISCIPLINA bud ON bud.ID_UDO_FK = udo.CLIENTID " + 
				"INNER JOIN DISCIPLINA_TEMPL dt ON dt.CLIENTID = bud.ID_DISCIPLINA_FK " + 
				"INNER JOIN RAGG_DISCPL rd ON rd.CLIENTID = dt.ID_RAGG_DISCIPL_TEMPL_FK " + 
				"WHERE udo.VALIDATA = ? AND umt.ID_TITOLARE_FK = ? " + 
				"GROUP BY dt.CLIENTID, dt.CODICE, dt.DESCR,rd.DENOMINAZIONE,rd.ORDINE,dt.ORDINE"); 
		q.setParameter(1, "Y");
		q.setParameter(2, titolare.getClientid());
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				DisciplinaUdoInstInfo discUdoInfo = new DisciplinaUdoInstInfo();
				discUdoInfo.setId((String) result[0]);
				discUdoInfo.setCodice((String) result[1]);
				discUdoInfo.setDescr((String) result[2]);
				discUdoInfo.setArea((String) result[3]);
				discUdoInfo.setPostiLetto((BigDecimal) result[4]);
				discUdoInfo.setPostiLettoAcc((BigDecimal) result[5]);
				discUdoInfo.setPostiLettoExtra((BigDecimal) result[6]);
				discUdoInfo.setOrdineArea((BigDecimal) result[7]);
				discUdoInfo.setOrdineDisc((BigDecimal) result[8]);
				discUdoInfo.setTipoPostiLetto(TipoPostiLettoEnum.RICHIESTI);
				toRet.add(discUdoInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare la DisciplinaUdoInstInfo con validita:'Y' and titolare_fk:'" + titolare.getClientid()+"'", ex);
	    	return null;
	    }
		return toRet;
	}
	
	public List<DisciplinaUdoInstInfo> findDisciplinaUdoInstInfoAttuatiForSedeOperModel(TitolareModel titolare){
		List<DisciplinaUdoInstInfo> toRet = new ArrayList<DisciplinaUdoInstInfo>();
		Query q = entityManager.createNativeQuery("SELECT som.CLIENTID,som.DENOMINAZIONE,dt.CODICE,dt.DESCR," +
				"SUM(bud.POSTI_LETTO) + SUM(bud.POSTI_LETTO_ACC) + SUM(bud.POSTI_LETTO_EXTRA) as pl_att, MAX(dt.ORDINE) as ord_disc " + 
				"FROM UDO_MODEL um " + 
				"INNER JOIN SEDE_OPER_MODEL som ON som.CLIENTID = um.ID_SEDE_FK " + 
//				"LEFT JOIN OSPEDALE_PROGR osp on osp.CLIENTID = som.ID_OSPEDALE_PROG_FK " + 
				"INNER JOIN TIPO_UDO_TEMPL tut ON tut.CLIENTID = um.ID_TIPO_UDO_FK " + 
				"INNER JOIN TIPO_UDO_22_TEMPL tu22t ON tu22t.CLIENTID = tut.ID_TIPO_UDO_22_FK " + 
				"INNER JOIN UO_MODEL_TITOLARE_UNION umt ON umt.ID_UO = um.ID_UO and umt.PROVENIENZA = um.PROVENIENZA_UO " + 
				"INNER JOIN BIND_UDO_DISCIPLINA bud ON bud.ID_UDO_FK = um.CLIENTID " + 
				"INNER JOIN DISCIPLINA_TEMPL dt ON dt.CLIENTID = bud.ID_DISCIPLINA_FK " + 
				"WHERE um.VALIDATA = ? "+
//				"AND tu22t.OSPEDALIERO = ? AND umt.ID_TITOLARE_FK = ? " +
				"GROUP BY dt.CODICE,dt.DESCR,som.CLIENTID,som.DENOMINAZIONE " + 
				"ORDER BY som.DENOMINAZIONE,dt.DESCR"); 
		q.setParameter(1, "Y");
//		q.setParameter(2, "S");
//		q.setParameter(3, titolare.getClientid());
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				DisciplinaUdoInstInfo discUdoInfo = new DisciplinaUdoInstInfo();
				discUdoInfo.setId((String) result[0]);
				discUdoInfo.setSede((String) result[1]);
				discUdoInfo.setCodice((String) result[2]);
				discUdoInfo.setDescr((String) result[3]);
				discUdoInfo.setPostiLetto((BigDecimal) result[4]);
				discUdoInfo.setOrdineDisc((BigDecimal) result[5]);
				discUdoInfo.setTipoPostiLetto(TipoPostiLettoEnum.ATTUATI);
				toRet.add(discUdoInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare DisciplinaUdoInstInfo (PL attuati)" + titolare.getClientid()+"'", ex);
	    	return null;
	    }
		return toRet;
	}
	
	public List<DisciplinaUdoInstInfo> findDisciplinaUdoInstInfoProgrammatiForSedeOperModel(TitolareModel titolare){
		List<DisciplinaUdoInstInfo> toRet = new ArrayList<DisciplinaUdoInstInfo>();
		Query q = entityManager.createNativeQuery("SELECT dt.CLIENTID,som.DENOMINAZIONE,dt.CODICE," +
				"dt.DESCR,pd.POSTI_LETTO_REGIONE, dt.ORDINE " + 
				"FROM PROGRAMMAZIONE_DISCIPLINA pd " + 
				"INNER JOIN PROGRAMMAZIONE pr ON pd.ID_PROGRAMMAZIONE_FK = pr.CLIENTID " + 
				"INNER JOIN OSPEDALE_PROGR op ON pr.ID_OSPED_PROG_FK = op.CLIENTID " + 
				"INNER JOIN SEDE_OPER_MODEL som ON som.ID_OSPEDALE_PROG_FK = op.CLIENTID " + 
				"INNER JOIN UDO_MODEL um ON um.ID_SEDE_FK = som.CLIENTID " + 
				"INNER JOIN DISCIPLINA_TEMPL dt ON dt.CLIENTID = pd.ID_DISCIPLINA_FK " + 
				"INNER JOIN TIPO_UDO_TEMPL tut ON tut.CLIENTID = um.ID_TIPO_UDO_FK " + 
				"INNER JOIN TIPO_UDO_22_TEMPL tu22t ON tu22t.CLIENTID = tut.ID_TIPO_UDO_22_FK " + 
				"WHERE tu22t.OSPEDALIERO = ? and pr.ID_TITOLARE_FK = ?"); 
		q.setParameter(1, "S");
		q.setParameter(2, titolare.getClientid());
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				DisciplinaUdoInstInfo discUdoInfo = new DisciplinaUdoInstInfo();
				discUdoInfo.setId((String) result[0]);
				discUdoInfo.setSede((String) result[1]);
				discUdoInfo.setCodice((String) result[2]);
				discUdoInfo.setDescr((String) result[3]);
				discUdoInfo.setPostiLettoAcc((BigDecimal) result[4]);
				discUdoInfo.setOrdineDisc((BigDecimal) result[5]);
				discUdoInfo.setTipoPostiLetto(TipoPostiLettoEnum.PROGRAMMATI);
				toRet.add(discUdoInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare DisciplinaUdoInstInfo (PL programmati)" + titolare.getClientid()+"'", ex);
	    	return null;
	    }
		return toRet;
	}
	
	public List<BrancaUdoInstInfo> findBrancaUdoInstInfoProgrammatiForSedeOperModel(TitolareModel titolare){
		List<BrancaUdoInstInfo> toRet = new ArrayList<BrancaUdoInstInfo>();
		Query q = entityManager.createNativeQuery("SELECT bt.CLIENTID,som.DENOMINAZIONE,bt.CODICE,bt.DESCR,bt.ORDINE " + 
				"FROM PROGRAMMAZIONE_DISCIPLINA pd " + 
				"INNER JOIN PROGRAMMAZIONE pr on pd.ID_PROGRAMMAZIONE_FK = pr.CLIENTID " + 
				"INNER JOIN OSPEDALE_PROGR op on pr.ID_OSPED_PROG_FK = op.CLIENTID " + 
				"INNER JOIN SEDE_OPER_MODEL som on som.ID_OSPEDALE_PROG_FK = op.CLIENTID " + 
				"INNER JOIN UDO_MODEL um on um.ID_SEDE_FK = som.CLIENTID " + 
				"INNER JOIN BRANCA_TEMPL bt on bt.CLIENTID = pd.ID_BRANCA_FK " + 
				"INNER JOIN  TIPO_UDO_TEMPL tut on tut.CLIENTID = um.ID_TIPO_UDO_FK " + 
				"INNER JOIN  TIPO_UDO_22_TEMPL tu22t on tu22t.CLIENTID = tut.ID_TIPO_UDO_22_FK " + 
				"WHERE pr.ID_TITOLARE_FK = ? and tu22t.OSPEDALIERO = ?"); 
		q.setParameter(2, titolare.getClientid());
		q.setParameter(1, "S");
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				BrancaUdoInstInfo branUdoInfo = new BrancaUdoInstInfo();
				branUdoInfo.setId((String) result[0]);
				branUdoInfo.setSede((String) result[1]);
				branUdoInfo.setCodice((String) result[2]);
				branUdoInfo.setDescr((String) result[3]);
				branUdoInfo.setOrdine((BigDecimal) result[4]);
				branUdoInfo.setTipoPostiLetto(TipoPostiLettoEnum.PROGRAMMATI);
				toRet.add(branUdoInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare BrancaUdoInstInfo (PL programmati)" + titolare.getClientid()+"'", ex);
	    	return null;
	    }
		return toRet;
	}

	public List<BrancaUdoInstInfo> findBrancaUdoInstInfoAttuatiForSedeOperModel(TitolareModel titolare){
		List<BrancaUdoInstInfo> toRet = new ArrayList<BrancaUdoInstInfo>();
		Query q = entityManager.createNativeQuery("SELECT bt.CLIENTID,som.DENOMINAZIONE,bt.CODICE,bt.DESCR, bt.ORDINE " + 
				"FROM UDO_MODEL um " + 
				"INNER JOIN SEDE_OPER_MODEL som on som.CLIENTID = um.ID_SEDE_FK " + 
				"INNER JOIN  TIPO_UDO_TEMPL tut on tut.CLIENTID = um.ID_TIPO_UDO_FK " + 
				"INNER JOIN  TIPO_UDO_22_TEMPL tu22t on tu22t.CLIENTID = tut.ID_TIPO_UDO_22_FK " + 
				"INNER JOIN UO_MODEL_TITOLARE_UNION umt ON umt.ID_UO = um.ID_UO and umt.PROVENIENZA = um.PROVENIENZA_UO " + 
				"INNER JOIN BIND_UDO_BRANCA bub ON bub.ID_UDO_FK = um.CLIENTID " + 
				"INNER JOIN BRANCA_TEMPL bt ON bt.CLIENTID = bub.ID_BRANCA_FK " + 
				"WHERE um.VALIDATA= ? AND tu22t.OSPEDALIERO = ? AND umt.ID_TITOLARE_FK = ? " + 
				"GROUP BY bt.CLIENTID,bt.CODICE,bt.DESCR,som.DENOMINAZIONE,bt.ORDINE " + 
				"ORDER BY som.DENOMINAZIONE,bt.DESCR"); 
		q.setParameter(1, "Y");
		q.setParameter(2, "S");
		q.setParameter(3, titolare.getClientid());
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				BrancaUdoInstInfo branUdoInfo = new BrancaUdoInstInfo();
				branUdoInfo.setId((String) result[0]);
				branUdoInfo.setSede((String) result[1]);
				branUdoInfo.setCodice((String) result[2]);
				branUdoInfo.setDescr((String) result[3]);
				branUdoInfo.setOrdine((BigDecimal) result[4]);
				branUdoInfo.setTipoPostiLetto(TipoPostiLettoEnum.ATTUATI);
				toRet.add(branUdoInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare BrancaUdoInstInfo (PL attuati)" + titolare.getClientid()+"'", ex);
	    	return null;
	    }
		return toRet;
	}
}
