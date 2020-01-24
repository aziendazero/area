package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;

import java.io.Serializable;

public class CheckPostiLettoDomandaPageBeanEntity implements Serializable {

	private static final long serialVersionUID = 18994960112215983L;
	private String id;
	private String codice;
	private String descr;
	private PostiLettoEntity postiLettoEntity;

	public CheckPostiLettoDomandaPageBeanEntity(String id, String codice, String descr) {
		this.id = id;
		this.codice = codice;
		this.descr = descr;
		postiLettoEntity = new PostiLettoEntity();
	}

	public CheckPostiLettoDomandaPageBeanEntity(DisciplinaUdoInstInfo disciplinaUdoInstInfo) {
		this.id = disciplinaUdoInstInfo.getId();
		this.codice = disciplinaUdoInstInfo.getCodice();
		this.descr = disciplinaUdoInstInfo.getDescr();
		postiLettoEntity = new PostiLettoEntity(disciplinaUdoInstInfo);
	}
	
	public void addPostiletto(DisciplinaUdoInstInfo disciplinaUdoInstInfo) {
		if(this.id.equals(disciplinaUdoInstInfo.getId())) {
			this.postiLettoEntity.addPostiLetto(disciplinaUdoInstInfo);
		}
	}
	
	public void addPostiletto(FattProdUdoInstInfo fattProdUdoInstInfo) {
		this.postiLettoEntity.addPostiLetto(fattProdUdoInstInfo);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public PostiLettoEntity getPostiLettoEntity() {
		return postiLettoEntity;
	}

	public void setPostiLettoEntity(PostiLettoEntity postiLettoEntity) {
		this.postiLettoEntity = postiLettoEntity;
	}

}
