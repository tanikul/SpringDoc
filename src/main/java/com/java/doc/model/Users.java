package com.java.doc.model;

// Generated Apr 20, 2015 2:42:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Users generated by hbm2java
 */
@Entity
@Table(name = "users", catalog = "document")
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = -3417087601947845069L;
	private Integer id;
	private String username;
	private String password;
	private String prefix;
	private String fname;
	private String lname;
	private String role;
	private String division;
	private Integer groupId;
	private Integer sectionId;

	public Users() {
	}

	public Users(String username, String password, String fname, String lname, String role, String division, Integer groupId, String prefix) {
		this.username = username;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.role = role;
		this.groupId = groupId;
		this.prefix = prefix;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "username", length = 100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "fname", length = 100)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "lname", length = 100)
	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Column(name = "role", length = 10)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Column(name = "division", length = 10)
	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
	
	@Column(name = "group_id", length = 2)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "prefix", length = 20)
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Column(name = "section_id", length = 2)
	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

}
