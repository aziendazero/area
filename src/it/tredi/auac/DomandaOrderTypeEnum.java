package it.tredi.auac;

public enum DomandaOrderTypeEnum {
	NumeroProcedimentoCrescente("domandaOrderTypeEnum.numeroProcedimentoCrescente") {
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@numero_procedimento)";
		}		
	},
	NumeroProcedimentoDecrescente("domandaOrderTypeEnum.numeroProcedimentoDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@numero_procedimento)";
		}		
	},

	OggettoCrescente("domandaOrderTypeEnum.oggettoCrescente") {
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/oggetto)";
		}		
	},
	OggettoDecrescente("domandaOrderTypeEnum.oggettoDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/oggetto)";
		}		
	},

	TipoDomandaCrescente("domandaOrderTypeEnum.tipoDomandaCrescente") {
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@oggetto_domanda)";
		}		
	},
	TipoDomandaDecrescente("domandaOrderTypeEnum.tipoDomandaDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@oggetto_domanda)";
		}		
	},

	TitolareCrescente("domandaOrderTypeEnum.titolareCrescente") {
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/titolare/ragione_sociale)";
		}		
	},
	TitolareDecrescente("domandaOrderTypeEnum.titolareDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/titolare/ragione_sociale)";
		}		
	},

	StatoCrescente("domandaOrderTypeEnum.statoCrescente") {
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/@folder_status)";
		}		
	},
	StatoDecrescente("domandaOrderTypeEnum.statoDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/@folder_status)";
		}		
	},
	
	DataCreazioneCrescente("domandaOrderTypeEnum.dataCreazioneCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/storia/creazione/@data:n)";
		}		
	},
	DataCreazioneDecrescente("domandaOrderTypeEnum.dataCreazioneDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/storia/creazione/@data:n)";
		}		
	},
	
	DataInvioCrescente("domandaOrderTypeEnum.dataInvioCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@data_invio_domanda:n)";
		}		
	},
	DataInvioDecrescente("domandaOrderTypeEnum.dataInvioDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@data_invio_domanda:n)";
		}		
	},

	DataValutazioneCompletezzaCorrettezzaCrescente("domandaOrderTypeEnum.dataValutazioneCompletezzaCorrettezzaCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@valutazione_completezza_correttezza:n)";
		}		
	},
	DataValutazioneCompletezzaCorrettezzaDecrescente("domandaOrderTypeEnum.dataValutazioneCompletezzaCorrettezzaDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@valutazione_completezza_correttezza:n)";
		}		
	},
	
	DataValutazioneRispondenzaProgrammazioneCrescente("domandaOrderTypeEnum.dataValutazioneRispondenzaProgrammazioneCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@valutazione_rispondenza_programmazione:n)";
		}		
	},
	DataValutazioneRispondenzaProgrammazioneDecrescente("domandaOrderTypeEnum.dataValutazioneRispondenzaProgrammazioneDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@valutazione_rispondenza_programmazione:n)";
		}		
	},
	
	DataConferimentoIncaricoCrescente("domandaOrderTypeEnum.dataConferimentoIncaricoCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@conferimento_incarico:n)";
		}		
	},
	DataConferimentoIncaricoDecrescente("domandaOrderTypeEnum.dataConferimentoIncaricoDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@conferimento_incarico:n)";
		}		
	},

	DataRedazioneRapportoVerificaCrescente("domandaOrderTypeEnum.dataRedazioneRapportoVerificaCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@redazione_rapporto_verifica)";
		}		
	},
	DataRedazioneRapportoVerificaDecrescente("domandaOrderTypeEnum.dataRedazioneRapportoVerificaDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@redazione_rapporto_verifica)";
		}		
	},

	DataPresentazioneProvvedimentoCrescente("domandaOrderTypeEnum.dataPresentazioneProvvedimentoCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@presentazione_provvedimento:n)";
		}		
	},
	DataPresentazioneProvvedimentoDecrescente("domandaOrderTypeEnum.dataPresentazioneProvvedimentoDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@presentazione_provvedimento:n)";
		}		
	},

	DataValutazioneCollegialeCrescente("domandaOrderTypeEnum.dataValutazioneCollegialeCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@valutazione_collegiale:n)";
		}		
	},
	DataValutazioneCollegialeDecrescente("domandaOrderTypeEnum.dataValutazioneCollegialeDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@valutazione_collegiale:n)";
		}		
	},

	DataConclusioneCrescente("domandaOrderTypeEnum.dataConclusioneCrescente"){
		public String getSortRule() {
			return "Xml(xpart:/fascicolo/extra/procedimento/@data_conclusione:n)";
		}		
	},
	DataConclusioneDecrescente("domandaOrderTypeEnum.dataConclusioneDecrescente"){
		public String getSortRule() {
			return "xml(xpart:/fascicolo/extra/procedimento/@data_conclusione:n)";
		}		
	},
	
	;

	
	private String value;
	 
    private DomandaOrderTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
	public abstract String getSortRule();    
}
