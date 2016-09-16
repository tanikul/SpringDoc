package com.java.doc.model;

import java.io.Serializable;
import java.util.List;

public class BookReciveOutTable implements Serializable {

	private static final long serialVersionUID = -7583325724192914992L;
	private int countSelect;
	private List<BookReciveOut> sendoutList;
	private List<BookReciveOut> sendoutListReport;
	
	public int getCountSelect() {
		return countSelect;
	}
	public void setCountSelect(int countSelect) {
		this.countSelect = countSelect;
	}
	public List<BookReciveOut> getSendoutList() {
		return sendoutList;
	}
	public void setSendoutList(List<BookReciveOut> sendoutList) {
		this.sendoutList = sendoutList;
	}
	public List<BookReciveOut> getSendoutListReport() {
		return sendoutListReport;
	}
	public void setSendoutListReport(List<BookReciveOut> sendoutListReport) {
		this.sendoutListReport = sendoutListReport;
	}
}
