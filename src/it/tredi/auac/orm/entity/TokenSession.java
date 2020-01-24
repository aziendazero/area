package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TOKEN_SESSION database table.
 * 
 */
@Entity
@Table(name="TOKEN_SESSION")
public class TokenSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String token;

	public TokenSession() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}