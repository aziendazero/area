package it.tredi.auac;

import it.tredi.auac.bean.PostIt;

import java.util.Comparator;

public class PostItDataDecrescenteComparator implements Comparator<PostIt> {
	//Ritorno -1 se o1 e' minore di o2; 0 se o1 e' uguale a o2, 1 se o1 e' maggiore do o2 
	@Override
    public int compare(PostIt o1, PostIt o2) {
		if(o1.getDataOraForCompare() == null) {
			if(o2.getDataOraForCompare() == null)
				return 0;
			else
				return 1;
		} else {
			if(o2.getDataOraForCompare() == null)
				return -1;
			return - o1.getDataOraForCompare().compareTo(o2.getDataOraForCompare());
		}
    }
}
