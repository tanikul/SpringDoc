package com.java.doc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.DataTable;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;

@Repository
public class UserDAOImpl implements UserDAO {


	protected Session session;
    protected Transaction tx;
    public UserDAOImpl() {
        HibernateUtil.buildIfNeeded();
    }
	final static Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	@Override
	public void addUser(Users u) {
		startOperation();
		try {
			session.merge(u);
			tx.commit();
			logger.debug("addUser : "+ u);
		} catch (HibernateException ex) {
			logger.debug("addUser HibernateException : ", ex);
			tx.rollback();
		}catch (Exception ex) {
			logger.debug("addUser Exception : ", ex);
			tx.rollback();
		}finally{
			HibernateUtil.close(session);
		}		
	}

	@Override
	public void updateUser(Users u) {
		startOperation();
		try {
			tx = session.beginTransaction();
			session.update(u);
			tx.commit();
		} catch (HibernateException ex) {
			logger.debug("updateUser HibernateException : ", ex);
			tx.rollback();
		} catch (Exception ex) {
			logger.debug("updateUser Exception : ", ex);
			tx.rollback();
		} finally {
			HibernateUtil.close(session);
		}			
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Users> listUser() {
		startOperation();
		List<Users> user = null;
		try {
			user = session.createQuery("from Users").list();
		}finally {
			HibernateUtil.close(session);
		}	
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Users getUserById(int id) {
		startOperation();
		Users u = null;
		try {
			u = (Users) session.load(Users.class, new Integer(id));
		}finally {
			HibernateUtil.close(session);
		}	
		return u;
	}

	@Override
	public void removeUser(int id) {
		startOperation();
		try {
			Users u = (Users) session.get(Users.class, new Integer(id));
			if (u != null) {
				session.delete(u);
			}
			tx.commit();
		} catch (HibernateException ex) {
			logger.debug("removeUser HibernateException : ", ex);
			tx.rollback();
		}catch (Exception ex) {
			logger.debug("removeUser Exception : ", ex);
			tx.rollback();
		} finally {
			HibernateUtil.close(session);
		}
	}

	@Transactional(readOnly = true)
	public Users findByUserName(String username) {
		startOperation();
		Users users = new Users();
		try {
			users = (Users) session.createQuery("from Users where username=?").setParameter(0, username).uniqueResult();
			/*Criteria query = session.createCriteria(Users.class);
			query.add(Restrictions.eq("username", username));
			users = (Users) query.uniqueResult();*/
			logger.debug("findByUserName : "+ username);
			return users;
		} catch (HibernateException ex) {
			logger.error("findByUserName HibernateException : " , ex);
		} catch(Exception e){
			logger.error("findByUserName Exception : " , e);
		}finally{
			HibernateUtil.close(session);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserTable> getSearchUser(DataTable data) {
		startOperation();
		List<UserTable> users = new ArrayList<UserTable>();
		try {
			String strSQL = "SELECT u.id, u.fname, u.lname, u.role, d.division_name, d.division_code, d.organization FROM users u, divisions d WHERE u.division = d.division_code";
			SQLQuery query = session.createSQLQuery(strSQL);
			List<?> list = query.list();
			Iterator<?> it = list.iterator();
			int i = 1;
			while(it.hasNext()){
				Object[] row = (Object[]) it.next();
				UserTable t = new UserTable();
				t.setSeq(i);
				t.setId(Integer.parseInt((row[0] == null) ? "" : row[0].toString()));
				t.setFname((row[1] == null) ? "" : row[1].toString());
				t.setLname((row[2] == null) ? "" : row[2].toString());
				t.setRole((row[3] == null) ? "" : row[3].toString());
				t.setDivisionName((row[4] == null) ? "" : row[4].toString());
				t.setDivisionCode((row[5] == null) ? "" : row[5].toString());
				t.setOrganization((row[6] == null) ? "" : row[6].toString());
				t.setButton("<button type='button' class='btn btn-warning' onclick='editUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> แก้ไข</button> <button type='button' class='btn btn-danger' onclick='deleteUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> ลบ</button>");
				users.add(t);
				i++;
			}
		}catch(HibernateException ex){
			logger.error("getSearchUser HibernateException : " , ex); 
		}catch(Exception ex){
			logger.error("getSearchUser Exception : " , ex); 
		}finally{
			HibernateUtil.close(session);
		}
		
		return users;
	}

	@Override
	public UserTable getFindUserById(int id) {
		UserTable result = new UserTable();
		startOperation();
		try {
			String strSQL = "SELECT u.id, u.fname, u.lname, u.role, d.division_name, d.division_code, d.organization, u.username, u.password FROM users u, divisions d WHERE u.division = d.division_code AND u.id = " + id;
			SQLQuery query = session.createSQLQuery(strSQL);
			List<?> list = query.list();
			Iterator<?> it = list.iterator();
			if(it.hasNext()){
				Object[] row = (Object[]) it.next();
				result.setId(Integer.parseInt((row[0] == null) ? "" : row[0].toString()));
				result.setFname((row[1] == null) ? "" : row[1].toString());
				result.setLname((row[2] == null) ? "" : row[2].toString());
				result.setRole((row[3] == null) ? "" : row[3].toString());
				result.setDivisionName((row[4] == null) ? "" : row[4].toString());
				result.setDivisionCode((row[5] == null) ? "" : row[5].toString());
				result.setOrganization((row[6] == null) ? "" : row[6].toString());
				result.setUsername(row[7] == null ? "" : row[7].toString());
				result.setPassword(row[8] == null ? "" : row[8].toString());
				result.setButton("<button type='button' class='btn btn-warning' onclick='editUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> แก้ไข</button> <button type='button' class='btn btn-danger' onclick='deleteUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> ลบ</button>");
			}
		}catch(HibernateException ex){
			logger.error("getFindUserById HibernateException : " , ex); 
		}catch(Exception ex){
			logger.error("getFindUserById Exception : " , ex); 
		}finally{
			HibernateUtil.close(session);
		}
		
		return result;
	}
	
	protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();
    }
}
