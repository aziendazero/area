package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.MraModel;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("mraModelDao")
public class MraModelDao extends AbstractJpaDao<MraModel> {
	public MraModelDao() {
		super();
		setClazz(MraModel.class);
	}
	
	//Restituisce l'unico MraModel se presente altrimenti restituisce null
	public MraModel getMraModelIfExist() {
		List<MraModel> mraModels = (List<MraModel>)this.findAll();
		if(mraModels != null && mraModels.size() > 0) {
			return mraModels.get(0);
		}
		return null;
	}
}
