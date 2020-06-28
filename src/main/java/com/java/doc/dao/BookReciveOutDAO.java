package com.java.doc.dao;
// master 5
import java.util.List;
import java.util.Map;

import com.java.doc.model.BookRecieveDepartment;
import com.java.doc.model.BookRecieveGroup;
import com.java.doc.model.BookRecieveSection;
import com.java.doc.model.BookRecieveUser;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.model.Sections;
import com.java.doc.model.StatusDetail;
import com.java.doc.model.Users;
import com.java.doc.util.TableSorter;

import net.sf.jasperreports.engine.JRDataSource;

public interface BookReciveOutDAO {

	public List<BookReciveOut> listReciveByYear(int year);
	public long Count();
	public BookReciveOutTable ListPageRecive(TableSorter table);
	public int CountSelect();
	public int Save(BookReciveOut recive);
	public boolean saveOrUpdate(BookReciveOut recive);
	public boolean merge(BookReciveOut recive);
	public BookReciveOut getDataFromId(int brId, Users user);
	public int LastID();
	public Integer getNextBrNum(int brYear);
	public List<Integer> getYear();
	public String delete(int id);
	public int updateReciveOutAdmin(BookReciveOut recive);
	public int updateReciveOutDepartment(BookRecieveDepartment recive);
	public int updateReciveOutGroup(BookRecieveGroup recive);
	public int updateReciveOutUser(BookRecieveUser recive);
	public int getCountDataBookRecive(int year);
	public BookReciveOut getLastRowOfYear(int brYear);
	public int insertRecieveDepartment(BookRecieveDepartment recive) throws Exception;
	public String deleteRecieveDepartment(BookReciveOut bookReciveOut, String brToNotIn);
	public String deleteRecieveGroup(BookRecieveDepartment bookRecieveDepartment, String brToNotIn);
	public String deleteRecieveUser(BookRecieveGroup bookRecieveGroup, String brToNotIn);
	public String deleteRecieveUserByBrId(int brId);
	public String deleteRecieveSectionByBrId(int brId);
	int insertRecieveUser(BookRecieveUser recive) throws Exception;
	int insertRecieveGroup(BookRecieveGroup recive) throws Exception;
	int insertRecieveSection(BookRecieveSection recive) throws Exception;
	public List<StatusDetail> getStatusDetail(String brId);
	public List<BookReciveOut> listReciveByYearAndBrNum(int year, int brNum);
	public Map<String, List<String>> getGroupSelectedByAdmin(String departments);
	public Map<String, List<String>> getSectionSelectedByAdmin(String groups);
	public Map<String, List<String>> getUserSelectedByAdmin(String groups, String sections);
	public List<BookRecieveDepartment> getBookRecieveOutDepartment(int brId);
	public List<BookRecieveGroup> getBookRecieveOutGroup(int brId, String brToDepartment);
	public List<BookRecieveUser> getBookRecieveOutUser(int brId, String brToDepartment, String brToGroup);
	public Sections getSectionById(Integer id);
}
