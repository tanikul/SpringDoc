package com.java.doc.model;

import java.io.Serializable;
import java.util.List;

public class BookSendOutTable implements Serializable {

	private static final long serialVersionUID = 8222298298182514158L;
	private int countSelect;
	private List<BookSendOut> sendoutList;
	private List<BookSendOut> sendoutListReport;
	
	public int getCountSelect() {
		return countSelect;
	}
	public void setCountSelect(int countSelect) {
		this.countSelect = countSelect;
	}
	public List<BookSendOut> getSendoutList() {
		return sendoutList;
	}
	public void setSendoutList(List<BookSendOut> sendoutList) {
		this.sendoutList = sendoutList;
	}
	public List<BookSendOut> getSendoutListReport() {
		return sendoutListReport;
	}
	public void setSendoutListReport(List<BookSendOut> sendoutListReport) {
		this.sendoutListReport = sendoutListReport;
	}
	
}
