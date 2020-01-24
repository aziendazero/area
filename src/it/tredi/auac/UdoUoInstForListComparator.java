package it.tredi.auac;

import it.tredi.auac.bean.UdoUoInstForList;

public abstract class UdoUoInstForListComparator {
	
	
	//Ritorno -1 se o1 e' minore di o2; 0 se o1 e' ugulae a o2, 1 se o1 e' maggiore do o2 
    /*
     * Sono gestiti tutti i casi a parte il caso in cui entrambi i parametri sono udo o entrambi i parametri sono uo
     */
	protected int compareNotAllUdoNotAllUo(UdoUoInstForList o1, UdoUoInstForList o2) {
    	if(o1.isUdo() && o2.isUo())
			return -1;
		else if(o1.isUo() && o2.isUdo())
			return 1;
		else if(o1.isRequisitiGeneraliAziendali())
			if(!o2.isRequisitiGeneraliAziendali())
				return 1;
			else
				return 0;
		else if(o2.isRequisitiGeneraliAziendali())
			if(!o1.isRequisitiGeneraliAziendali())
				return -1;
			else
				return 0;
		return 0;
    }

    /*
     * Sono gestiti tutti i casi a parte il caso in cui entrambi i parametri sono udo
     */
    protected int compareIfNotAllUdo(UdoUoInstForList o1, UdoUoInstForList o2) {
    	if(o1.isUdo() && o2.isUo())
			return -1;
		else if(o1.isUo() && o2.isUdo())
			return 1;
		else if(o1.isUo() && o2.isUo())
			//entrambe uo
			return o1.getUoInst().getDenominazione().compareTo(o2.getUoInst().getDenominazione());
		else if(o1.isRequisitiGeneraliAziendali())
			if(!o2.isRequisitiGeneraliAziendali())
				return 1;
			else
				return 0;
		else if(o2.isRequisitiGeneraliAziendali())
			if(!o1.isRequisitiGeneraliAziendali())
				return -1;
			else
				return 0;
		return 0;
    }
}
