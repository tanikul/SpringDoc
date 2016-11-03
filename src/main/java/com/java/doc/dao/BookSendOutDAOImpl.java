package com.java.doc.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.util.TableSorter;
import com.mysql.jdbc.StringUtils;

@Repository
public class BookSendOutDAOImpl implements BookSendOutDAO {

	protected Session session;
    protected Transaction tx;
    public BookSendOutDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	private int CountSelect=0;
	private static final Logger logger = Logger.getLogger(BookSendOutDAOImpl.class);
	
	@Override
	public void save(BookSendOut sendout) {
		startOperation();
		try{
			session.save(sendout);
		}finally{
			HibernateUtil.close(session);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BookSendOut> listSendOut() {
		startOperation();
		List<BookSendOut> list = null;
		try{
			list = session.createQuery("from BookSendOut").list();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BookSendOutTable ListPageSendOut(TableSorter table) {
		startOperation();
		BookSendOutTable listTable = new BookSendOutTable();
		try {
			int idx = table.getSortList().get(0).get(0);
			String order = (table.getSortList().get(0).get(1) > 0) ? "DESC" : "ASC";
	
			String[] column = {"bsId", "bsYear", "bsRdate", "bsNum", "bsPlace", "bsDate" , "bsFrom", "bsTo", "bsSubject", "brTypeQuick", "brTypeSecret", "bsRemark"};
			Criteria query = session.createCriteria(BookSendOut.class);
			
			if(!StringUtils.isNullOrEmpty(table.getSearch().getFrom().trim())){
				query.add(Restrictions.like("bsFrom", "%" + table.getSearch().getFrom().trim() + "%"));
			}
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			if(!StringUtils.isNullOrEmpty(table.getSearch().getStartDate().trim()) && !StringUtils.isNullOrEmpty(table.getSearch().getEndDate().trim())){
				try {
					String[] arrStart = table.getSearch().getStartDate().trim().split("/");
					String[] arrEnd = table.getSearch().getEndDate().trim().split("/");
					String strStart = arrStart[0] + "-" + arrStart[1] + "-" + (Integer.parseInt(arrStart[2]) - 543);
					String strEnd = arrEnd[0] + "-" + arrEnd[1] + "-" + (Integer.parseInt(arrEnd[2]) - 543);
					Date fromDate = df.parse(strStart);
					Date endDate = df.parse(strEnd);
					query.add(Restrictions.between("bsRdate", fromDate, endDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getFrom().trim())) query.add(Restrictions.like("bsFrom", "%" + table.getSearch().getFrom().trim() + "%"));
			if(!StringUtils.isNullOrEmpty(table.getSearch().getTo().trim())) query.add(Restrictions.like("bsTo", "%" + table.getSearch().getTo().trim() + "%"));
			if(!StringUtils.isNullOrEmpty(table.getSearch().getNum().trim())) query.add(Restrictions.like("bsPlace", "%" + table.getSearch().getNum().trim() + "%"));
			if(!StringUtils.isNullOrEmpty(table.getSearch().getRemark().trim())) query.add(Restrictions.like("bsRemark", "%" + table.getSearch().getRemark().trim() + "%"));
			if(!StringUtils.isNullOrEmpty(table.getSearch().getSubject().trim())) query.add(Restrictions.like("bsSubject", "%" + table.getSearch().getSubject().trim() + "%"));
			//if(!table.getRole().equals("ADMIN")){
				//query.add(Restrictions.eq("division", table.getDivision()));
				//query.add(Restrictions.like("brPlace", "%" + table.getDivision() + "%"));
			//}
			
			
			int year = Calendar.getInstance(Locale.US).get(Calendar.YEAR);
			if(StringUtils.isNullOrEmpty(table.getSearch().getYear()) && StringUtils.isNullOrEmpty(table.getSearch().getStartDate()) && StringUtils.isNullOrEmpty(table.getSearch().getEndDate())){
				query.add(Restrictions.eq("bsYear", year + 543));
			}else{
				if(table.getSearch().getYear() != null && !"".equals(table.getSearch().getYear()) && !table.getSearch().getYear().equals("ALL")) query.add(Restrictions.eq("bsYear", Integer.valueOf(table.getSearch().getYear().trim())));
			}
			
			if(order.equals("DESC")){
				query.addOrder(Order.desc(column[idx]));	
			}else{
				query.addOrder(Order.asc(column[idx]));
			}
			this.CountSelect = query.list().size();
			listTable.setCountSelect(query.list().size());
			listTable.setSendoutListReport(query.list());
			query.setFirstResult(table.getSize() * table.getPage());
			query.setMaxResults(table.getSize());
			listTable.setSendoutList(query.list());
		} catch(Exception ex){
			logger.error("ListPageSendOut : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return listTable;
	}

	@Override
	public int CountSelect() {
		return this.CountSelect;
	}

	@Override
	public int LastID() {
		startOperation();
		BookSendOut query = null;
		try{
			query = (BookSendOut) session.createQuery("from BookSendOut order by bsId desc").setMaxResults(1).uniqueResult();
		}finally{
			HibernateUtil.close(session);
		}
		return query.getBsId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getYear() {
		startOperation();
		Criteria criteria = null;
		List<Integer> years = new ArrayList<Integer>();
		try {
			criteria = session.createCriteria(BookSendOut.class);
			criteria.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("bsYear"))));
			criteria.add(Restrictions.isNotNull("bsYear"));
			criteria.add(Restrictions.gt("bsYear", new Integer(0)));
			criteria.addOrder(Order.desc("bsYear"));
			years = criteria.list();
		}finally{
			HibernateUtil.close(session);
		}
		return years;
	}

	@Override
	public boolean saveOrUpdate(BookSendOut recive) {
		startOperation();
		try{
			session.saveOrUpdate(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			logger.error("saveOrUpdate : ", ex);
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public boolean merge(BookSendOut recive) {
		startOperation();
		try{
			session.merge(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			logger.error("merge : ", ex);
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public Integer getNextBsNum(int bsYear) {
		startOperation();
		Integer bsNum = 1;
		try {
			Query query = session.createQuery("from BookSendOut where bsYear = :bsYear order by bsNum desc");
			query.setParameter("bsYear", bsYear);
			BookSendOut bookSendOut = (BookSendOut) query.list().get(0);
			bsNum = bookSendOut.getBsNum() + 1;
		} catch (Exception ex) {
			logger.error("getNextBsNum : ", ex);
			bsNum = 1;
		}finally{
			HibernateUtil.close(session);
		}
		return bsNum;
	}

	@Override
	public BookSendOut getDataFromId(int id) {
		startOperation();
		BookSendOut data = null;
		try{
			data = (BookSendOut) session.get(BookSendOut.class, id);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public String delete(int id) {
		startOperation();
		String rs = "0";
		try{
			Query query = session.createQuery("delete BookSendOut where bsId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete Attachment where objectId = :ID and objectName = 'BOOK_SEND_OUT'");
			query.setParameter("ID", id);
			query.executeUpdate();
			rs = "1";
		} catch (Exception ex) {
			logger.error("delete : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }

}
