package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_recieve_user", catalog = "document")
public class BookRecieveUser implements java.io.Serializable {

	private static final long serialVersionUID = 7498966739305815929L;

	private Integer brUserId;
	private Integer brId;
	private String brToUser;
	private String brToDepartment;
	private String brToGroup;
	private String brToUserName;
	private String status;
	private String updatedBy;
	private Date updatedDate;
	private String brToSection;
	
	public BookRecieveUser() {
	}

	@Override
	public String toString(){
		String str = " brUserId : " + brUserId + ", "
				+ " brId : " + brId + ", "
				+ " brToDepartment : " + brToDepartment + ", "
				+ " brToUser : " + brToUser + ", "
				+ " brToGroup : " + brToGroup + ", "
				+ " brToUserName : " + brToUserName + ", "
				+ " updatedBy : " + updatedBy + ", "
				+ " updatedDate : " + updatedDate + ", "
				+ " status : " + status;
		return str;
	}
	
	public BookRecieveUser(Integer brId, Integer brUserId, String brToUser,
			String brToUserName, String status, String updatedBy, Date updatedDate, String brToGroup, String brToDepartment) {
		this.brUserId = brUserId;
		this.brId = brId;
		this.brToUser = brToUser;
		this.brToGroup = brToGroup;
		this.brToUserName = brToUserName;
		this.status = status;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.brToDepartment = brToDepartment;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "BR_USER_ID", unique = true, nullable = false)
	public Integer getBrUserId() {
		return this.brUserId;
	}

	public void setBrUserId(Integer brUserId) {
		this.brUserId = brUserId;
	}
	
	@Column(name = "BR_ID")
	public Integer getBrId() {
		return this.brId;
	}

	public void setBrId(Integer brId) {
		this.brId = brId;
	}
	
	@Column(name = "BR_TO_DEPARTMENT")
	public String getBrToDepartment() {
		return this.brToDepartment;
	}

	public void setBrToDepartment(String brToDepartment) {
		this.brToDepartment = brToDepartment;
	}

	@Column(name = "BR_TO_USER")
	public String getBrToUser() {
		return this.brToUser;
	}

	public void setBrToUser(String brToUser) {
		this.brToUser = brToUser;
	}
	
	@Column(name = "BR_TO_GROUP")
	public String getBrToGroup() {
		return this.brToGroup;
	}

	public void setBrToGroup(String brToGroup) {
		this.brToGroup = brToGroup;
	}
	
	@Column(name = "BR_TO_USER_NAME")
	public String getBrToUserName() {
		return brToUserName;
	}

	public void setBrToUserName(String brToUserName) {
		this.brToUserName = brToUserName;
	}
	
	@Column(name = "BR_TO_SECTION")
	public String getBrToSection() {
		return brToSection;
	}

	public void setBrToSection(String brToSection) {
		this.brToSection = brToSection;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}	
	
	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	
}
