package com.java.doc.dao;

import java.util.List;

import com.java.doc.model.DataTable;
import com.java.doc.model.Groups;
import com.java.doc.model.Sections;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;

public interface UserDAO {

	public void addUser(Users u);
	public void updateUser(Users u);
	public List<Users> listUser();
	public Users getUserById(int id);
	public void removeUser(int id);
	public List<UserTable> getSearchUser(DataTable data);
	public UserTable getFindUserById(int id);
	public List<Groups> getGroupFromDivision(String divisionCode);
	public List<Users> getUserFromGroup(String groupId);
	public Groups getGroupName(String groupId);
	public List<Sections> getSectionFromGroupDropDown(String groupId);
	public Sections getSectionName(String sectionId);
	public Users findByUserName(String username);
}
