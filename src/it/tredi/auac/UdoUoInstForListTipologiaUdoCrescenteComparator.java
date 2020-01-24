package it.tredi.auac;

import it.tredi.auac.bean.UdoUoInstForList;

import java.util.Comparator;

public class UdoUoInstForListTipologiaUdoCrescenteComparator extends UdoUoInstForListComparator implements Comparator<UdoUoInstForList> {
	//Ritorno -1 se o1 e' minore di o2; 0 se o1 e' ugulae a o2, 1 se o1 e' maggiore do o2 
	@Override
    public int compare(UdoUoInstForList o1, UdoUoInstForList o2) {
		if(o1.isUdo() && o2.isUdo()) {
			if(o1.getTipoUdoTemplDescr() == null) {
				if(o2.getTipoUdoTemplDescr() == null)
					return 0;
				else
					return -1;
			} else {
				//o1 non null
				if(o2.getTipoUdoTemplDescr() == null)
					return 1;
				return o1.getTipoUdoTemplDescr().compareTo(o2.getTipoUdoTemplDescr());			
			}
		}
		return this.compareIfNotAllUdo(o1, o2);
    }
}
