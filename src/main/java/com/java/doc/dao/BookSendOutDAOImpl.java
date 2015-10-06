package com.java.doc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.BookSendOut;
import com.java.doc.util.TableSorter;

@Repository
public class BookSendOutDAOImpl implements BookSendOutDAO {

	private Session session = HibernateUtil.getSessionFactory().openSession();
	private int CountSelect=0;
	
	@Override
	public void save(BookSendOut sendout) {
		session.save(sendout);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BookSendOut> listSendOut() {
		List<BookSendOut> list = session.createQuery("from BookSendOut").list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BookSendOut> ListPageSendOut(TableSorter table) {
		int idx = table.getSortList().get(0).get(0);
		String order = (table.getSortList().get(0).get(1) > 0) ? "DESC" : "ASC";

		String[] column = {"bsYear", "bsRdate", "bsNum", "bsFrom", "bsPlace", "bsFrom", "bsTo", "bsSubject", "bsRemark"};
		Criteria query = session.createCriteria(BookSendOut.class);
		if(!table.getSearch().getFrom().trim().isEmpty()) query.add(Restrictions.like("bsFrom", "%" + table.getSearch().getFrom().trim() + "%"));
		if(!table.getSearch().getTo().trim().isEmpty()) query.add(Restrictions.like("bsTo", "%" + table.getSearch().getTo().trim() + "%"));
		if(!table.getSearch().getNum().trim().isEmpty()) query.add(Restrictions.like("bsNum", "%" + table.getSearch().getNum().trim() + "%"));
		if(!table.getSearch().getRemark().trim().isEmpty()) query.add(Restrictions.like("bsRemark", "%" + table.getSearch().getRemark().trim() + "%"));
		if(!table.getSearch().getSubject().trim().isEmpty()) query.add(Restrictions.like("bsSubject", "%" + table.getSearch().getSubject().trim() + "%"));
		if(!table.getSearch().getStartDate().trim().isEmpty()) query.add(Restrictions.ge("bsRdate", table.getSearch().getStartDate().trim()));
		if(!table.getSearch().getEndDate().trim().isEmpty()) query.add(Restrictions.le("bsDate", table.getSearch().getEndDate().trim()));
		if(order.equals("DESC")){
			query.addOrder(Order.desc(column[idx]));		
		}else{
			query.addOrder(Order.asc(column[idx]));
		}
		
		this.CountSelect = query.list().size();
		query.setFirstResult(1 + (table.getSize() * table.getPage()));
		query.setMaxResults(table.getSize());
		List<BookSendOut> list = query.list();
		return list;
	}

	@Override
	public int CountSelect() {
		return this.CountSelect;
	}

	@Override
	public int LastID() {
		BookSendOut query = (BookSendOut) session.createQuery("from BookSendOut order by bsId desc").setMaxResults(1).uniqueResult();
		return query.getBsId();
	}

}
