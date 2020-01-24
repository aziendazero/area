package it.tredi.auac;

import it.tredi.auac.orm.entity.RequisitoTempl;

import java.util.Comparator;

public class RequisitoTemplNomeComparator implements Comparator<RequisitoTempl> {
	@Override
    public int compare(RequisitoTempl o1, RequisitoTempl o2) {
        return o1.getNome().compareTo(o2.getNome());
    }
}
