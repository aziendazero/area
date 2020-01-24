package it.tredi.auac.orm.entity;

import java.io.Serializable;

public class EstensioneUdoInstInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String descr;
	private String nome;

	public EstensioneUdoInstInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}