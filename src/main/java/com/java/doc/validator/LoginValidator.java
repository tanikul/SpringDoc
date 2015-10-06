package com.java.doc.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginValidator {
	
	@NotEmpty(message = "กรุณากรอก username")
	private String username;
	
	@NotEmpty(message = "กรุณากรอก password")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}