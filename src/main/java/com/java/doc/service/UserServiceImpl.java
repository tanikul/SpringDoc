package com.java.doc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.UserDAO;
import com.java.doc.model.DataTable;
import com.java.doc.model.Groups;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;

@Service("userService")
public class UserServiceImpl implements UserService {

	final static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	@Qualifier("userDao")
	private UserDAO userDao;
	
	@Override
	@Transactional
	public void addUser(Users u) {
		this.userDao.addUser(u);
	}

	@Override
	@Transactional
	public void updateUser(Users u) {
		this.userDao.updateUser(u);
	}

	@Override
	@Transactional
	public List<Users> listUser() {
		return this.userDao.listUser();
	}

	@Override
	@Transactional
	public Users getUserById(int id) {
		return this.userDao.getUserById(id);
	}

	@Override
	@Transactional
	public void removeUser(int id) {
		this.userDao.removeUser(id);
	}

	@Override
	public Users findByUserName(String username) {
		return this.userDao.findByUserName(username);
	}

	@Override
	public List<UserTable> searchUser(DataTable data) {
		return userDao.getSearchUser(data);
	}

	@Override
	public UserTable findUserById(int id) {
		return userDao.getFindUserById(id);
	}

	@Override
	public Map<Integer, String> getGroupFromDivisionDropDown(String divisionCode) {
		Map<Integer, String> map = null;
		try{
			map = new HashMap<Integer, String>();
			List<Groups> groups = userDao.getGroupFromDivision(divisionCode);
			for(Groups item : groups){
				map.put(item.getGroupId(), item.getGroupName());
			}
		}catch(Exception ex){
			logger.error("getGroupFromDivisionDropDown : " + ex.getMessage());
		}
		
		return map;
	}

	@Override
	public Map<Integer, String> getUserFromGroupDropDown(String groupId) {
		Map<Integer, String> map = null;
		try{
			map = new HashMap<Integer, String>();
			List<Users> user = userDao.getUserFromGroup(groupId);
			for(Users item : user){
				map.put(item.getId(), item.getPrefix() + item.getFname() + " " + item.getLname());
			}
		}catch(Exception ex){
			logger.error("getUserFromGroupDropDown : " + ex.getMessage());
		}
		
		return map;
	}

	@Override
	public Groups getGroupFromDivision(String divisionCode) {
		return userDao.getGroupFromDivision(divisionCode).get(0);
	}

	@Override
	public Users getUserFromGroup(String groupId) {
		return userDao.getUserFromGroup(groupId).get(0);
	}

	@Override
	public Groups getGroupName(String groupId) {
		// TODO Auto-generated method stub
		return userDao.getGroupName(groupId);
	}

}
