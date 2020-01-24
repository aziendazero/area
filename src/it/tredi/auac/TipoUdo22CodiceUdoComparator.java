package it.tredi.auac;

import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.util.Comparator;

public class TipoUdo22CodiceUdoComparator implements Comparator<TipoUdo22Templ> {
	@Override
    public int compare(TipoUdo22Templ o1, TipoUdo22Templ o2) {
        return o1.getCodiceUdo().compareTo(o2.getCodiceUdo());
    }
}
