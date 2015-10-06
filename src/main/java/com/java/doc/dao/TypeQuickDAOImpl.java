package com.java.doc.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.TypeQuick;

@Repository
public class TypeQuickDAOImpl implements TypeQuickDAO {
	
	private Session session = HibernateUtil.getSessionFactory().openSession();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypeQuick> listTypeQuick() {
		List<TypeQuick> type = session.createQuery("from TypeQuick where ACTIVE = 'Y'").list();
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, String> SelectQuick() {
		List<TypeQuick> type = session.createQuery("from TypeQuick where ACTIVE = 'Y'").list();
		Iterator<TypeQuick> itr = type.iterator();
		HashMap<Integer, String> list = new HashMap<Integer, String>();
		while(itr.hasNext()){
			TypeQuick t = itr.next();
			list.put(t.getId(), t.getTypeQuick());
		}
		return list;
	}
}
