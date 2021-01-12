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
import com.java.doc.model.Boards;
import com.java.doc.model.Divisions;

@Repository("divisionDao")
public class DivisionDAOImpl implements DivisionDAO {

	//protected Session session;
    //protected Transaction tx;
    private static final Logger logger = Logger.getLogger(DivisionDAOImpl.class);
    public DivisionDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Divisions> listDivision() {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		List<Divisions> list = null;
		try{
			list = session.createQuery("from Divisions order by divisionCode asc").list();
		}catch(Exception ex){
			logger.error("listDivision : ", ex);
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
	
	/*protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }*/

	@Override
	public Divisions getDivisionByCode(String code) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		Divisions list = null;
		try{
			list = (Divisions) session.createQuery("from Divisions where divisionCode=?").setParameter(0, Integer.parseInt(code)).uniqueResult();
		}catch(Exception ex){
			logger.error("getDivisionByCode : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	private Session OpenSession() {
		Session session = HibernateUtil.openSession();
		return session;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boards> listBorad() {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		List<Boards> list = null;
		try{
			list = session.createQuery("from Boards order by id asc").list();
		}catch(Exception ex){
			logger.error("listBorad : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@Override
	public Boards getBoardById(Integer id) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		Boards list = null;
		try{
			list = (Boards) session.createQuery("from Boards where id=?").setParameter(0, id).uniqueResult();
		}catch(Exception ex){
			logger.error("getBoardById : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@Override
	public Map<Integer, String> selectBoard() {
		Iterator<Boards> itr = this.listBorad().iterator();
		Map<Integer, String> list = new HashMap<Integer, String>();
		while(itr.hasNext()){
			Boards d = itr.next();
			list.put(d.getId() , d.getBoardName());
		}
		Map<Integer, String> sortedMap = new TreeMap<Integer, String>(list);
		return sortedMap;
	}
}
