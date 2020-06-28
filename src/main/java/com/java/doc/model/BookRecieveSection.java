package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_recieve_section", catalog = "document")
public class BookRecieveSection implements java.io.Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6030058537832131337L;
	private Integer brSectionId;
	private Integer brId;
	private String brToSection;
	private String brToDepartment;
	private String brToGroup;
	private String brToSectionName;
	private String status;
	private String updatedBy;
	private Date updatedDate;
	
	
	@Override
	public String toString(){
		String str = " brSectionId : " + brSectionId + ", "
				+ " brId : " + brId + ", "
				+ " brToDepartment : " + brToDepartment + ", "
				+ " brToSection : " + brToSection + ", "
				+ " brToGroup : " + brToGroup + ", "
				+ " brToSectionName : " + brToSectionName + ", "
				+ " updatedBy : " + updatedBy + ", "
				+ " updatedDate : " + updatedDate + ", "
				+ " status : " + status;
		return str;
	}
	public BookRecieveSection() {}
	public BookRecieveSection(Integer brId, Integer brSectionId, String brToSection,
			String brToSectionName, String status, String updatedBy, Date updatedDate, String brToGroup, String brToDepartment) {
		this.brSectionId = brSectionId;
		this.brId = brId;
		this.brToSection = brToSection;
		this.brToGroup = brToGroup;
		this.brToSectionName = brToSectionName;
		this.status = status;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.brToDepartment = brToDepartment;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "BR_SECTION_ID", unique = true, nullable = false)
	public Integer getBrSectionId() {
		return this.brSectionId;
	}

	public void setBrSectionId(Integer brSectionId) {
		this.brSectionId = brSectionId;
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

	@Column(name = "BR_TO_SECTION")
	public String getBrToSection() {
		return this.brToSection;
	}

	public void setBrToSection(String brToSection) {
		this.brToSection = brToSection;
	}
	
	@Column(name = "BR_TO_GROUP")
	public String getBrToGroup() {
		return this.brToGroup;
	}

	public void setBrToGroup(String brToGroup) {
		this.brToGroup = brToGroup;
	}
	
	@Column(name = "BR_TO_USECTION")
	public String getBrToSectionName() {
		return brToSectionName;
	}

	public void setBrToSectionName(String brToSectionName) {
		this.brToSectionName = brToSectionName;
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
