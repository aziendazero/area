package it.tredi.auac;

import java.util.Comparator;
import java.util.Map;

public class DocDataDecrescenteComparator implements Comparator<Map<String,String>> {
	//Ritorno -1 se o1 e' minore di o2; 0 se o1 e' uguale a o2, 1 se o1 e' maggiore do o2 
	@Override
    public int compare(Map<String,String> o1, Map<String,String> o2) {
		if(o1.get("creazione_data") == null) {
			if(o2.get("creazione_data") == null)
				return 0;
			else
				return 1;
		} else {
			if(o2.get("creazione_data") == null)
				return -1;
			return - o1.get("creazione_data").compareTo(o2.get("creazione_data"));
		}
    }
}
