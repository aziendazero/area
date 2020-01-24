package it.tredi.auac.orm.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PrestazioneUdoInstInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String nome;
	@SerializedName(value="descr")
	private String descrizione;

	public PrestazioneUdoInstInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


}