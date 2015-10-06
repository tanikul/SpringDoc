package com.java.doc.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class BookSendOutValidator {

//	@NotEmpty(message = "กรุณากรอกวันที่")
	private Integer bsId;
	private String bsNum;
	private String bsTypeQuick;
	private String bsTypeSecret;
	private Integer bsYear;
	private String bsRdate;
	private String bsPlace;
	private String bsDate;
	private String bsFrom;
	private String bsTo;
	private String bsSubject;
	private String bsRemark;
	private String bsDivision;
	private String bsPcode;
	private String bsStatus;
	private String bsImage;
	public Integer getBsId() {
		return bsId;
	}
	public void setBsId(Integer bsId) {
		this.bsId = bsId;
	}
	public String getBsNum() {
		return bsNum;
	}
	public void setBsNum(String bsNum) {
		this.bsNum = bsNum;
	}
	public String getBsTypeQuick() {
		return bsTypeQuick;
	}
	public void setBsTypeQuick(String bsTypeQuick) {
		this.bsTypeQuick = bsTypeQuick;
	}
	public String getBsTypeSecret() {
		return bsTypeSecret;
	}
	public void setBsTypeSecret(String bsTypeSecret) {
		this.bsTypeSecret = bsTypeSecret;
	}
	public Integer getBsYear() {
		return bsYear;
	}
	public void setBsYear(Integer bsYear) {
		this.bsYear = bsYear;
	}
	public String getBsRdate() {
		return bsRdate;
	}
	public void setBsRdate(String bsRdate) {
		this.bsRdate = bsRdate;
	}
	public String getBsPlace() {
		return bsPlace;
	}
	public void setBsPlace(String bsPlace) {
		this.bsPlace = bsPlace;
	}
	public String getBsDate() {
		return bsDate;
	}
	public void setBsDate(String bsDate) {
		this.bsDate = bsDate;
	}
	public String getBsFrom() {
		return bsFrom;
	}
	public void setBsFrom(String bsFrom) {
		this.bsFrom = bsFrom;
	}
	public String getBsTo() {
		return bsTo;
	}
	public void setBsTo(String bsTo) {
		this.bsTo = bsTo;
	}
	public String getBsSubject() {
		return bsSubject;
	}
	public void setBsSubject(String bsSubject) {
		this.bsSubject = bsSubject;
	}
	public String getBsRemark() {
		return bsRemark;
	}
	public void setBsRemark(String bsRemark) {
		this.bsRemark = bsRemark;
	}
	public String getBsDivision() {
		return bsDivision;
	}
	public void setBsDivision(String bsDivision) {
		this.bsDivision = bsDivision;
	}
	public String getBsPcode() {
		return bsPcode;
	}
	public void setBsPcode(String bsPcode) {
		this.bsPcode = bsPcode;
	}
	public String getBsStatus() {
		return bsStatus;
	}
	public void setBsStatus(String bsStatus) {
		this.bsStatus = bsStatus;
	}
	public String getBsImage() {
		return bsImage;
	}
	public void setBsImage(String bsImage) {
		this.bsImage = bsImage;
	}

}
