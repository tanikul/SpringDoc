package com.java.doc.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.TypeSecret;

@Repository
public class TypeSecretDAOImpl implements TypeSecretDAO {

	private Session session = HibernateUtil.getSessionFactory().openSession();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypeSecret> listTypeSecret() {
		List<TypeSecret> list = session.createQuery("from TypeSecret where ACTIVE = 'Y'").list();
		return list;
	}
}
