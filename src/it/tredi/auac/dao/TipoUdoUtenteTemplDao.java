package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.TipoTitolareTempl;
import it.tredi.auac.orm.entity.TipoUdoUtenteTempl;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("tipoUdoUtenteTemplDao")
public class TipoUdoUtenteTemplDao extends AbstractJpaDao<TipoUdoUtenteTempl> {
	//private static final Logger log = Logger.getLogger(TipoUdoUtenteTemplDao.class);
	
	public TipoUdoUtenteTemplDao() {
		super();
		setClazz(TipoUdoUtenteTempl.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoUdoUtenteTempl> findTipoUdoUtenteTemplByTipoTitolare(TipoTitolareTempl tipoTitolareTempl) {
		Query q = entityManager.createNamedQuery("TipoUdoUtenteTempl.findUtentiPerTipoTitolare", TipoUdoUtenteTempl.class);
		
		q.setParameter("tipoTitolareTempl", tipoTitolareTempl);

		EntityGraph<?> riInstGraph = entityManager.getEntityGraph("TipoUdoUtenteTempl.utentiConAnagrafica");
		q.setHint("javax.persistence.fetchgraph", riInstGraph);
		q.setHint("eclipselink.left-join-fetch", "r.tipoUdo22Templ");//@ManyToOne		
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.anagraficaUtenteModel");//@ManyToOne
		
		return (List<TipoUdoUtenteTempl>)q.getResultList();
	}	

	/*
	public TipoUdoUtenteTempl getDomandaInstByClientIdForShowFolderEntityGraph(String clientId){
		EntityGraph<?> domandaInstShowFolderGraph = entityManager.getEntityGraph("DomandaInst.showFolder");
		Query q = entityManager.createQuery("select d from DomandaInst d where d.clientid = :clientid");
		q.setParameter("clientid", clientId);
		q.setHint("javax.persistence.fetchgraph", domandaInstShowFolderGraph);

		
		q.setHint("eclipselink.batch.type", "JOIN");//default
		//q.setHint("eclipselink.batch.type", "EXISTS");
		//q.setHint("eclipselink.batch.type", "IN");//ERRORE
		
		//q.setHint("eclipselink.join-fetch", "d.titolareModel");
		//q.setHint("eclipselink.join-fetch", "d.tipoProcTempl");
		
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts");//@OneToMany
		//Occorre impostare questo perche' jpa segue l'albero per un solo livello 
		//quindi caricando la domanda carica la lista uoInst (primo livello) e li si ferma caricando d.uoInsts.uoModel con query separate 
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		//q.setHint("eclipselink.batch", "d.udoInsts");//@OneToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.disciplinaTempls");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.brancaTempls");//@ManyToOne
		
		//Questi 2 vengono caricati insieme alla lista udoInsts per come sono settati sull'entity e in quanto inclusi nel EntityGraph
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		
		//L'ideale sarebbe che venisse caricato anche questo al caricamento della lista udoInsts
		q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl.tipoUdo22Templ");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

	    DomandaInst dom = (DomandaInst)q.getSingleResult();
	    
    	//PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
		//System.out.println("unitUtil.isLoaded(dom, \"uoInsts\"): " + unitUtil.isLoaded(dom, "uoInsts"));
		//System.out.println("unitUtil.isLoaded(dom, \"udoInsts\"): " + unitUtil.isLoaded(dom, "udoInsts"));
	    
	    return dom;
	    //return (DomandaInst)q.getSingleResult();
	}*/
}
