package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_recieve_board", catalog = "document")
public class BookRecieveBoard implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409355971084690232L;
	
	private Integer brBoardId;
	private Integer brId;
	private String brToBoard;
	private String brToBoardName;
	private String status;
	private String updatedBy;
	private Date updatedDate;
	private String remark;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "BR_BOARD_ID", unique = true, nullable = false)
	public Integer getBrBoardId() {
		return this.brBoardId;
	}

	public void setBrBoardId(Integer brBoardId) {
		this.brBoardId = brBoardId;
	}
	
	@Column(name = "BR_ID")
	public Integer getBrId() {
		return this.brId;
	}

	public void setBrId(Integer brId) {
		this.brId = brId;
	}

	@Column(name = "BR_TO_BOARD")
	public String getBrToBoard() {
		return this.brToBoard;
	}

	public void setBrToBoard(String brToBoard) {
		this.brToBoard = brToBoard;
	}
	
	@Column(name = "BR_TO_BOARD_NAME")
	public String getBrToBoardName() {
		return brToBoardName;
	}

	public void setBrToBoardName(String brToBoardName) {
		this.brToBoardName = brToBoardName;
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
