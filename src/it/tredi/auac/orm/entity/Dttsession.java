package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DTTSESSIONS database table.
 * 
 */
@Entity
@Table(name="DTTSESSIONS")
public class Dttsession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long sessid;

	private String sessdescr;

	@Temporal(TemporalType.DATE)
	private Date sessend;

	private String sesshelp;

	private String sesshost;

	private String sessname;

	private String sessobjguid;

	private String sessobjname;

	private BigDecimal sessresult;

	private BigDecimal sesssid;

	@Lob
	private byte[] sesssnapshot;

	@Temporal(TemporalType.DATE)
	private Date sessstart;

	private BigDecimal sesssuite;

	private BigDecimal sesste;

	private String sesstest;

	private String sessurl;

	private BigDecimal sessuserid;

	//bi-directional many-to-one association to Dttsession
	@ManyToOne
	@JoinColumn(name="SESSNEXT")
	private Dttsession dttsession1;

	//bi-directional many-to-one association to Dttsession
	@OneToMany(mappedBy="dttsession1")
	private List<Dttsession> dttsessions1;

	//bi-directional many-to-one association to Dttsession
	@ManyToOne
	@JoinColumn(name="SESSTESTSRC")
	private Dttsession dttsession2;

	//bi-directional many-to-one association to Dttsession
	@OneToMany(mappedBy="dttsession2")
	private List<Dttsession> dttsessions2;

	public Dttsession() {
	}

	public long getSessid() {
		return this.sessid;
	}

	public void setSessid(long sessid) {
		this.sessid = sessid;
	}

	public String getSessdescr() {
		return this.sessdescr;
	}

	public void setSessdescr(String sessdescr) {
		this.sessdescr = sessdescr;
	}

	public Date getSessend() {
		return this.sessend;
	}

	public void setSessend(Date sessend) {
		this.sessend = sessend;
	}

	public String getSesshelp() {
		return this.sesshelp;
	}

	public void setSesshelp(String sesshelp) {
		this.sesshelp = sesshelp;
	}

	public String getSesshost() {
		return this.sesshost;
	}

	public void setSesshost(String sesshost) {
		this.sesshost = sesshost;
	}

	public String getSessname() {
		return this.sessname;
	}

	public void setSessname(String sessname) {
		this.sessname = sessname;
	}

	public String getSessobjguid() {
		return this.sessobjguid;
	}

	public void setSessobjguid(String sessobjguid) {
		this.sessobjguid = sessobjguid;
	}

	public String getSessobjname() {
		return this.sessobjname;
	}

	public void setSessobjname(String sessobjname) {
		this.sessobjname = sessobjname;
	}

	public BigDecimal getSessresult() {
		return this.sessresult;
	}

	public void setSessresult(BigDecimal sessresult) {
		this.sessresult = sessresult;
	}

	public BigDecimal getSesssid() {
		return this.sesssid;
	}

	public void setSesssid(BigDecimal sesssid) {
		this.sesssid = sesssid;
	}

	public byte[] getSesssnapshot() {
		return this.sesssnapshot;
	}

	public void setSesssnapshot(byte[] sesssnapshot) {
		this.sesssnapshot = sesssnapshot;
	}

	public Date getSessstart() {
		return this.sessstart;
	}

	public void setSessstart(Date sessstart) {
		this.sessstart = sessstart;
	}

	public BigDecimal getSesssuite() {
		return this.sesssuite;
	}

	public void setSesssuite(BigDecimal sesssuite) {
		this.sesssuite = sesssuite;
	}

	public BigDecimal getSesste() {
		return this.sesste;
	}

	public void setSesste(BigDecimal sesste) {
		this.sesste = sesste;
	}

	public String getSesstest() {
		return this.sesstest;
	}

	public void setSesstest(String sesstest) {
		this.sesstest = sesstest;
	}

	public String getSessurl() {
		return this.sessurl;
	}

	public void setSessurl(String sessurl) {
		this.sessurl = sessurl;
	}

	public BigDecimal getSessuserid() {
		return this.sessuserid;
	}

	public void setSessuserid(BigDecimal sessuserid) {
		this.sessuserid = sessuserid;
	}

	public Dttsession getDttsession1() {
		return this.dttsession1;
	}

	public void setDttsession1(Dttsession dttsession1) {
		this.dttsession1 = dttsession1;
	}

	public List<Dttsession> getDttsessions1() {
		return this.dttsessions1;
	}

	public void setDttsessions1(List<Dttsession> dttsessions1) {
		this.dttsessions1 = dttsessions1;
	}

	public Dttsession addDttsessions1(Dttsession dttsessions1) {
		getDttsessions1().add(dttsessions1);
		dttsessions1.setDttsession1(this);

		return dttsessions1;
	}

	public Dttsession removeDttsessions1(Dttsession dttsessions1) {
		getDttsessions1().remove(dttsessions1);
		dttsessions1.setDttsession1(null);

		return dttsessions1;
	}

	public Dttsession getDttsession2() {
		return this.dttsession2;
	}

	public void setDttsession2(Dttsession dttsession2) {
		this.dttsession2 = dttsession2;
	}

	public List<Dttsession> getDttsessions2() {
		return this.dttsessions2;
	}

	public void setDttsessions2(List<Dttsession> dttsessions2) {
		this.dttsessions2 = dttsessions2;
	}

	public Dttsession addDttsessions2(Dttsession dttsessions2) {
		getDttsessions2().add(dttsessions2);
		dttsessions2.setDttsession2(this);

		return dttsessions2;
	}

	public Dttsession removeDttsessions2(Dttsession dttsessions2) {
		getDttsessions2().remove(dttsessions2);
		dttsessions2.setDttsession2(null);

		return dttsessions2;
	}

}