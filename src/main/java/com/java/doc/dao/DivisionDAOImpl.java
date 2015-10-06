package com.java.doc.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.Divisions;

@Repository
public class DivisionDAOImpl implements DivisionDAO {

	private Session session = HibernateUtil.getSessionFactory().openSession();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Divisions> listDivision() {
		List<Divisions> list = session.createQuery("from Divisions order by id asc").list();
		return list;
	}

	@Override
	public Map<String, String> selectDivision() {
		Iterator<Divisions> itr = this.listDivision().iterator();
		Map<String, String> list = new HashMap<String, String>();
		while(itr.hasNext()){
			Divisions d = itr.next();
			list.put(d.getDivisionCode(), d.getDivisionCode() + " - " + d.getDivisionName());
		}
		return list;
	}

}
