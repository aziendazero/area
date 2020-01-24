package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * The persistent class for the PARAMETRI database table.
 * 
 */
@Entity
public class Parametri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nome;

	private String note;

	private String valore;

	public Parametri() {
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}