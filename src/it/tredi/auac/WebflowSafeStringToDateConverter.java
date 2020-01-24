package it.tredi.auac;
import org.springframework.binding.convert.converters.StringToDate;

public class WebflowSafeStringToDateConverter extends StringToDate {
	
	public WebflowSafeStringToDateConverter() {
		super();
	}

	@Override
	public Object toObject(String string, Class targetClass) throws Exception {
		if (string == null)
			return null;
		return super.toObject(string, targetClass);
	}
	
}
