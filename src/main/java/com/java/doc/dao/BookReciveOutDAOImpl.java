package com.java.doc.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.util.TableSorter;
import com.mysql.jdbc.StringUtils;

@Repository
public class BookReciveOutDAOImpl implements BookReciveOutDAO {

	protected Session session;
    protected Transaction tx;
    public BookReciveOutDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	private static final Logger logger = Logger.getLogger(BookReciveOutDAOImpl.class);
	private int CountSelect;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BookReciveOut> listRecive() {
		startOperation();
		List<BookReciveOut> list = null;
		try {
			list = session.createQuery("from BookReciveOut").list();
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}

	@Override
	public long Count() {
		startOperation();
		Long d = null;
		try {
			d = (Long) session.createCriteria(BookReciveOut.class).setProjection(Projections.rowCount()).uniqueResult();
		}finally{
			HibernateUtil.close(session);
		}
		return d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BookReciveOutTable ListPageRecive(TableSorter table) {
		startOperation();
		BookReciveOutTable bookReciveOutTable = new BookReciveOutTable();
		try {
			int idx = table.getSortList().get(0).get(0);
			String order = (table.getSortList().get(0).get(1) > 0) ? "DESC" : "ASC";
			String[] column = {"brId", "brYear", "brRdate", "brNum", "brPlace", "brDate", "brFrom", "brTo", "brSubject", "brTypeQuick", "brTypeSecret", "brRemark"};
			Criteria query = session.createCriteria(BookReciveOut.class);
			
			if(!StringUtils.isNullOrEmpty(table.getSearch().getFrom().trim())){
				query.add(Restrictions.like("brFrom", "%" + table.getSearch().getFrom().trim() + "%"));
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
					query.add(Restrictions.between("brRdate", fromDate, endDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(!StringUtils.isNullOrEmpty(table.getSearch().getTo().trim())){
				query.add(Restrictions.like("brTo", "%" + table.getSearch().getTo().trim() + "%"));
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getNum().trim())){
				query.add(Restrictions.like("brPlace", "%" + table.getSearch().getNum().trim() + "%"));
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getRemark().trim())){
				query.add(Restrictions.like("brRemark", "%" + table.getSearch().getRemark().trim() + "%"));
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getSubject().trim())){
				query.add(Restrictions.like("brSubject", "%" + table.getSearch().getSubject().trim() + "%"));
			}
			if(!table.getRole().equals("ADMIN")){
				//query.add(Restrictions.eq("division", table.getDivision()));
				query.add(Restrictions.like("brPlace", "%" + table.getDivision() + "%"));
			}
			int year = Calendar.getInstance(Locale.US).get(Calendar.YEAR);
			if(StringUtils.isNullOrEmpty(table.getSearch().getYear()) && StringUtils.isNullOrEmpty(table.getSearch().getStartDate()) && StringUtils.isNullOrEmpty(table.getSearch().getEndDate())){
				query.add(Restrictions.eq("brYear", year + 543));
			}else{
				if(table.getSearch().getYear() != null && !"".equals(table.getSearch().getYear()) && !table.getSearch().getYear().equals("ALL")){
					query.add(Restrictions.eq("brYear", Integer.valueOf(table.getSearch().getYear().trim())));
				}
			}
			
			if(order.equals("DESC")){
				query.addOrder(Order.desc(column[idx]));
			}else{
				query.addOrder(Order.asc(column[idx]));
			}
			bookReciveOutTable.setCountSelect(query.list().size());
			bookReciveOutTable.setSendoutListReport(query.list());
			query.setFirstResult(table.getSize() * table.getPage());
			query.setMaxResults(table.getSize());
			bookReciveOutTable.setSendoutList(query.list());
		} catch (Exception ex){
			logger.error("ListPageRecive : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return bookReciveOutTable;
	}

	@Override
	public int CountSelect() {
		return this.CountSelect;
	}

	@Override
	public boolean Save(BookReciveOut recive) {
		startOperation();
		try{
			session.save(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			logger.error("Save : ", ex);
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public JRDataSource getDataRecivePDF() {
		startOperation();
		List<BookReciveOut> items = new ArrayList<BookReciveOut>();
		int j = 0;
		for(BookReciveOut i : this.listRecive()){
			items.add(i);
			if(j == 100) break;
			j++;
		}
		JRDataSource ds = new JRBeanCollectionDataSource(items); 
		return ds;
	}

	@Override
	public BookReciveOut getDataFromId(int id) {
		startOperation();
		BookReciveOut data = null;
		try{
			data = (BookReciveOut) session.get(BookReciveOut.class, id);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public int LastID() {
		BookReciveOut query = null;
		try{
			startOperation();
			query = (BookReciveOut) session.createQuery("from BookReciveOut order by brId desc").setMaxResults(1).uniqueResult();
		}catch(Exception ex){
			logger.error("saveOrUpdate : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return query.getBrId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getYear() throws HibernateException {
		startOperation();
		Criteria criteria = null;
		List<Integer> s = new ArrayList<Integer>();
		try{
			//session = HibernateUtil.getSessionFactory().openSession();
			criteria = session.createCriteria(BookReciveOut.class);
			criteria.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("brYear"))));
			criteria.add(Restrictions.isNotNull("brYear"));
			criteria.add(Restrictions.gt("brYear", new Integer(0)));
			criteria.addOrder(Order.desc("brYear"));
			s = criteria.list();
		}catch(Exception ex){
			logger.error("getYear : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return s;
	}

	@Override
	public boolean saveOrUpdate(BookReciveOut recive) {
		//session = HibernateUtil.getSessionFactory().openSession();
		startOperation();
		try{
			session.saveOrUpdate(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			logger.error("saveOrUpdate : ", ex);
			ex.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public boolean merge(BookReciveOut recive) {
		//session = HibernateUtil.getSessionFactory().openSession();
		startOperation();
		try{
			session.merge(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			logger.error("merge : ", ex);
			ex.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			HibernateUtil.close(session);
		}
	}

	@Override
	public Integer getNextBrNum(int brYear) {
		//session = HibernateUtil.getSessionFactory().openSession();
		Integer brNum = 1;
		startOperation();
		try {
			Query query = session.createQuery("from BookReciveOut where brYear = :brYear order by brNum desc");
			query.setParameter("brYear", brYear);
			BookReciveOut bookReciveOut = (BookReciveOut) query.list().get(0);
			brNum = bookReciveOut.getBrNum() + 1;
		} catch (Exception ex) { 
			logger.error("getNextBrNum : ", ex);
			brNum = 1;
		}finally{
			HibernateUtil.close(session);
		}
		return brNum;
	}

	@Override
	public String delete(int id) {
		startOperation();
		String rs = "0";
		try{
			Query query = session.createQuery("delete BookReciveOut where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete Attachment where objectId = :ID and objectName = 'BOOK_RECIVE_OUT'");
			query.setParameter("ID", id);
			query.executeUpdate();
			rs = "1";
		} catch (Exception ex) {
			logger.error("delete : ", ex);
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
