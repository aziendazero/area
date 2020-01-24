package it.tredi.auac.converter;

import it.tredi.auac.TipoValutazioneVerificaEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TipoValutazioneVerificaEnumConverter implements AttributeConverter<TipoValutazioneVerificaEnum, String> {
	@Override
    public String convertToDatabaseColumn(TipoValutazioneVerificaEnum attribute) {
        switch (attribute) {
            case MANUALE:
                return "M";
            case AUTOMATICA:
                return "A";
            case SEMIAUTOMATICA:
                return "S";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }
 
    @Override
    public TipoValutazioneVerificaEnum convertToEntityAttribute(String dbData) {
        if ("M".equals(dbData))
            return TipoValutazioneVerificaEnum.MANUALE;
        else if ("A".equals(dbData))
			return TipoValutazioneVerificaEnum.AUTOMATICA;
        else if ("S".equals(dbData))
        	return TipoValutazioneVerificaEnum.SEMIAUTOMATICA;
        else
            throw new IllegalArgumentException("TipoValutazioneVerificaEnum dbData:" + dbData + " sconosciuto");
    }
}
