package com.java.doc.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.User;

import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.model.StatusDetail;
import com.java.doc.model.Users;
import com.java.doc.util.TableSorter;

public interface BookReciveOutService {

	public List<BookReciveOut> listReciveByYear(int year);
	public long Count();
	public BookReciveOutTable ListPageRecive(TableSorter table);
	public int CountSelect();
	public int SaveReciveOut(BookReciveOut recive) throws Exception;
	public BookReciveOut getDataFromId(int brId, Users user);
	public int LastID();
	public Integer getNextBrNum(int brYear);
	public List<Integer> getYear();
	public String delete(BookReciveOut br) throws Exception;
	public int updateReciveOut(BookReciveOut recive, Users user) throws Exception;
	public int getCountDataBookReciveOut(int year);
	public BookReciveOut getLastRowOfYear(int brYear);
	public List<StatusDetail> getStatusDetail(String brId);
	public boolean insertBrToExcel(BookReciveOut recive) throws Exception;
	public List<BookReciveOut> listReciveByYearAndBrNum(int year, int brNum);
	public void SaveReciveOutFromExcel(BookReciveOut recive);
	public Map<String, List<String>> getGroupSelectedByAdmin(String departments);
	public Map<String, List<String>> getSectionSelectedByAdmin(String groups);
	public Map<String, List<String>> getUserSelectedByAdmin(String groups, String sections);
	public Map<String, List<String>> getUserForSectionRoleSelectedByAdmin(String groups, String sections);
}
