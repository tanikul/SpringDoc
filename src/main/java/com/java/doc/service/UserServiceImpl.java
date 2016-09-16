package com.java.doc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.UserDAO;
import com.java.doc.model.DataTable;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;

@Service
public class UserServiceImpl implements UserService {

	private UserDAO users;
	
	public void setUsers(UserDAO u){
		this.users = u;
	}
	
	@Override
	@Transactional
	public void addUser(Users u) {
		this.users.addUser(u);
	}

	@Override
	@Transactional
	public void updateUser(Users u) {
		this.users.updateUser(u);
	}

	@Override
	@Transactional
	public List<Users> listUser() {
		return this.users.listUser();
	}

	@Override
	@Transactional
	public Users getUserById(int id) {
		return this.users.getUserById(id);
	}

	@Override
	@Transactional
	public void removeUser(int id) {
		this.users.removeUser(id);
	}

	@Override
	public Users findByUserName(String username) {
		return this.users.findByUserName(username);
	}

	@Override
	public List<UserTable> searchUser(DataTable data) {
		return users.getSearchUser(data);
	}

	@Override
	public UserTable findUserById(int id) {
		return users.getFindUserById(id);
	}

}
