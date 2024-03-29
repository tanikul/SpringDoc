package com.java.doc.dao;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import com.java.doc.model.BookRecieveBoard;
import com.java.doc.model.BookRecieveDepartment;
import com.java.doc.model.BookRecieveGroup;
import com.java.doc.model.BookRecieveSection;
import com.java.doc.model.BookRecieveUser;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.model.Sections;
import com.java.doc.model.StatusDetail;
import com.java.doc.model.Users;
import com.java.doc.util.TableSorter;
import com.mysql.jdbc.StringUtils;

@Repository("bookReciveOutDao")
public class BookReciveOutDAOImpl implements BookReciveOutDAO {

	
    public BookReciveOutDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	private static final Logger logger = Logger.getLogger(BookReciveOutDAOImpl.class);
	private int CountSelect;

	@Override
	public List<BookReciveOut> listReciveByYear(int year) {
		Session session = OpenSession();
		List<BookReciveOut> list = null;
		try {
			String sql = "SELECT a.BR_ID,";
			sql	+= "a.BR_NUM,";
			sql += "a.BR_YEAR,";
			sql += "a.BR_RDATE,";
			sql += "a.BR_TYPE_QUICK,";
			sql += "a.BR_TYPE_SECRET,";
			sql += "a.BR_PLACE,";
			sql += "a.BR_DATE,";
			sql += "a.BR_FROM,";
			sql += "a.BR_TO,";
			sql += "a.BR_SUBJECT,";
			sql += "a.BR_REMARK,";
			sql += "a.BR_DIVISION,";
			sql += "a.BR_PCODE,";
			sql += "a.BR_STATUS,";
			sql += "a.BR_IMAGE,";
			sql += "a.CREATED_BY,";
			sql += "a.CREATED_DATE,";
			sql += "a.DIVISION,";
			sql += "a.UPDATED_BY,";
			sql += "a.UPDATED_DATE,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT SEPARATOR ',') BR_TO_DEPARTMENT, ";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_SHORT SEPARATOR ',') BR_TO_DEPARTMENT_SHORT,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_NAME SEPARATOR ',') BR_TO_DEPARTMENT_NAME, ";
			sql += "NULL BR_TO_GROUP, NULL BR_TO_GROUP_NAME, NULL BR_TO_USER, NULL BR_TO_USER_NAME";
			sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id ";
			sql += " WHERE a.BR_YEAR = '" + year + "'";
			sql += " GROUP BY a.BR_ID ORDER BY a.BR_ID ASC";
			list = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();			
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@Override
	public List<BookReciveOut> listReciveByYearAndBrNum(int year, int brNum) {
		Session session = OpenSession();
		List<BookReciveOut> list = null;
		try {
			String sql = "SELECT a.BR_ID,";
			sql	+= "a.BR_NUM,";
			sql += "a.BR_YEAR,";
			sql += "a.BR_RDATE,";
			sql += "a.BR_TYPE_QUICK,";
			sql += "a.BR_TYPE_SECRET,";
			sql += "a.BR_PLACE,";
			sql += "a.BR_DATE,";
			sql += "a.BR_FROM,";
			sql += "a.BR_TO,";
			sql += "a.BR_SUBJECT,";
			sql += "a.BR_REMARK,";
			sql += "a.BR_DIVISION,";
			sql += "a.BR_PCODE,";
			sql += "a.BR_STATUS,";
			sql += "a.BR_IMAGE,";
			sql += "a.CREATED_BY,";
			sql += "a.CREATED_DATE,";
			sql += "a.DIVISION,";
			sql += "a.UPDATED_BY,";
			sql += "a.UPDATED_DATE,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT SEPARATOR ',') BR_TO_DEPARTMENT, ";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_SHORT SEPARATOR ',') BR_TO_DEPARTMENT_SHORT,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_NAME SEPARATOR ',') BR_TO_DEPARTMENT_NAME, ";
			sql += "NULL BR_TO_GROUP, NULL BR_TO_GROUP_NAME, NULL BR_TO_USER, NULL BR_TO_USER_NAME";
			sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id ";
			sql += " WHERE a.BR_YEAR = '" + year + "'";
			sql += " AND a.BR_NUM > " + brNum;
			sql += " GROUP BY a.BR_ID ORDER BY a.BR_ID ASC";
			list = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();			
		}finally{
			HibernateUtil.close(session);
		}
		return list;
	}
	
	@Override
	public long Count() {
		Session session = OpenSession();
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
		Session session = OpenSession();
		BookReciveOutTable bookReciveOutTable = new BookReciveOutTable();
		try {
			int idx = table.getSortList().get(0).get(0);
			String order = (table.getSortList().get(0).get(1) > 0) ? "DESC" : "ASC";
			String[] column = {"a.br_Id", "a.br_Year", "a.br_Rdate", "a.br_Num", "a.br_Place", "a.br_Date", "a.br_From", "b.br_To_Department_Name", "b.br_To_Group_Name", "b.br_To_User_Name", "a.br_Subject", "a.br_Type_Quick", "a.br_Type_Secret", "a.br_Remark"};
			
			String sql = "SELECT a.BR_ID,";
			sql	+= "a.BR_NUM,";
			sql += "a.BR_YEAR,";
			sql += "a.BR_RDATE,";
			sql += "a.BR_TYPE_QUICK,";
			sql += "a.BR_TYPE_SECRET,";
			sql += "a.BR_PLACE,";
			sql += "a.BR_DATE,";
			sql += "a.BR_FROM,";
			sql += "a.BR_TO,";
			if(table.getRole().equals("ADMIN")) {
				sql += " (CASE WHEN GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ', ') IS NULL ";
				sql += " THEN GROUP_CONCAT(DISTINCT bo.BR_TO_BOARD_NAME ORDER BY bo.BR_BOARD_ID ASC SEPARATOR ', ') ";
				sql += " ELSE GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ', ') END) BR_TO_DEPARTMENT, ";
			}else {
				sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ', ') BR_TO_DEPARTMENT,";
			}
			sql += "GROUP_CONCAT(DISTINCT c.BR_TO_GROUP ORDER BY c.BR_TO_GROUP ASC SEPARATOR ', ') BR_TO_GROUP,";
			sql += "GROUP_CONCAT(DISTINCT d.BR_TO_USER ORDER BY d.BR_TO_USER ASC SEPARATOR ', ') BR_TO_USER,";
			sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT_SHORT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ', ') BR_TO_DEPARTMENT_SHORT,";
			sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT_NAME ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ', ') BR_TO_DEPARTMENT_NAME,";
			sql += "GROUP_CONCAT(DISTINCT c.BR_TO_GROUP_NAME ORDER BY c.BR_TO_GROUP ASC SEPARATOR ', ') BR_TO_GROUP_NAME,";
			sql += "GROUP_CONCAT(DISTINCT d.BR_TO_USER_NAME ORDER BY d.BR_TO_USER ASC SEPARATOR ', ') BR_TO_USER_NAME,";
			sql	+= "GROUP_CONCAT(DISTINCT b.RUNNING_NO ORDER BY b.RUNNING_NO ASC SEPARATOR ', ') RUNNING_NO_DEPARTMENT, ";
			sql += "a.BR_SUBJECT,";
			sql += "a.BR_REMARK,";
			sql += "a.BR_DIVISION,";
			sql += "a.BR_PCODE,";
			if(table.getRole().equals("USER")) {
				sql += "d.STATUS BR_STATUS,";
			}else if(table.getRole().equals("BOARD")) {
				sql += "bo.STATUS BR_STATUS,";
			}else {
				sql += "a.BR_STATUS,";
			}
			sql += "a.BR_IMAGE,";
			sql += "a.CREATED_BY,";
			sql += "a.CREATED_DATE,";
			sql += "a.DIVISION,";
			sql += "a.UPDATED_BY,";
			sql += "a.UPDATED_DATE,";
			sql += "a.BR_TO_SECTION,";
			sql += "a.BR_TO_SECTION_NAME, ";
			sql += " NULL CNT_CHILD, ";
			sql += " NULL BR_TO_BOARD, ";
			sql += " NULL BR_REMARK_DEPARTMENT, NULL BR_REMARK_GROUP, NULL BR_REMARK_SECTION, NULL BR_REMARK_USER, NULL BR_REMARK_BOARD";
			sql += " FROM BOOK_RECIVE_OUT a ";
			if(table.getRole().equals("BOARD")){
				sql += " INNER JOIN BOOK_RECIEVE_BOARD bo ON a.br_id = bo.br_id AND bo.BR_TO_BOARD = " + table.getBoardId();
			}
			sql += " LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id ";
			
			if(table.getRole().equals("GROUP")) {
				sql += " INNER JOIN BOOK_RECIEVE_GROUP c ON b.br_id = c.br_id AND b.BR_TO_DEPARTMENT = c.BR_TO_DEPARTMENT"; 
			}else {
				sql += " LEFT JOIN BOOK_RECIEVE_GROUP c ON b.br_id = c.br_id AND b.BR_TO_DEPARTMENT = c.BR_TO_DEPARTMENT"; 
			}
			if(table.getRole().equals("GROUP") && table.getSectionId() != 0) {
				sql += " INNER JOIN BOOK_RECIEVE_SECTION s ON s.br_id = b.BR_ID AND s.BR_TO_GROUP = c.BR_TO_GROUP AND s.BR_TO_SECTION = " + table.getSectionId();
				sql += " LEFT JOIN BOOK_RECIEVE_USER d ON c.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP AND d.BR_TO_SECTION  = s.BR_TO_SECTION";
			}
			
			if(table.getRole().equals("USER") && table.getSectionId() != 0) {
				sql += " LEFT JOIN BOOK_RECIEVE_SECTION s ON s.br_id = b.BR_ID AND s.BR_TO_GROUP = c.BR_TO_GROUP AND s.BR_TO_SECTION = " + table.getSectionId();
				sql += " LEFT JOIN BOOK_RECIEVE_USER d ON c.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP AND (d.BR_TO_SECTION  = s.BR_TO_SECTION OR d.BR_TO_SECTION IS NULL)";
			}else if(table.getRole().equals("USER")) {
				sql += " INNER JOIN BOOK_RECIEVE_USER d ON c.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP";
				
			}else if(!(table.getRole().equals("GROUP") && table.getSectionId() != 0)){
				sql += " LEFT JOIN BOOK_RECIEVE_USER d ON c.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP";
			} 
			if(table.getRole().equals("ADMIN")) {
				sql += " LEFT JOIN BOOK_RECIEVE_BOARD bo ON a.BR_ID = bo.BR_ID ";
			}
			List<String> where = new ArrayList<String>();
			if(!StringUtils.isNullOrEmpty(table.getSearch().getFrom().trim())){
				where.add("a.BR_FROM LIKE '%" + table.getSearch().getFrom().trim() + "%'");
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
					where.add("a.BR_RDATE between STR_TO_DATE('" + strStart + "', '%d-%m-%Y') and STR_TO_DATE('" + strEnd + "', '%d-%m-%Y')");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(!StringUtils.isNullOrEmpty(table.getSearch().getTo().trim())){
				where.add("(b.br_To_Department_Name LIKE '%" + table.getSearch().getTo().trim() + "' or d.br_To_User_Name LIKE '%" + table.getSearch().getTo().trim() + "%' or c.br_To_Group_Name LIKE '%" + table.getSearch().getTo().trim() + "%' or b.br_To_Department_Short LIKE '%" + table.getSearch().getTo().trim() + "%')");
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getNum().trim())){
				where.add("a.br_Place LIKE '%" + table.getSearch().getNum().trim() + "%'");
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getRemark().trim())){
				where.add("a.br_Remark LIKE '%" + table.getSearch().getRemark().trim() + "%'");
			}
			if(!StringUtils.isNullOrEmpty(table.getSearch().getSubject().trim())){
				where.add("a.br_Subject LIKE '%" + table.getSearch().getSubject().trim() + "%'");
			}
			if(table.getRole().equals("DEPARTMENT")){
				where.add("b.br_To_Department = '" + table.getDivision() + "'");
			}else if(table.getRole().equals("GROUP")){
				where.add("c.br_To_Group = '" + table.getGroupId() + "'");
			}else if(table.getRole().equals("USER")){
				where.add("d.br_To_User = '" + table.getUserId() + "'");
			}
			int year = Calendar.getInstance(Locale.US).get(Calendar.YEAR);
			if(StringUtils.isNullOrEmpty(table.getSearch().getYear()) && StringUtils.isNullOrEmpty(table.getSearch().getStartDate()) && StringUtils.isNullOrEmpty(table.getSearch().getEndDate())){
				year = year + 543;
				where.add("a.br_Year = '" + year + "'");
			}else{
				if(table.getSearch().getYear() != null && !"".equals(table.getSearch().getYear()) && !table.getSearch().getYear().equals("ALL")){
					where.add("a.br_Year = '" + table.getSearch().getYear().trim() + "'");
				}
			}
			if(where.size() > 0){
				sql += " WHERE ";
				for(String item : where){
					sql += item + " AND ";
				}
				sql = sql.substring(0, sql.length() - 4);
			}
			sql += " GROUP BY a.br_id ";
			if(table.getRole().equals("USER")) {
				sql += " , d.STATUS";
			}else if(table.getRole().equals("BOARD")) {
				sql += " , bo.STATUS";
			}
			List<BookReciveOut> book = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();
			bookReciveOutTable.setCountSelect(book.size());
			bookReciveOutTable.setSendoutListReport(book);
			if(order.equals("DESC")){
				sql += " ORDER BY " + column[idx] + " DESC";
			}else{
				sql += " ORDER BY " + column[idx] + " ASC";
			}
			sql += " LIMIT " + table.getSize() + " OFFSET " + table.getSize() * table.getPage();
			book = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();
			bookReciveOutTable.setSendoutList(book);
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
	public int Save(BookReciveOut recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		BigInteger result = new BigInteger("0");
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIVE_OUT (BR_NUM, BR_YEAR, BR_RDATE, BR_TYPE_QUICK, BR_TYPE_SECRET, BR_PLACE, "
					+ "BR_DATE, BR_FROM, BR_TO, BR_SUBJECT, BR_REMARK, CREATED_BY, CREATED_DATE) VALUES (:BR_NUM, :BR_YEAR, :BR_RDATE, :BR_TYPE_QUICK, :BR_TYPE_SECRET, :BR_PLACE, "
					+ ":BR_DATE, :BR_FROM, :BR_TO, :BR_SUBJECT, :BR_REMARK, :CREATED_BY, SYSDATE())");
			query.setParameter("BR_NUM", recive.getBrNum());
			query.setParameter("BR_YEAR", recive.getBrYear());
			query.setParameter("BR_RDATE", recive.getBrRdate());
			query.setParameter("BR_TYPE_QUICK", recive.getBrTypeQuick());
			query.setParameter("BR_TYPE_SECRET", recive.getBrTypeSecret());
			query.setParameter("BR_PLACE", recive.getBrPlace());
			query.setParameter("BR_DATE", recive.getBrDate());
			query.setParameter("BR_FROM", recive.getBrFrom());
			query.setParameter("BR_TO", recive.getBrTo());
			query.setParameter("BR_SUBJECT", recive.getBrSubject());
			query.setParameter("BR_REMARK", recive.getBrRemark());
			query.setParameter("CREATED_BY", recive.getCreatedBy());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("Save : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}

	@Override
	public BookReciveOut getDataFromId(int brId, Users user) {
		Session session = OpenSession();
		List<BookReciveOut> data = new ArrayList<BookReciveOut>();
		BookReciveOut result = new BookReciveOut();
		try{
			String sql = "";
			sql	+= "SELECT  x.* ";
			if(user.getRole().equals("ADMIN")) {
			
				sql	+= ", (SELECT  GROUP_CONCAT(s.xx SEPARATOR ',') CNT_CHILD FROM (";
				sql	+= "SELECT count(f.BR_TO_GROUP) xx FROM (SELECT BR_TO_DEPARTMENT FROM BOOK_RECIEVE_DEPARTMENT WHERE BR_ID = " + brId + " ORDER BY BR_TO_DEPARTMENT ASC) a ";
				sql	+= "LEFT JOIN BOOK_RECIEVE_GROUP f ON f.BR_TO_DEPARTMENT = a.BR_TO_DEPARTMENT  AND f.BR_ID = " + brId + " ";
				sql	+= "GROUP BY a.BR_TO_DEPARTMENT ) s) CNT_CHILD ";
			}else {
				sql	+= ", NULL CNT_CHILD ";
			}
			sql	+= "FROM (";
					
			sql += "SELECT a.BR_ID,";
			sql	+= "a.BR_NUM,";
			sql += "a.BR_YEAR,";
			sql += "a.BR_RDATE,";
			sql += "a.BR_TYPE_QUICK,";
			sql += "a.BR_TYPE_SECRET,";
			sql += "a.BR_PLACE,";
			sql += "a.BR_DATE,";
			sql += "a.BR_FROM,";
			sql += "a.BR_TO,";
			sql += "a.BR_SUBJECT,";
			sql += "a.BR_REMARK,";
			sql += "a.BR_DIVISION,";
			sql += "a.BR_PCODE,";
			
			sql += "a.BR_IMAGE,";
			sql += "a.CREATED_BY,";
			sql += "a.CREATED_DATE,";
			sql += "a.DIVISION,";
			sql += "a.UPDATED_BY,";
			sql += "a.UPDATED_DATE,";
			
			if(user.getRole().equals("ADMIN") || user.getRole().equals("BOARD")){
				if(user.getRole().equals("BOARD")) {
					sql += "bo.STATUS BR_STATUS,";
				}else {
					sql += "a.BR_STATUS,";
				}
				
				sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ',') BR_TO_DEPARTMENT, ";
				sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT_SHORT ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ',') BR_TO_DEPARTMENT_SHORT,";
				sql += "GROUP_CONCAT(DISTINCT b.BR_TO_DEPARTMENT_NAME ORDER BY b.BR_TO_DEPARTMENT ASC SEPARATOR ',') BR_TO_DEPARTMENT_NAME, ";
				sql += "NULL BR_TO_GROUP_NAME, NULL BR_TO_SECTION_NAME, NULL BR_TO_USER_NAME,";
				sql += "GROUP_CONCAT(DISTINCT d.BR_TO_USER ORDER BY d.BR_TO_USER ASC SEPARATOR ',') BR_TO_USER,";
				sql += "GROUP_CONCAT(DISTINCT c.BR_TO_GROUP ORDER BY c.BR_TO_GROUP ASC SEPARATOR ',') BR_TO_GROUP,"; 
				sql += "GROUP_CONCAT(DISTINCT e.BR_TO_SECTION ORDER BY e.BR_TO_SECTION ASC SEPARATOR ',') BR_TO_SECTION, "; 
				sql += "GROUP_CONCAT(DISTINCT b.RUNNING_NO ORDER BY b.RUNNING_NO ASC SEPARATOR ',') RUNNING_NO_DEPARTMENT, "; 
				sql += " NULL BR_REMARK_DEPARTMENT, NULL BR_REMARK_GROUP, NULL BR_REMARK_SECTION, NULL BR_REMARK_USER, ";
				sql += " GROUP_CONCAT(DISTINCT bo.BR_TO_BOARD ORDER BY bo.BR_BOARD_ID ASC SEPARATOR ',') BR_TO_BOARD, ";
				sql += " GROUP_CONCAT(DISTINCT bo.REMARK ORDER BY bo.BR_BOARD_ID ASC SEPARATOR ',') BR_REMARK_BOARD "; 
				sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id ";
				sql += " LEFT JOIN BOOK_RECIEVE_GROUP c ON a.br_id = c.br_id AND c.BR_TO_DEPARTMENT = b.BR_TO_DEPARTMENT";
				sql += " LEFT JOIN BOOK_RECIEVE_SECTION e ON a.br_id = e.br_id AND c.BR_TO_GROUP = e.BR_TO_GROUP";
				sql += " LEFT JOIN BOOK_RECIEVE_USER d ON a.br_id = d.br_id AND c.BR_TO_DEPARTMENT = d.BR_TO_DEPARTMENT AND c.BR_TO_GROUP = d.BR_TO_GROUP";
				sql += " LEFT JOIN BOOK_RECIEVE_BOARD bo ON a.br_id = bo.br_id ";
				sql += " WHERE a.br_id = " + brId;
				if(user.getRole().equals("BOARD")) {
					sql += " AND bo.br_to_board = " + user.getBoardId();
				}
			}else if(user.getRole().equals("DEPARTMENT")){
				sql += "a.BR_STATUS,";
				sql += "GROUP_CONCAT(c.BR_TO_GROUP SEPARATOR ',') BR_TO_GROUP,";
				sql += "GROUP_CONCAT(c.BR_TO_GROUP_NAME SEPARATOR ',') BR_TO_GROUP_NAME, ";
				sql += "NULL BR_TO_USER, NULL BR_TO_SECTION, NULL BR_TO_SECTION_NAME, NULL BR_TO_USER_NAME, b.BR_TO_DEPARTMENT, b.BR_TO_DEPARTMENT_SHORT, b.BR_TO_DEPARTMENT_NAME, ";
				sql += " b.REMARK BR_REMARK_DEPARTMENT, NULL BR_REMARK_GROUP, NULL BR_REMARK_SECTION, NULL BR_REMARK_USER, ";
				sql += "GROUP_CONCAT(b.RUNNING_NO SEPARATOR ',') RUNNING_NO_DEPARTMENT, NULL BR_REMARK_BOARD, NULL BR_TO_BOARD ";
				sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id LEFT JOIN BOOK_RECIEVE_GROUP c ON b.br_id = c.br_id AND b.BR_TO_DEPARTMENT = c.BR_TO_DEPARTMENT ";
				sql += " AND c.BR_TO_DEPARTMENT = b.BR_TO_DEPARTMENT ";
				sql += " WHERE a.br_id = " + brId;
				sql += " AND b.BR_TO_DEPARTMENT = '" + user.getDivision() + "'";
			}else if(user.getRole().equals("GROUP")){
				sql += "a.BR_STATUS,";

				sql += "GROUP_CONCAT(DISTINCT e.BR_TO_SECTION ORDER BY e.BR_TO_SECTION ASC SEPARATOR ',') BR_TO_SECTION ,";
				sql += "GROUP_CONCAT(DISTINCT e.BR_TO_SECTION_NAME ORDER BY e.BR_TO_SECTION_NAME ASC SEPARATOR ',') BR_TO_SECTION_NAME ,";
				sql += "GROUP_CONCAT(DISTINCT d.BR_TO_USER_NAME ORDER BY d.BR_TO_USER_NAME ASC SEPARATOR ',') BR_TO_USER_NAME ,";
				sql += "GROUP_CONCAT(DISTINCT d.BR_TO_USER ORDER BY d.BR_TO_USER ASC SEPARATOR ',') BR_TO_USER ,";
				sql += "c.BR_TO_GROUP, c.BR_TO_GROUP_NAME, b.BR_TO_DEPARTMENT, b.BR_TO_DEPARTMENT_SHORT, b.BR_TO_DEPARTMENT_NAME, ";
				sql += " b.REMARK BR_REMARK_DEPARTMENT, c.REMARK BR_REMARK_GROUP, NULL BR_REMARK_USER, ";
				if(user.getSectionId() != null) {
					sql += " e.REMARK BR_REMARK_SECTION,";
				}else {
					sql += " NULL BR_REMARK_SECTION,";
				}
				sql += " NULL RUNNING_NO_DEPARTMENT, NULL BR_REMARK_BOARD, NULL BR_TO_BOARD ";
				sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id LEFT JOIN BOOK_RECIEVE_GROUP c ON b.br_id = c.br_id AND b.BR_TO_DEPARTMENT = c.BR_TO_DEPARTMENT LEFT JOIN BOOK_RECIEVE_USER d ON c.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP ";
				sql += " LEFT JOIN BOOK_RECIEVE_SECTION e ON b.br_id = e.br_id ";
				sql += " AND e.BR_TO_GROUP = c.BR_TO_GROUP";
				sql += " WHERE a.br_id = " + brId;
				if(user.getSectionId() != null) {
					sql += " AND e.BR_TO_SECTION = '" + user.getSectionId() + "'";
				}
				sql += " AND b.BR_TO_DEPARTMENT = '" + user.getDivision() + "'";
				sql += " AND c.BR_TO_GROUP = '" + user.getGroupId() + "'";
			}else if(user.getRole().equals("USER")){
				sql += "d.STATUS BR_STATUS,";
				sql += "d.BR_TO_USER,";
				sql += "d.BR_TO_USER_NAME, ";
				sql += " NULL BR_TO_SECTION, NULL BR_TO_SECTION_NAME,";
				sql += "c.BR_TO_GROUP, c.BR_TO_GROUP_NAME, b.BR_TO_DEPARTMENT, b.BR_TO_DEPARTMENT_SHORT, b.BR_TO_DEPARTMENT_NAME, ";
				sql += " b.REMARK BR_REMARK_DEPARTMENT, c.REMARK BR_REMARK_GROUP, e.REMARK BR_REMARK_SECTION, d.REMARK BR_REMARK_USER,";
				sql += " NULL RUNNING_NO_DEPARTMENT, NULL BR_REMARK_BOARD, NULL BR_TO_BOARD ";
				sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id LEFT JOIN BOOK_RECIEVE_GROUP c ON b.br_id = c.br_id AND b.BR_TO_DEPARTMENT = c.BR_TO_DEPARTMENT INNER JOIN BOOK_RECIEVE_USER d ON b.br_id = d.br_id AND c.BR_TO_GROUP = d.BR_TO_GROUP AND b.BR_TO_DEPARTMENT = d.BR_TO_DEPARTMENT";
				sql += " AND d.BR_TO_GROUP = c.BR_TO_GROUP";
				sql += " LEFT JOIN BOOK_RECIEVE_SECTION e ON b.br_id = e.br_id AND e.BR_TO_SECTION = d.BR_TO_SECTION";
				sql += " WHERE a.br_id = " + brId;
				sql += " AND b.BR_TO_DEPARTMENT = '" + user.getDivision() + "'";
				sql += " AND c.BR_TO_GROUP = '" + user.getGroupId() + "'";
				sql += " AND d.BR_TO_USER = '" + user.getId() + "'";
			}
			sql += " GROUP BY a.BR_ID";
			if(user.getRole().equals("DEPARTMENT")){
				sql += " ,b.BR_TO_DEPARTMENT_SHORT, b.BR_TO_DEPARTMENT_NAME, b.REMARK ";
			}else if(user.getRole().equals("GROUP")){
				sql += " ,c.BR_TO_GROUP_NAME , b.BR_TO_DEPARTMENT_SHORT,b.BR_TO_DEPARTMENT_NAME, b.REMARK, c.REMARK ";
				if(user.getSectionId() != null) {
					sql += " , e.REMARK";
				}
			}else if(user.getRole().equals("USER")){
				sql += " ,d.STATUS, d.BR_TO_USER, d.BR_TO_USER_NAME, c.BR_TO_GROUP_NAME, b.BR_TO_DEPARTMENT_SHORT, b.BR_TO_DEPARTMENT_NAME, d.REMARK, b.REMARK, c.REMARK, e.REMARK ";
			}else if(user.getRole().equals("BOARD")){
				sql += " ,bo.STATUS";
			}
			sql	+= ") x";
			data = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();
			if(data.size() > 0){
				result = data.get(0);
			}
		}catch(Exception ex){
			logger.error("getDataFromId : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}

	@Override
	public int LastID() {
		Integer id = 0; 
		Session session = OpenSession();
		try{
			id =  (Integer) session.createSQLQuery("SELECT BR_ID FROM BOOK_RECIVE_OUT ORDER BY BR_ID DESC LIMIT 1").uniqueResult();
		}catch(Exception ex){
			logger.error("LastID : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getYear() throws HibernateException {
		Session session = OpenSession();
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
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
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
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
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
		Integer id = 0; 
		Integer brNum = 1;
		Session session = OpenSession();
		try{
			
			Query sql = session.createSQLQuery("SELECT BR_NUM FROM BOOK_RECIVE_OUT WHERE BR_YEAR = :BR_YEAR ORDER BY BR_NUM DESC LIMIT 1");
			sql.setParameter("BR_YEAR", brYear);
			id =  (Integer) sql.uniqueResult();
			brNum = (id == null) ? 1 : id + 1;
		}catch(Exception ex){
			logger.error("getNextBrNum : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return brNum;
	}

	@Override
	public String delete(int id) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			Query query = session.createQuery("delete BookReciveOut where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete BookRecieveDepartment where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete BookRecieveGroup where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete BookRecieveSection where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete BookRecieveUser where brId = :ID");
			query.setParameter("ID", id);
			query.executeUpdate();
			query = session.createQuery("delete Attachment where objectId = :ID and objectName = 'BOOK_RECIVE_OUT'");
			query.setParameter("ID", id);
			query.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("delete : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}

	@Override
	public int updateReciveOutDepartment(BookRecieveDepartment recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int result = 0;
		try{
			Query query = session.createQuery("update BookRecieveDepartment set status = :status, updatedDate = SYSDATE(), updatedBy = :updatedBy, remark = :remark where brId = :brId and brToDepartment = :brToDepartment");
			query.setParameter("status", recive.getStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("brId", recive.getBrId());
			query.setParameter("brToDepartment", recive.getBrToDepartment());
			query.setParameter("remark", recive.getRemark());
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception ex) {
			logger.error("updateReciveOutDepartment : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}

	@Override
	public int updateReciveOutGroup(BookRecieveGroup recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int result = 0;
		try{
			Query query = session.createQuery("update BookRecieveGroup set status = :status, updatedDate = SYSDATE(), updatedBy = :updatedBy, remark = :remark where brId = :brId and brToDepartment = :brToDepartment and brToGroup = :brToGroup");
			query.setParameter("status", recive.getStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("brId", recive.getBrId());
			query.setParameter("brToDepartment", recive.getBrToDepartment());
			query.setParameter("brToGroup", recive.getBrToGroup());
			query.setParameter("remark", recive.getRemark());
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception ex) {
			logger.error("updateReciveOutGroup : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}
	
	@Override
	public int updateReciveOutSection(BookRecieveSection recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int result = 0;
		try{
			Query query = session.createQuery("update BookRecieveSection set status = :status, updatedDate = SYSDATE(), updatedBy = :updatedBy, remark = :remark where brId = :brId and brToDepartment = :brToDepartment and brToGroup = :brToGroup and brToSection = :brToSection");
			query.setParameter("status", recive.getStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("remark", recive.getRemark());
			query.setParameter("brId", recive.getBrId());
			query.setParameter("brToDepartment", recive.getBrToDepartment());
			query.setParameter("brToGroup", recive.getBrToGroup());
			query.setParameter("brToSection", recive.getBrToSection());
			
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception ex) {
			logger.error("updateReciveOutSection : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}

	@Override
	public int updateReciveOutUser(BookRecieveUser recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int result = 0;
		try{
			Query query = session.createQuery("update BookRecieveUser set status = :status, updatedDate = SYSDATE(), updatedBy = :updatedBy, remark = :remark where brId = :brId and brToGroup = :brToGroup and brToUser = :brToUser and brToDepartment = :brToDepartment");
			query.setParameter("status", recive.getStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("remark", recive.getRemark());
			query.setParameter("brId", recive.getBrId());
			query.setParameter("brToGroup", recive.getBrToGroup());
			query.setParameter("brToUser", recive.getBrToUser());
			query.setParameter("brToDepartment", recive.getBrToDepartment());
			result = query.executeUpdate();
			
			List<BookRecieveUser> data = session.createSQLQuery("select * from Book_Recieve_User where BR_ID = '" + recive.getBrId() + "'").addEntity(BookRecieveUser.class).list();
			boolean flag = true;
			for(BookRecieveUser u : data) {
				if("N".equals(u.getStatus())){
					flag = false;
				}
			}
			if(flag) {
				query = session.createQuery("update BookReciveOut set "
						+ "brStatus = :brStatus, "
						+ "updatedBy = :updatedBy, "
						+ "updatedDate = SYSDATE() "
						+ "where brId = :brId");
				query.setParameter("brStatus", recive.getStatus());
				query.setParameter("updatedBy", recive.getUpdatedBy());
				query.setParameter("brId", recive.getBrId());
				result = query.executeUpdate();
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("updateReciveOutUser : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}

	@Override
	public int updateStatusReciveOut(BookReciveOut recive) {
		Session session = OpenSession();
		int result = 0;
		try{
			Query query = session.createQuery("update BookReciveOut set "
					+ "brStatus = :brStatus, "
					+ "updatedBy = :updatedBy, "
					+ "updatedDate = SYSDATE() "
					+ "where brId = :brId");
			query.setParameter("brStatus", recive.getBrStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("brId", recive.getBrId());
			result = query.executeUpdate();
		} catch (Exception ex) {
			logger.error("updateStatusReciveOut : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}
	
	@Override
	public int updateReciveOutAdmin(BookReciveOut recive) {
		Session session = OpenSession();
		int result = 0;
		try{
			Query query = session.createQuery("update BookReciveOut set "
					+ "brNum = :brNum, "
					+ "brYear = :brYear, "
					+ "brRdate = :brRdate, "
					+ "brTypeQuick = :brTypeQuick, "
					+ "brTypeSecret = :brTypeSecret, "
					+ "brPlace = :brPlace, "
					+ "brDate = :brDate, "
					+ "brFrom = :brFrom, "
					+ "brTo = :brTo, "
					+ "brSubject = :brSubject, "
					+ "brRemark = :brRemark, "
					+ "brStatus = :brStatus, "
					+ "updatedBy = :updatedBy, "
					+ "updatedDate = SYSDATE() "
					+ "where brId = :brId");
			query.setParameter("brNum", recive.getBrNum());
			query.setParameter("brYear", recive.getBrYear());
			query.setParameter("brRdate", recive.getBrRdate());
			query.setParameter("brTypeQuick", recive.getBrTypeQuick());
			query.setParameter("brTypeSecret", recive.getBrTypeSecret());
			query.setParameter("brPlace", recive.getBrPlace());
			query.setParameter("brDate", recive.getBrDate());
			query.setParameter("brFrom", recive.getBrFrom());
			query.setParameter("brTo", recive.getBrTo());
			query.setParameter("brSubject", recive.getBrSubject());
			query.setParameter("brRemark", recive.getBrRemark());
			query.setParameter("brStatus", recive.getBrStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("brId", recive.getBrId());
			result = query.executeUpdate();
		} catch (Exception ex) {
			logger.error("updateReciveOutAdmin : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}

	@Override
	public int insertRecieveDepartment(BookRecieveDepartment recive) throws Exception {
		BigInteger result = new BigInteger("0");
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIEVE_DEPARTMENT (BR_ID, BR_TO_DEPARTMENT, BR_TO_DEPARTMENT_SHORT, BR_TO_DEPARTMENT_NAME, STATUS, UPDATED_BY, UPDATED_DATE, RUNNING_NO, REMARK)"
					+ " VALUES (:BR_ID, :BR_TO_DEPARTMENT, :BR_TO_DEPARTMENT_SHORT, :BR_TO_DEPARTMENT_NAME, :STATUS, :UPDATED_BY, SYSDATE(), :RUNNING_NO, :REMARK) ");
			query.setParameter("BR_ID", recive.getBrId());
			query.setParameter("BR_TO_DEPARTMENT", recive.getBrToDepartment());
			query.setParameter("BR_TO_DEPARTMENT_SHORT", recive.getBrToDepartmentShort());
			query.setParameter("BR_TO_DEPARTMENT_NAME", recive.getBrToDepartmentName());
			query.setParameter("STATUS", recive.getStatus());
			query.setParameter("UPDATED_BY", recive.getUpdatedBy());
			query.setParameter("RUNNING_NO", recive.getRunningNo());
			query.setParameter("REMARK", recive.getRemark());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("insertRecieveDepartment : ", ex);
			tx.rollback();
			throw ex;
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}
	
	@Override
	public int insertRecieveBoard(BookRecieveBoard recive) throws Exception {
		BigInteger result = new BigInteger("0");
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIEVE_BOARD (BR_ID, BR_TO_BOARD, BR_TO_BOARD_NAME, STATUS, UPDATED_BY, UPDATED_DATE, REMARK)"
					+ " VALUES (:BR_ID, :BR_TO_BOARD, :BR_TO_BOARD_NAME, :STATUS, :UPDATED_BY, SYSDATE(), :REMARK) ");
			query.setParameter("BR_ID", recive.getBrId());
			query.setParameter("BR_TO_BOARD", recive.getBrToBoard());
			query.setParameter("BR_TO_BOARD_NAME", recive.getBrToBoardName());
			query.setParameter("STATUS", recive.getStatus());
			query.setParameter("UPDATED_BY", recive.getUpdatedBy());
			query.setParameter("REMARK", recive.getRemark());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("insertRecieveBoard : ", ex);
			tx.rollback();
			throw ex;
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}
	
	@Override
	public int insertRecieveGroup(BookRecieveGroup recive) throws Exception {
		BigInteger result = new BigInteger("0");
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIEVE_GROUP (BR_ID, BR_TO_DEPARTMENT, BR_TO_GROUP, BR_TO_GROUP_NAME, STATUS, UPDATED_BY, UPDATED_DATE, REMARK)"
					+ " VALUES (:BR_ID, :BR_TO_DEPARTMENT, :BR_TO_GROUP, :BR_TO_GROUP_NAME, :STATUS, :UPDATED_BY, SYSDATE(), :REMARK) ");
			query.setParameter("BR_ID", recive.getBrId());
			query.setParameter("BR_TO_DEPARTMENT", recive.getBrToDepartment());
			query.setParameter("BR_TO_GROUP", recive.getBrToGroup());
			query.setParameter("BR_TO_GROUP_NAME", recive.getBrToGroupName());
			query.setParameter("STATUS", recive.getStatus());
			query.setParameter("UPDATED_BY", recive.getUpdatedBy());
			query.setParameter("REMARK", recive.getRemark());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("insertRecieveGroup : ", ex);
			tx.rollback();
			throw ex;
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}
	
	@Override
	public int insertRecieveUser(BookRecieveUser recive) throws Exception {
		BigInteger result = new BigInteger("0");

		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIEVE_USER (BR_ID, BR_TO_GROUP, BR_TO_USER, BR_TO_USER_NAME, BR_TO_DEPARTMENT, STATUS, UPDATED_BY, UPDATED_DATE, REMARK, BR_TO_SECTION)"
					+ " VALUES (:BR_ID, :BR_TO_GROUP, :BR_TO_USER, :BR_TO_USER_NAME, :BR_TO_DEPARTMENT, :STATUS, :UPDATED_BY, SYSDATE(), :REMARK, :BR_TO_SECTION) ");
			query.setParameter("BR_ID", recive.getBrId());
			query.setParameter("BR_TO_GROUP", recive.getBrToGroup());
			query.setParameter("BR_TO_USER", recive.getBrToUser());
			query.setParameter("BR_TO_USER_NAME", recive.getBrToUserName());
			query.setParameter("BR_TO_DEPARTMENT", recive.getBrToDepartment());
			query.setParameter("STATUS", recive.getStatus());
			query.setParameter("UPDATED_BY", recive.getUpdatedBy());
			query.setParameter("REMARK", recive.getRemark());
			query.setParameter("BR_TO_SECTION", recive.getBrToSection());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("insertRecieveUser : ", ex);
			tx.rollback();
			throw ex;
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}
	
	@Override
	public String deleteRecieveDepartment(BookReciveOut bookReciveOut, String brToNotIn) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_Department where br_Id = :brId and br_to_department not in(" + brToNotIn + ")")
				.setParameter("brId", bookReciveOut.getBrId())
				.executeUpdate();
			session.createSQLQuery("delete from Book_Recieve_Group where br_Id = :brId and br_to_department not in(" + brToNotIn + ")")
				.setParameter("brId", bookReciveOut.getBrId())
				.executeUpdate();
			session.createSQLQuery("delete from Book_Recieve_Section where br_Id = :brId and br_to_department not in(" + brToNotIn + ")")
				.setParameter("brId", bookReciveOut.getBrId())
				.executeUpdate();
			session.createSQLQuery("delete from Book_Recieve_User where br_Id = :brId and br_to_department not in(" + brToNotIn + ")")
				.setParameter("brId", bookReciveOut.getBrId())
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveDepartment : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public String deleteRecieveGroup(BookRecieveDepartment bookRecieveDepartment, String brToNotIn) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_Group where br_Id = :brId and br_To_Department = :brToDepartment and br_to_group not in(" + brToNotIn + ")")
				.setParameter("brId", bookRecieveDepartment.getBrId())
				.setParameter("brToDepartment", bookRecieveDepartment.getBrToDepartment())
				.executeUpdate();
			session.createSQLQuery("delete from Book_Recieve_User where br_Id = :brId and br_To_Department = :brToDepartment and br_to_group not in(" + brToNotIn + ")")
				.setParameter("brId", bookRecieveDepartment.getBrId())
				.setParameter("brToDepartment", bookRecieveDepartment.getBrToDepartment())
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveGroup : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public String deleteRecieveUser(BookRecieveGroup bookRecieveGroup, String brToNotIn) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_User where br_Id = :brId and br_To_Group = :brToGroup and br_To_Department = :brToDepartment and br_to_user not in(" + brToNotIn + ")")
				.setParameter("brId", bookRecieveGroup.getBrId())
				.setParameter("brToGroup", bookRecieveGroup.getBrToGroup())
				.setParameter("brToDepartment", bookRecieveGroup.getBrToDepartment())
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveUser : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public String deleteRecieveSectionUser(BookRecieveSection bookRecieveSection, String brToNotIn) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_User where br_Id = :brId and br_To_Group = :brToGroup and br_To_Department = :brToDepartment and br_To_Section = :brToSection and br_to_user not in(" + brToNotIn + ")")
				.setParameter("brId", bookRecieveSection.getBrId())
				.setParameter("brToGroup", bookRecieveSection.getBrToGroup())
				.setParameter("brToSection", bookRecieveSection.getBrToSection())
				.setParameter("brToDepartment", bookRecieveSection.getBrToDepartment())
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveSectionUser : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public String deleteRecieveUserByBrId(int brId) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_User where br_Id = :brId")
				.setParameter("brId", brId)
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveUserByBrId : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public int getCountDataBookRecive(int year) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		try{
			rs = ((Long) session.createCriteria(BookReciveOut.class).add(Restrictions.eq("brYear", year)).setProjection(Projections.rowCount()).uniqueResult()).intValue();
		}catch (Exception ex) {
			logger.error("getCountDataBookRecive : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}
	
	@Override
	public BookReciveOut getLastRowOfYear(int brYear) {
		BookReciveOut bookReciveOut = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT a.BR_ID,";
			sql	+= "a.BR_NUM,";
			sql += "a.BR_YEAR,";
			sql += "a.BR_RDATE,";
			sql += "a.BR_TYPE_QUICK,";
			sql += "a.BR_TYPE_SECRET,";
			sql += "a.BR_PLACE,";
			sql += "a.BR_DATE,";
			sql += "a.BR_FROM,";
			sql += "a.BR_TO,";
			sql += "a.BR_SUBJECT,";
			sql += "a.BR_REMARK,";
			sql += "a.BR_DIVISION,";
			sql += "a.BR_PCODE,";
			sql += "a.BR_STATUS,";
			sql += "a.BR_IMAGE,";
			sql += "a.CREATED_BY,";
			sql += "a.CREATED_DATE,";
			sql += "a.DIVISION,";
			sql += "a.UPDATED_BY,";
			sql += "a.UPDATED_DATE,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT SEPARATOR ',') BR_TO_DEPARTMENT, ";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_SHORT SEPARATOR ',') BR_TO_DEPARTMENT_SHORT,";
			sql += "GROUP_CONCAT(b.BR_TO_DEPARTMENT_NAME SEPARATOR ',') BR_TO_DEPARTMENT_NAME, ";
			sql += "NULL BR_TO_GROUP, NULL BR_TO_GROUP_NAME, NULL BR_TO_USER, NULL BR_TO_USER_NAME";
			sql += " FROM BOOK_RECIVE_OUT a LEFT JOIN BOOK_RECIEVE_DEPARTMENT b ON a.br_id = b.br_id ";
			sql += " WHERE a.BR_YEAR = '" + brYear + "'";
			sql += " GROUP BY a.BR_ID ORDER BY a.BR_ID DESC LIMIT 1";
			List<BookReciveOut> data = session.createSQLQuery(sql).addEntity(BookReciveOut.class).list();
			if(data.size() > 0){
				bookReciveOut = data.get(0);
			}
		} catch (Exception ex) { 
			logger.error("getLastRowOfYear : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return bookReciveOut;
	}
	
	@Override
	public List<StatusDetail> getStatusDetail(String brId) {
		List<StatusDetail> statusDetail = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT * FROM (SELECT a.BR_TO_DEPARTMENT, b.BR_TO_GROUP, c.BR_TO_USER, a.BR_TO_DEPARTMENT_NAME, b.BR_TO_GROUP_NAME, c.BR_TO_USER_NAME, c.STATUS,  d.BR_SECTION_ID, d.BR_TO_SECTION_NAME FROM book_recieve_department a ";
			sql += "LEFT JOIN book_recieve_group b on a.BR_ID = b.BR_ID AND a.BR_TO_DEPARTMENT = b.BR_TO_DEPARTMENT ";
			sql += "LEFT JOIN book_recieve_section d ON a.BR_ID = d.BR_ID AND d.BR_TO_GROUP = b.BR_TO_GROUP ";
			sql += "LEFT JOIN book_recieve_user c ON a.BR_ID = c.BR_ID AND c.BR_TO_GROUP = b.BR_TO_GROUP AND c.BR_TO_SECTION = d.BR_TO_SECTION ";
			sql += "WHERE a.BR_ID = :brId ";
			sql += "UNION ";
			sql += "SELECT a.BR_TO_DEPARTMENT, b.BR_TO_GROUP, c.BR_TO_USER, a.BR_TO_DEPARTMENT_NAME, b.BR_TO_GROUP_NAME, c.BR_TO_USER_NAME, c.STATUS,  NULL BR_SECTION_ID, NULL BR_TO_SECTION_NAME FROM book_recieve_department a ";
			sql += "LEFT JOIN book_recieve_group b on a.BR_ID = b.BR_ID AND a.BR_TO_DEPARTMENT = b.BR_TO_DEPARTMENT ";
			sql += "LEFT JOIN book_recieve_user c ON a.BR_ID = c.BR_ID AND c.BR_TO_GROUP = b.BR_TO_GROUP AND c.BR_TO_SECTION IS NULL ";
			sql += "WHERE a.BR_ID = :brId) a ORDER BY a.BR_TO_DEPARTMENT, a.BR_TO_GROUP, a.BR_TO_USER";
			
			Query query = session.createSQLQuery(sql);
			query.setParameter("brId", brId);
			List<Object[]> rows = query.list();
			statusDetail = new ArrayList<StatusDetail>();
			for(Object[] row : rows){
				StatusDetail s = new StatusDetail();
				s.setBrToDepartment((row[0] != null) ? row[0].toString() : "");
				s.setBrToGroup((row[1] != null) ? row[1].toString() : "");
				s.setBrToUser((row[2] != null) ? row[2].toString() : "");
				s.setBrToDepartmentName((row[3] != null) ? row[3].toString() : "");
				s.setBrToGroupName((row[4] != null) ? row[4].toString() : "");
				s.setBrToUserName((row[5] != null) ? row[5].toString() : "");
				s.setStatus((row[6] != null) ? row[6].toString() : "");
				s.setBrToSection((row[7] != null) ? row[7].toString() : "");
				s.setBrToSectionName((row[8] != null) ? row[8].toString() : "");
				statusDetail.add(s);
			}
		} catch (Exception ex) { 
			logger.error("getStatusDetail : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return statusDetail;
	}

	@Override
	public Map<String, List<String>> getGroupSelectedByAdmin(String departments) {
		Map<String, List<String>> map = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT b.division_name, a.GROUP_ID, a.GROUP_NAME, a.division_code FROM groups a INNER JOIN divisions b ON a.division_code = b.division_code WHERE a.division_code IN (" + departments + ")";
			Query query = session.createSQLQuery(sql);
			List<Object[]> rows = query.list();
			map = new HashMap<String, List<String>>();
			String chk = "";
			List<String> groups = new ArrayList<String>();
			for(Object[] row : rows){
				if(StringUtils.isNullOrEmpty(chk)){
					chk = row[0].toString();
				}else if(!chk.equals(row[0].toString())){
					map.put(chk, groups);
					groups = new ArrayList<String>();
					chk = row[0].toString();
				}
				groups.add(row[3] + "xx##xx" + row[1] + "xx#xx" + row[2]);
			}
			map.put(chk, groups);
		} catch (Exception ex) { 
			logger.error("getGroupSelectedByAdmin : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return map;
	}
	
	
	@Override
	public Map<String, List<String>> getUserSelectedByAdmin(String groups, String sections) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();;
		Session session = OpenSession();
		try {
			String[] arrSections = null;
			String sql = "";
			if(StringUtils.isNullOrEmpty((groups))) {
				groups = "''";
			}
			Map<String, String> users = new HashMap<String, String>();
			if(!StringUtils.isNullOrEmpty(sections)) {
				sections = sections.replaceAll("'",  "");
				arrSections = sections.split(",");
				boolean dataNotfound = false;
				for(int i = 0; i < arrSections.length; i++) {
					sql = "SELECT b.division_name, c.group_name, a.id, a.prefix, a.fname, a.lname, a.division, a.group_id, s.id section_id, s.section_name";
					sql += " FROM users a ";
					sql += " INNER JOIN divisions b ON a.division = b.division_code ";
					sql += " INNER JOIN groups c ON a.division = c.division_code and a.group_id = c.group_id";
					sql += " INNER JOIN sections s ON a.section_id = s.id AND s.id = " + arrSections[i]; 
					sql += " WHERE a.group_id IN (" + groups + ") AND a.role = 'USER'";
					
					Query query = session.createSQLQuery(sql);
					List<Object[]> rows = query.list();
					if(rows.isEmpty()) {
						dataNotfound = true;
						break;
					}else {
						
						for(Object[] row : rows){
							String result = "";
							if(!StringUtils.isNullOrEmpty(sections)){
								result = row[0] + "xx#xx" + row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[8] + "xx#xx" + row[9] + "xx#xx" + row[3] + row[4] + " " + row[5];
							}else {
								result = row[0] + "xx#xx" + row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[3] + row[4] + " " + row[5];
							}
							
							users.put(row[2].toString(), result);
						}
					}
				}
				if(dataNotfound) {
					map = null;
					sql = "SELECT b.division_name, c.group_name, a.id, a.prefix, a.fname, a.lname, a.division, a.group_id, s.id section_id, s.section_name";
					sql += " FROM users a ";
					sql += " INNER JOIN divisions b ON a.division = b.division_code ";
					sql += " INNER JOIN groups c ON a.division = c.division_code and a.group_id = c.group_id";
					sql += " LEFT JOIN sections s ON a.section_id = s.id AND s.id IN (" + sections + ")"; 
					sql += " WHERE a.group_id IN (" + groups + ") AND a.role = 'USER'";
					Query query = session.createSQLQuery(sql);
					List<Object[]> rows = query.list();
					if(!rows.isEmpty()){
						
						for(Object[] row : rows){
							
							String result = "";
							if(!StringUtils.isNullOrEmpty(sections)){
								result = row[0] + "xx#xx" + row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[8] + "xx#xx" + row[9] + "xx#xx" + row[3] + row[4] + " " + row[5];
							}else {
								result = row[0] + "xx#xx" + row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[3] + row[4] + " " + row[5];
							}
							
							users.put(row[2].toString(), result);
						}
					}
				}
			}else {
			
				sql = "SELECT b.division_name, c.group_name, a.id, a.prefix, a.fname, a.lname, a.division, a.group_id";
				sql += " FROM users a ";
				sql += " INNER JOIN divisions b ON a.division = b.division_code ";
				sql += " INNER JOIN groups c ON a.division = c.division_code and a.group_id = c.group_id";
				sql += " WHERE a.group_id IN (" + groups + ") AND a.role = 'USER'";
				
				Query query = session.createSQLQuery(sql);
				List<Object[]> rows = query.list();
				if(!rows.isEmpty()){
					
					for(Object[] row : rows){
						
						String result = row[0] + "xx#xx" + row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[3] + row[4] + " " + row[5];
						users.put(row[2].toString(), result);
					}
				}
			}
			for(Entry<String, String> ent : users.entrySet()){
				String[] arr = ent.getValue().split("xx#xx");
				String str = ent.getValue().replaceFirst(arr[0] + "xx#xx", "");
				if(map.containsKey(arr[0])) {
					map.get(arr[0]).add(str);
				}else {
					List<String> l = new ArrayList<String>();
					l.add(str);
					map.put(arr[0], l);
				}
			}
		} catch (Exception ex) { 
			logger.error("getUserSelectedByAdmin : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return map;
	}

	@Override
	public Map<String, List<String>> getUserForSectionRoleSelectedByAdmin(String groups, String sections) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Session session = OpenSession();
		try {
			String[] arrSections = null;
			String sql = "";
			if(StringUtils.isNullOrEmpty((groups))) {
				groups = "''";
			}
			sql = "SELECT b.division_name, c.group_name, a.id, a.prefix, a.fname, a.lname, a.division, a.group_id, s.id section_id, s.section_name";
			sql += " FROM users a ";
			sql += " INNER JOIN divisions b ON a.division = b.division_code ";
			sql += " INNER JOIN groups c ON a.division = c.division_code and a.group_id = c.group_id";
			sql += " INNER JOIN sections s ON a.section_id = s.id AND s.id = " + sections; 
			sql += " WHERE a.group_id IN (" + groups + ") AND a.role = 'USER'";
			
			Query query = session.createSQLQuery(sql);
			List<Object[]> rows = query.list();

			
			String chk = "";
			List<String> users = new ArrayList<String>();
			
			for(Object[] row : rows){
				if(StringUtils.isNullOrEmpty(chk)){
					chk = row[0].toString();
				}else if(!chk.equals(row[0].toString())){
					map.put(chk, users);
					users = new ArrayList<String>();
					chk = row[0].toString();
				}
				String result = "";
				if(!StringUtils.isNullOrEmpty(sections)){
					result = row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[8] + "xx#xx" + row[9] + "xx#xx" + row[3] + row[4] + " " + row[5];
				}else {
					result = row[6] + "xx#xx" + row[7] + "xx#xx" + row[2] + "xx#xx" + row[3] + row[4] + " " + row[5];
				}
				
				users.add(result);
			}
			map.put(chk, users);
			
		} catch (Exception ex) { 
			logger.error("getUserSelectedByAdmin : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return map;
	}
	
	@Override
	public List<BookRecieveDepartment> getBookRecieveOutDepartment(int brId) {
		List<BookRecieveDepartment> data = new ArrayList<BookRecieveDepartment>();
		Session session = OpenSession();
		try {
			String sql = "SELECT * FROM BOOK_RECIEVE_DEPARTMENT WHERE BR_ID = " + brId;
			sql += " ORDER BY BR_TO_DEPARTMENT ASC ";
			data = (List<BookRecieveDepartment>) session.createSQLQuery(sql).addEntity(BookRecieveDepartment.class).list();
		} catch (Exception ex) { 
			logger.error("getBookRecieveOutDepartment : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public List<BookRecieveGroup> getBookRecieveOutGroup(int brId, String brToDepartment) {
		List<BookRecieveGroup> data = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT * FROM BOOK_RECIEVE_GROUP WHERE BR_ID = " + brId + " AND BR_TO_DEPARTMENT = '" + brToDepartment + "'";
			sql += " ORDER BY BR_TO_GROUP ASC ";
			data = session.createSQLQuery(sql).addEntity(BookRecieveGroup.class).list();
		} catch (Exception ex) { 
			logger.error("getBookRecieveOutGroup : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public List<BookRecieveUser> getBookRecieveOutUser(int brId, String brToDepartment, String brToGroup) {
		List<BookRecieveUser> data = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT * FROM BOOK_RECIEVE_USER WHERE BR_ID = " + brId + " AND BR_TO_DEPARTMENT = '" + brToDepartment + "' AND BR_TO_GROUP = '" + brToGroup + "'";
			sql += " ORDER BY BR_TO_USER ASC ";
			data = session.createSQLQuery(sql).addEntity(BookRecieveUser.class).list();
		} catch (Exception ex) { 
			logger.error("getBookRecieveOutUser : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}
	
	@Override
	public List<BookRecieveUser> getBookRecieveOutSectionUser(int brId, String brToDepartment, String brToGroup, String brToSection) {
		List<BookRecieveUser> data = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT * FROM BOOK_RECIEVE_USER WHERE BR_ID = " + brId + " AND BR_TO_DEPARTMENT = '" + brToDepartment + "' AND BR_TO_GROUP = '" + brToGroup + "' AND BR_TO_SECTION = '" + brToSection + "'";
			sql += " ORDER BY BR_TO_USER ASC ";
			data = session.createSQLQuery(sql).addEntity(BookRecieveUser.class).list();
		} catch (Exception ex) { 
			logger.error("getBookRecieveOutUser : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return data;
	}

	@Override
	public Map<String, List<String>> getSectionSelectedByAdmin(String groups) {
		Map<String, List<String>> map = null;
		Session session = OpenSession();
		if(StringUtils.isNullOrEmpty((groups))) {
			groups = "''";
		}
		try {
			String sql = "SELECT b.group_name, a.id, a.section_name, a.group_id FROM sections a INNER JOIN groups b ON a.group_id = b.group_id WHERE a.group_id IN (" + groups + ")";
			Query query = session.createSQLQuery(sql);
			List<Object[]> rows = query.list();
			if(!rows.isEmpty()){
				map = new HashMap<String, List<String>>();
				String chk = "";
				List<String> sections = new ArrayList<String>();
				for(Object[] row : rows){
					if(StringUtils.isNullOrEmpty(chk)){
						chk = row[0].toString();
					}else if(!chk.equals(row[0].toString())){
						map.put(chk, sections);
						sections = new ArrayList<String>();
						chk = row[0].toString();
					}
					sections.add(row[3] + "xx##xx" + row[1] + "xx#xx" + row[2]);
				}
				map.put(chk, sections);
			}
		} catch (Exception ex) { 
			logger.error("getSectionSelectedByAdmin : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return map;
	}
	
	private Session OpenSession() {
		Session session = HibernateUtil.openSession();
		return session;
	}

	@Override
	public int insertRecieveSection(BookRecieveSection recive) throws Exception {
		BigInteger result = new BigInteger("0");
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		
		try{
			Query query = session.createSQLQuery("INSERT INTO BOOK_RECIEVE_SECTION (BR_ID, BR_TO_DEPARTMENT, BR_TO_GROUP, BR_TO_SECTION_NAME, STATUS, UPDATED_BY, UPDATED_DATE, BR_TO_SECTION)"
					+ " VALUES (:BR_ID, :BR_TO_DEPARTMENT, :BR_TO_GROUP, :BR_TO_SECTION_NAME, :STATUS, :UPDATED_BY, SYSDATE(), :BR_TO_SECTION) ");
			
			query.setParameter("BR_ID", recive.getBrId());
			query.setParameter("BR_TO_DEPARTMENT", recive.getBrToDepartment());
			query.setParameter("BR_TO_GROUP", recive.getBrToGroup());
			query.setParameter("BR_TO_SECTION_NAME", recive.getBrToSectionName());
			query.setParameter("STATUS", recive.getStatus());
			query.setParameter("UPDATED_BY", recive.getUpdatedBy());
			query.setParameter("BR_TO_SECTION", recive.getBrToSection());
			query.executeUpdate();
			result = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
			tx.commit();
		}catch(Exception ex){
			logger.error("insertRecieveGroup : ", ex);
			tx.rollback();
			throw ex;
		}finally{
			HibernateUtil.close(session);
		}
		return result.intValue();
	}

	@Override
	public String deleteRecieveSectionByBrId(int brId) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		String rs = "0";
		try{
			session.createSQLQuery("delete from Book_Recieve_Section where br_Id = :brId")
				.setParameter("brId", brId)
				.executeUpdate();
			tx.commit();
			rs = "1";
		} catch (Exception ex) {
			logger.error("deleteRecieveSectionByBrId : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return rs;
	}

	@Override
	public Sections getSectionById(Integer id) {
		Sections sections = null;
		Session session = OpenSession();
		try {
			String sql = "SELECT a.ID,";
			sql	+= "a.GROUP_ID,";
			sql += "a.SECTION_NAME";
			sql += " FROM sections a ";
			sql += " WHERE a.ID = '" + id + "'";
			List<Sections> data = session.createSQLQuery(sql).addEntity(Sections.class).list();
			if(data.size() > 0){
				sections = data.get(0);
			}
		} catch (Exception ex) { 
			logger.error("getSectionByBrId : ", ex);
		}finally{
			HibernateUtil.close(session);
		}
		return sections;
	}
	
	
	@Override
	public Integer getNextRunningNoDepartment(int brYear, String brToDepartment) {
		Integer id = 0; 
		Integer brNum = 1;
		Session session = OpenSession();
		try{
			
			Query sql = session.createSQLQuery("SELECT a.RUNNING_NO FROM BOOK_RECIEVE_DEPARTMENT a INNER JOIN BOOK_RECIVE_OUT b ON a.BR_ID = b.BR_ID WHERE b.BR_YEAR = :BR_YEAR AND a.BR_TO_DEPARTMENT = :BR_TO_DEPARTMENT AND a.RUNNING_NO IS NOT NULL ORDER BY a.BR_DEPARTMENT_ID DESC LIMIT 1");
			sql.setParameter("BR_YEAR", brYear);
			sql.setParameter("BR_TO_DEPARTMENT", brToDepartment);
			id =  (Integer) sql.uniqueResult();
			brNum = (id == null) ? 1 : id + 1;
		}catch(Exception ex){
			logger.error("getNextRunningNoDepartment : ", ex);
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
		return brNum;
	}
	
	@Override
	public int updateReciveOutBoard(BookRecieveBoard recive) {
		Session session = OpenSession();
		Transaction tx = session.beginTransaction();
		int result = 0;
		try{
			Query query = session.createQuery("update BookRecieveBoard set status = :status, updatedDate = SYSDATE(), updatedBy = :updatedBy, remark = :remark where brId = :brId and brToBoard = :brToBoard");
			query.setParameter("status", recive.getStatus());
			query.setParameter("updatedBy", recive.getUpdatedBy());
			query.setParameter("remark", recive.getRemark());
			query.setParameter("brId", recive.getBrId());
			query.setParameter("brToBoard", recive.getBrToBoard());
			result = query.executeUpdate();
			
			List<BookRecieveBoard> data = session.createSQLQuery("select * from Book_Recieve_Board where BR_ID = '" + recive.getBrId() + "'").addEntity(BookRecieveBoard.class).list();
			boolean flag = true;
			for(BookRecieveBoard u : data) {
				if("N".equals(u.getStatus())){
					flag = false;
				}
			}
			if(flag) {
				query = session.createQuery("update BookReciveOut set "
						+ "brStatus = :brStatus, "
						+ "updatedBy = :updatedBy, "
						+ "updatedDate = SYSDATE() "
						+ "where brId = :brId");
				query.setParameter("brStatus", recive.getStatus());
				query.setParameter("updatedBy", recive.getUpdatedBy());
				query.setParameter("brId", recive.getBrId());
				result = query.executeUpdate();
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("updateReciveOutBoard : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}
		return result;
	}
}
