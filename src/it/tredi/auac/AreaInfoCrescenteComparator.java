package it.tredi.auac;

import it.tredi.auac.bean.IAreaInfo;

import java.util.Comparator;

public class AreaInfoCrescenteComparator implements Comparator<IAreaInfo> {
	//Ritorno -1 se o1 e' minore di o2; 0 se o1 e' uguale a o2, 1 se o1 e' maggiore do o2 
	@Override
    public int compare(IAreaInfo o1, IAreaInfo o2) {
		if(o1.getNome() == null) {
			if(o2.getNome() == null)
				return 0;
			else
				return 1;
		} else {
			if(o2.getNome() == null)
				return -1;
			return o1.getNome().compareTo(o2.getNome());
		}
    }
}
