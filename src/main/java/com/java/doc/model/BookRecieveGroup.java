package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_recieve_group", catalog = "document")
public class BookRecieveGroup implements java.io.Serializable {


	private static final long serialVersionUID = -5843021613443571317L;
	private Integer brGroupId;
	private Integer brId;
	private String brToDepartment;
	private String brToGroup;
	private String brToGroupName;
	private String status;
	private String updatedBy;
	private Date updatedDate;
	private String remark;
	
	public BookRecieveGroup() {
	}

	@Override
	public String toString(){
		String str = " brGroupId : " + brGroupId + ", "
				+ " brId : " + brId + ", "
				+ " brToDepartment : " + brToDepartment + ", "
				+ " brToGroup : " + brToGroup + ", "
				+ " brToGroupName : " + brToGroupName + ", "
				+ " updatedBy : " + updatedBy + ", "
				+ " updatedDate : " + updatedDate + ", "
				+ " status : " + status;
		return str;
	}
	
	public BookRecieveGroup(Integer brId, Integer brGroupId, String brToDepartment,
			String brToGroup, String brToGroupName, String status, String updatedBy, Date updatedDate) {
		this.brGroupId = brGroupId;
		this.brId = brId;
		this.brToDepartment = brToDepartment;
		this.brToGroup = brToGroup;
		this.brToGroupName = brToGroupName;
		this.status = status;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "BR_GROUP_ID", unique = true, nullable = false)
	public Integer getBrToGroupId() {
		return this.brGroupId;
	}

	public void setBrToGroupId(Integer brGroupId) {
		this.brGroupId = brGroupId;
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
	
	@Column(name = "BR_TO_GROUP")
	public String getBrToGroup() {
		return brToGroup;
	}

	public void setBrToGroup(String brToGroup) {
		this.brToGroup = brToGroup;
	}

	@Column(name = "BR_TO_GROUP_NAME")
	public String getBrToGroupName() {
		return brToGroupName;
	}

	public void setBrToGroupName(String brToGroupName) {
		this.brToGroupName = brToGroupName;
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
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
}
