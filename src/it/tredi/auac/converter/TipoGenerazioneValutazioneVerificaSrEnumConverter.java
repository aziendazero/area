package it.tredi.auac.converter;

import it.tredi.auac.TipoGenerazioneValutazioneVerificaSrEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TipoGenerazioneValutazioneVerificaSrEnumConverter implements AttributeConverter<TipoGenerazioneValutazioneVerificaSrEnum, String> {
	// possibili valori: N non aggiorna i dati, P aggiorna il campo VALUTAZIONE, A aggiorna il campo AUTO_VALUTAZIONE
	@Override
    public String convertToDatabaseColumn(TipoGenerazioneValutazioneVerificaSrEnum attribute) {
        switch (attribute) {
            case NESSUNAGGIORNAMENTO:
                return "N";
            case PRINCIPALE:
                return "P";
            case APPOGGIO:
                return "A";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }
 
    @Override
    public TipoGenerazioneValutazioneVerificaSrEnum convertToEntityAttribute(String dbData) {
        if ("N".equals(dbData))
            return TipoGenerazioneValutazioneVerificaSrEnum.NESSUNAGGIORNAMENTO;
        else if ("P".equals(dbData))
			return TipoGenerazioneValutazioneVerificaSrEnum.PRINCIPALE;
        else if ("A".equals(dbData))
        	return TipoGenerazioneValutazioneVerificaSrEnum.APPOGGIO;
        else
            throw new IllegalArgumentException("TipoGenerazioneValutazioneVerificaSrEnum dbData:" + dbData + " sconosciuto");
    }
}
