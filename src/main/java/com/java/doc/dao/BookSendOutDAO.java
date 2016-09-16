package com.java.doc.dao;

import java.util.List;

import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.util.TableSorter;

public interface BookSendOutDAO {

	public void save(BookSendOut sendout);
	public boolean saveOrUpdate(BookSendOut recive);
	public boolean merge(BookSendOut recive);
	public List<BookSendOut> listSendOut();
	public BookSendOutTable ListPageSendOut(TableSorter table);
	public BookSendOut getDataFromId(int id);
	public int CountSelect();
	public int LastID(); 
	public Integer getNextBsNum(int bsYear);
	public List<Integer> getYear();
	public String delete(int id);
}
