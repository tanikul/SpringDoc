package com.java.doc.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.BookReciveOut;
import com.java.doc.util.TableSorter;

@Repository
public class BookReciveOutDAOImpl implements BookReciveOutDAO {

	private Session session = HibernateUtil.getSessionFactory().openSession();
	private int CountSelect;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BookReciveOut> listRecive() {
		List<BookReciveOut> list = session.createQuery("from BookReciveOut").list();
		return list;
	}

	@Override
	public long Count() {
		return (Long) session.createCriteria(BookReciveOut.class).setProjection(Projections.rowCount()).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BookReciveOut> ListPageRecive(TableSorter table) {
		int idx = table.getSortList().get(0).get(0);
		String order = (table.getSortList().get(0).get(1) > 0) ? "DESC" : "ASC";
		String[] column = {"brYear", "brRdate", "brNum", "brFrom", "brPlace", "brFrom", "brTo", "brSubject", "brRemark"};
		Criteria query = session.createCriteria(BookReciveOut.class);
		if(!table.getSearch().getFrom().trim().isEmpty()) query.add(Restrictions.like("brFrom", "%" + table.getSearch().getFrom().trim() + "%"));
		if(!table.getSearch().getTo().trim().isEmpty()) query.add(Restrictions.like("brTo", "%" + table.getSearch().getTo().trim() + "%"));
		if(!table.getSearch().getNum().trim().isEmpty()) query.add(Restrictions.like("brNum", "%" + table.getSearch().getNum().trim() + "%"));
		if(!table.getSearch().getRemark().trim().isEmpty()) query.add(Restrictions.like("brRemark", "%" + table.getSearch().getRemark().trim() + "%"));
		if(!table.getSearch().getSubject().trim().isEmpty()) query.add(Restrictions.like("brSubject", "%" + table.getSearch().getSubject().trim() + "%"));
		if(!table.getSearch().getStartDate().trim().isEmpty()) query.add(Restrictions.ge("brRdate", table.getSearch().getStartDate().trim()));
		if(!table.getSearch().getEndDate().trim().isEmpty()) query.add(Restrictions.le("brDate", table.getSearch().getEndDate().trim()));
		if(order.equals("DESC")){
			query.addOrder(Order.desc(column[idx]));		
		}else{
			query.addOrder(Order.asc(column[idx]));
		}
		CountSelect = query.list().size();
		query.setFirstResult(table.getSize() * table.getPage());
		query.setMaxResults(table.getSize());
		List<BookReciveOut> list = query.list();
		return list;
	}

	@Override
	public int CountSelect() {
		return this.CountSelect;
	}

	@Override
	public boolean Save(BookReciveOut recive) {
		Transaction tx = session.beginTransaction();
		try{
			session.save(recive);
			tx.commit();
			return true;
		}catch(Exception ex){
			tx.rollback();
			return false;
		}
	}

	@Override
	public JRDataSource getDataRecivePDF() {
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
		BookReciveOut data = (BookReciveOut) session.get(BookReciveOut.class, id);
		return data;
	}

	@Override
	public int LastID() {
		BookReciveOut query = (BookReciveOut) session.createQuery("from BookReciveOut order by brId desc").setMaxResults(1).uniqueResult();
		return query.getBrId();
	}
}
