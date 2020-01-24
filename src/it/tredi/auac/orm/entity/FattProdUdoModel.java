package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the FATT_PROD_UDO_MODEL database table.
 * 
 */
@Entity
@Table(name="FATT_PROD_UDO_MODEL")
public class FattProdUdoModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_FATTORE")
	private BigDecimal idFattore;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String valore;

	private String valore2;

	private String valore3;

	//bi-directional many-to-one association to StrutturaModel
	@ManyToOne
	@JoinColumn(name="ID_STRUTTURA_FK")
	private StrutturaModel strutturaModel;

	//bi-directional many-to-one association to TipoFattoreProdTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_FK")
	private TipoFattoreProdTempl tipoFattoreProdTempl;

	//bi-directional many-to-many association to UdoModel
	@ManyToMany(mappedBy="fattProdUdoModels")
	private List<UdoModel> udoModels;

	public FattProdUdoModel() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public BigDecimal getIdFattore() {
		return this.idFattore;
	}

	public void setIdFattore(BigDecimal idFattore) {
		this.idFattore = idFattore;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getValore2() {
		return this.valore2;
	}

	public void setValore2(String valore2) {
		this.valore2 = valore2;
	}

	public String getValore3() {
		return this.valore3;
	}

	public void setValore3(String valore3) {
		this.valore3 = valore3;
	}

	public StrutturaModel getStrutturaModel() {
		return this.strutturaModel;
	}

	public void setStrutturaModel(StrutturaModel strutturaModel) {
		this.strutturaModel = strutturaModel;
	}

	public TipoFattoreProdTempl getTipoFattoreProdTempl() {
		return this.tipoFattoreProdTempl;
	}

	public void setTipoFattoreProdTempl(TipoFattoreProdTempl tipoFattoreProdTempl) {
		this.tipoFattoreProdTempl = tipoFattoreProdTempl;
	}

	public List<UdoModel> getUdoModels() {
		return this.udoModels;
	}

	public void setUdoModels(List<UdoModel> udoModels) {
		this.udoModels = udoModels;
	}

	public String getFullDescr() {
		//getTipo = fattoreProdModel.getTipoFattoreProdTempl().getDescr()
		//Tipo: ' + ${disciplina.tipoFattoreProdTempl.descr} + (${disciplina.valore2 ne null}?', Accreditati: ' + ${disciplina.valore2}:'') + (${disciplina.valore ne null}?', Autorizzati: ' + ${disciplina.valore}:'')
		String val = "";
		if(this.getTipoFattoreProdTempl() == null || this.getTipoFattoreProdTempl().getTipologiaFattProd() == null || FattProdUdoInstInfo.POSTI_LETTO.equals(this.getTipoFattoreProdTempl().getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipoFattoreProdTempl().getDescr()==null) ? "" : this.getTipoFattoreProdTempl().getDescr()) + ", Accreditati: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Autorizzati: " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.POSTI_LETTO_EXTRA.equals(this.getTipoFattoreProdTempl().getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipoFattoreProdTempl().getDescr()==null) ? "" : this.getTipoFattoreProdTempl().getDescr() ) + ", Autorizzati: " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.POSTI_LETTO_OBI.equals(this.getTipoFattoreProdTempl().getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipoFattoreProdTempl().getDescr()==null) ? "" : this.getTipoFattoreProdTempl().getDescr() ) + ", Accreditati: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Autorizzati: " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.POSTI_PAGANTI.equals(this.getTipoFattoreProdTempl().getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipoFattoreProdTempl().getDescr()==null) ? "" : this.getTipoFattoreProdTempl().getDescr() ) + ": " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.SALE_OPERATORIE.equals(this.getTipoFattoreProdTempl().getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipoFattoreProdTempl().getDescr()==null) ? "" : this.getTipoFattoreProdTempl().getDescr() ) + ", Numero: " + (this.getValore()==null ? "" : this.getValore()) + ", Ore: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Descrizione: " + (this.getValore3()==null ? "" : this.getValore3());
		}
		return val;
	}
}