package com.java.doc.util;

import java.util.List;

public class TableSorter {
	public int page;
	private List<String> filter;
	private List<List<Integer>> sortList;
	private int size;
	private ObjSearch search;
	private String role;
	private String division;
	private String userId;
	
	public ObjSearch getSearch() {
		return search;
	}
	public void setSearch(ObjSearch search) {
		this.search = search;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<List<Integer>> getSortList() {
		return sortList;
	}
	public void setSortList(List<List<Integer>> sortList) {
		this.sortList = sortList;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<String> getFilter() {
		return filter;
	}
	public void setFilter(List<String> filter) {
		this.filter = filter;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public class ObjSearch {
		private String type;
		private String startDate;
		private String endDate;
		private String subject;
		private String num;
		private String remark;
		private String from;
		private String to;
		private String year;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		
		
	}
}

