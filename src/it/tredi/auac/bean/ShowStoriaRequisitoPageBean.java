package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;

import java.io.Serializable;
import java.util.List;

public class ShowStoriaRequisitoPageBean implements Serializable {

	private static final long serialVersionUID = -1657158419958888871L;

	private List<StoricoRisposteRequisiti> storicoRisposteRequisitiL;

	public List<StoricoRisposteRequisiti> getStoricoRisposteRequisitiL() {
		return storicoRisposteRequisitiL;
	}

	public void setStoricoRisposteRequisitiL(
			List<StoricoRisposteRequisiti> storicoRisposteRequisitiL) {
		this.storicoRisposteRequisitiL = storicoRisposteRequisitiL;
	}

}
