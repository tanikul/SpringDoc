package com.java.doc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


	private Session session = HibernateUtil.getSessionFactory().openSession();
	
	@Override
	public void addUser(Users u) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(u);
			tx.commit();
		} catch (HibernateException ex) {
			tx.rollback();
		}finally{
			session.clear();
		}		
	}

	@Override
	public void updateUser(Users u) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(u);
			tx.commit();
		} catch (HibernateException ex) {
			tx.rollback();
		} finally {
			session.clear();
		}			
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Users> listUser() {
		List<Users> user = session.createQuery("from Users").list();
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Users getUserById(int id) {
		Users u = (Users) session.load(Users.class, new Integer(id));
		return u;
	}

	@Override
	public void removeUser(int id) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Users u = (Users) session.get(Users.class, new Integer(id));
			if (u != null) {
				session.delete(u);
			}
			tx.commit();
		} catch (HibernateException ex) {
			tx.rollback();
		} finally {
			session.clear();
		}
	}

	@SuppressWarnings("unchecked")
	public Users findByUserName(String username) {
		List<Users> users = new ArrayList<Users>();
		try {
			users = session.createQuery("from Users where username=?").setParameter(0, username).list();
			if (users.size() > 0) {
				return users.get(0);
			} 
		} catch (HibernateException ex) {
			System.out.println("Error : " + ex.getMessage()); 
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserTable> getSearchUser(DataTable data) {
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
			System.out.println("Error : " + ex.getMessage()); 
		}
		
		return users;
	}

	@Override
	public UserTable getFindUserById(int id) {
		UserTable result = new UserTable();
		try {
			String strSQL = "SELECT u.id, u.fname, u.lname, u.role, d.division_name, d.division_code, d.organization FROM users u, divisions d WHERE u.division = d.division_code AND u.id = " + id;
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
				result.setButton("<button type='button' class='btn btn-warning' onclick='editUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> แก้ไข</button> <button type='button' class='btn btn-danger' onclick='deleteUser(" + row[0].toString() + ");'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> ลบ</button>");
			}
		}catch(HibernateException ex){
			System.out.println("Error : " + ex.getMessage()); 
		}
		
		return result;
	}
}
