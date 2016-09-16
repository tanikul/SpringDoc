package com.java.doc.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.Divisions;

@Repository
public class DivisionDAOImpl implements DivisionDAO {

	protected Session session;
    protected Transaction tx;
    private static final Logger logger = Logger.getLogger(DivisionDAOImpl.class);
    public DivisionDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Divisions> listDivision() {
		startOperation();
		List<Divisions> list = null;
		try{
			list = session.createQuery("from Divisions order by divisionCode asc").list();
		}catch(Exception ex){
			logger.error("Save : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}

	@Override
	public Map<Integer, String> selectDivision() {
		Iterator<Divisions> itr = this.listDivision().iterator();
		Map<Integer, String> list = new HashMap<Integer, String>();
		while(itr.hasNext()){
			Divisions d = itr.next();
			list.put(d.getDivisionCode() , d.getDivisionCode() + " - " + d.getDivisionName());
		}
		Map<Integer, String> sortedMap = new TreeMap<Integer, String>(list);
		return sortedMap;
	}
	
	protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }
}
