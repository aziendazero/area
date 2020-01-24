
package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.BinaryAttachmentsAppl;

import org.springframework.stereotype.Repository;

@Repository("binaryAttachmentsApplDao")
public class BinaryAttachmentsApplDao extends AbstractJpaDao<BinaryAttachmentsAppl> {

	public BinaryAttachmentsApplDao() {
		super();
		setClazz(BinaryAttachmentsAppl.class);
	}

}