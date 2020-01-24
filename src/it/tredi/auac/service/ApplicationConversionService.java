package it.tredi.auac.service;

import it.tredi.auac.WebflowSafeStringToDateConverter;

import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component("conversionService")
public class ApplicationConversionService extends DefaultConversionService
{
    @Override
    protected void addDefaultConverters() {
    	super.addDefaultConverters();
    	
    	//string-to-date conversion
    	WebflowSafeStringToDateConverter stringToDateConverter = new WebflowSafeStringToDateConverter();
    	stringToDateConverter.setPattern("dd/MM/yyyy");
    	this.addConverter(stringToDateConverter);
    }
}