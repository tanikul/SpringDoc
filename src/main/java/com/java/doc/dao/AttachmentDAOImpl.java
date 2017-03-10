package com.java.doc.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.Attachment;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

	protected Session session;
    protected Transaction tx;
    private static final Logger logger = Logger.getLogger(AttachmentDAOImpl.class);
    public AttachmentDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Attachment> listAttachment(int objectId, String objectName) {
		startOperation();
		List<Attachment> list = new ArrayList<Attachment>();
		try {
			Criteria query = session.createCriteria(Attachment.class)
				.add(Restrictions.eq("objectId", objectId))
				.add(Restrictions.eq("objectName", objectName));
			list = query.list();
		} catch(Exception ex){
			logger.error("listAttachment : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}

	@Override
	public int save(Attachment attachment) {
		startOperation();
		try{
			session.save(attachment);
			tx.commit();
			return attachment.getAttachmentId();
		}catch(Exception ex){
			logger.error("save : ", ex);
			ex.printStackTrace();
			tx.rollback();
			return 0;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public Attachment getAttachment(int attachmentId) {
		startOperation();
		Attachment data = null;
		try{
			data = (Attachment) session.get(Attachment.class, attachmentId);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public boolean delete(int attachmentId) {
		try {
			Attachment attachment = this.getAttachment(attachmentId);
			startOperation();
			session.delete(attachment);
			tx.commit();
			return true;
		} catch (Exception ex) {
			logger.error("delete : ", ex);
			ex.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public void updateObjectId(String attachmentList, int objectId, String objectName) {
		startOperation();
		try {
			if(attachmentList != null && !"".equals(attachmentList)) {
				Query query = session.createQuery("update Attachment set objectId = :objectId where attachmentId in (" + attachmentList + ") and objectName = :objectName ");
				query.setParameter("objectId", objectId);
				query.setParameter("objectName", objectName);
				query.executeUpdate();
			}
		} catch(Exception ex){
			logger.error("updateObjectId : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
	}
	
	protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }

}
