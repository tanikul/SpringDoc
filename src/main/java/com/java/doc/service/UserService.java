package com.java.doc.service;

import java.util.List;

import com.java.doc.model.DataTable;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;

public interface UserService {
	
	public void addUser(Users u);
	public void updateUser(Users u);
	public List<Users> listUser();
	public Users getUserById(int id);
	public void removeUser(int id);
	public List<UserTable> searchUser(DataTable data);
	public UserTable findUserById(int id);
	
	Users findByUserName(String username);
}
