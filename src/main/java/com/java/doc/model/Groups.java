package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups", catalog = "document")
public class Groups implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1146422291880739699L;
	private String divisionCode;
	private Integer groupId;
	private String groupName;
	

	public Groups(String divisionCode, Integer groupId, String groupName) {
		this.divisionCode = divisionCode;
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	@Column(name = "division_code", length = 10)
	public String getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "group_id", unique = true, nullable = false)  
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "group_name", length = 255)
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Groups() {
	}
}
