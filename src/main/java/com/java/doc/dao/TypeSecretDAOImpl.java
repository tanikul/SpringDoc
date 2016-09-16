package com.java.doc.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.TypeQuick;
import com.java.doc.model.TypeSecret;

@Repository
public class TypeSecretDAOImpl implements TypeSecretDAO {

	protected Session session;
    protected Transaction tx;
    private static final Logger logger = Logger.getLogger(TypeSecretDAOImpl.class);
    public TypeSecretDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypeSecret> listTypeSecret() throws Exception {
		startOperation();
		List<TypeSecret> list = null;
		try{
			list = session.createQuery("from TypeSecret where ACTIVE = 'Y'").list();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, String> SelectSecret() throws Exception {
		startOperation();
		HashMap<Integer, String> list = null;
		try{
			List<TypeQuick> type = session.createQuery("from TypeQuick where ACTIVE = 'Y'").list();
			Iterator<TypeQuick> itr = type.iterator();
			list = new HashMap<Integer, String>();
			while(itr.hasNext()){
				TypeQuick t = itr.next();
				list.put(t.getId(), t.getTypeQuick());
			}
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@Override
	public String getTypeSecretById(Integer id) throws Exception {
		startOperation();
		TypeSecret type = null;
		try{
			type = (TypeSecret) session.get(TypeSecret.class, id);
		}finally{
			HibernateUtil.close(session);
		}
		return type.getTypeSecret();
	}
	
	protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }
}
