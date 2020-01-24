package it.tredi.auac.converter;

import it.tredi.auac.BooleanEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class BooleanEnumConverter implements AttributeConverter<BooleanEnum, String> {
	@Override
    public String convertToDatabaseColumn(BooleanEnum attribute) {
        switch (attribute) {
            case TRUE:
                return "S";
            case FALSE:
                return "N";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }
 
    @Override
    public BooleanEnum convertToEntityAttribute(String dbData) {
        if ("S".equals(dbData))
            return BooleanEnum.TRUE;
        else if ("N".equals(dbData))
			return BooleanEnum.FALSE;
        else
            throw new IllegalArgumentException("TipoValutazioneVerificaEnum dbData:" + dbData + " sconosciuto");
    }
}
