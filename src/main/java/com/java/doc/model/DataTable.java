package com.java.doc.model;

import java.util.List;

public class DataTable {

	private List<Column> columns;
	private int draw;
	private int length;
	private String myKey;
	private List<Order> order;
	private Search search;
	
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getMyKey() {
		return myKey;
	}
	public void setMyKey(String myKey) {
		this.myKey = myKey;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
		
}
