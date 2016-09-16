package com.java.doc.service;

import java.util.List;

import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.util.TableSorter;
import com.java.doc.validator.BookSendOutValidator;

public interface BookSendOutService {
	
	public boolean saveBookOut(BookSendOut sendout);
	public boolean saveOrUpdate(BookSendOut recive);
	public boolean merge(BookSendOut recive);
	public BookSendOut convert(BookSendOutValidator send);
	public List<BookSendOut> listSendOut();
	public BookSendOutTable ListPageSendOut(TableSorter table);
	public BookSendOut getDataFromId(int id);
	public int CountSelect();
	public int LastID();
	public Integer getNextBsNum(int bsYear);
	public List<Integer> getYear();
	public String delete(int id);
}
