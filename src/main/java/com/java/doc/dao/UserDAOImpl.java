package com.java.doc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.hibernate.HibernateUtil;
import com.java.doc.model.DataTable;
import com.java.doc.model.Groups;
import com.java.doc.model.Sections;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;
import com.java.doc.util.Constants;

@Repository("userDao")
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
	public Users getUserById(int id) {
		startOperation();
		Users users = null;
		try {
			users = (Users) session.createQuery("from Users where id=?").setParameter(0, id).uniqueResult();
		}finally {
			HibernateUtil.close(session);
		}	
		return users;
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
	
	@Override
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
			String strSQL = "SELECT u.id, u.fname, u.lname, u.role, d.division_name, d.division_code, g.group_name, u.prefix, s.section_name FROM users u LEFT JOIN divisions d ON u.division = d.division_code LEFT JOIN groups g ON u.group_id = g.group_id LEFT JOIN sections s ON u.section_id = s.id";
			SQLQuery query = session.createSQLQuery(strSQL);
			List<?> list = query.list();
			Iterator<?> it = list.iterator();
			int i = 1;
			Map<String, String> roles = Constants.getRoles(); 
			while(it.hasNext()){
				Object[] row = (Object[]) it.next();
				UserTable t = new UserTable();
				t.setSeq(i);
				t.setId(Integer.parseInt((row[0] == null) ? "" : row[0].toString()));
				String s = "";
				if(row[7] != null){
					s += row[7];
				}
				if(row[1] != null){
					s += row[1];
				}
				t.setFname(s);
				t.setLname((row[2] == null) ? "" : row[2].toString());
				t.setRole((row[3] == null) ? "" : roles.get(row[3].toString()));
				t.setDivisionName((row[4] == null) ? "" : row[4].toString());
				t.setDivisionCode((row[5] == null) ? "" : row[5].toString());
				t.setGroupName((row[6] == null) ? "" : row[6].toString());
				t.setSectionName((row[8] == null) ? "" : row[8].toString());
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
			String strSQL = "SELECT u.id, u.fname, u.lname, u.role, d.division_name, d.division_code, d.organization, u.username, u.password, u.group_id, g.group_name, u.prefix, s.id section_id, s.section_name FROM users u LEFT JOIN divisions d ON u.division = d.division_code LEFT JOIN groups g ON u.group_id = g.group_id LEFT JOIN sections s ON g.group_id = s.group_id WHERE u.id = " + id;
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
				result.setGroupId(row[9] == null ? null : Integer.parseInt(row[9].toString()));
				result.setGroupName(row[10] == null ? "" : row[10].toString());
				result.setPrefix(row[11] == null ? "" : row[11].toString());
				result.setSectionId(row[12] == null ? null : Integer.parseInt(row[12].toString()));
				result.setSectionName(row[13] == null ? "" : row[13].toString());
				result.setButton("<button type='button' class='btn btn-warning' onclick='editUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> เน�เธ�เน�เน�เธ�</button> <button type='button' class='btn btn-danger' onclick='deleteUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> เธฅเธ�</button>");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Groups> getGroupFromDivision(String divisionCode) {
		startOperation();
		List<Groups> group = null;
		try {
			group = session.createQuery("from Groups where divisionCode=?").setParameter(0, divisionCode).list();
		}catch(Exception ex){
			logger.error("getGroupFromDivision Exception : " , ex); 
		}finally {
			HibernateUtil.close(session);
		}	
		return group;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getUserFromGroup(String groupId) {
		startOperation();
		List<Users> users = null;
		try {
			users = session.createQuery("from Users where groupId=? and role='USER'").setParameter(0, Integer.parseInt(groupId)).list();
		}catch(Exception ex){
			logger.error("getUserFromGroup Exception : " , ex); 
		}finally {
			HibernateUtil.close(session);
		}	
		return users;
	}
	
	@Override
	public Groups getGroupName(String groupId) {
		startOperation();
		Groups group = null;
		try {
			group = (Groups) session.createQuery("from Groups where groupId=?").setParameter(0, Integer.parseInt(groupId)).uniqueResult();
		}catch(Exception ex){
			logger.error("getGroupName Exception : " , ex); 
		}finally {
			HibernateUtil.close(session);
		}	
		return group;
	}

	@Override
	public List<Sections> getSectionFromGroupDropDown(String groupId) {
		startOperation();
		List<Sections> sections = null;
		try {
			//System.out.println(groupId);
			sections = session.createQuery("from Sections where groupId=?").setParameter(0, Integer.parseInt(groupId)).list();
		}catch(Exception ex){
			logger.error("getSectionFromGroupDropDown Exception : " , ex); 
		}finally {
			HibernateUtil.close(session);
		}	
		return sections;
	}
}
