package com.java.doc.service;
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.UserDAO;
import com.java.doc.model.Users;
 
@Service("myDetailService")
public class MyDetailService implements UserDetailsService {
 
	@Autowired
	@Qualifier(value = "userDao")
	private UserDAO userDao;
 
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String username) 
               throws UsernameNotFoundException {
 
		Users user = userDao.findByUserName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRole());
		return buildUserForAuthentication(user, authorities);
	}
 
	private User buildUserForAuthentication(Users user, 
		List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(), authorities);
	}
 
	private List<GrantedAuthority> buildUserAuthority(String userRole) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		setAuths.add(new SimpleGrantedAuthority(userRole));
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
}