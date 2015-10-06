package com.java.doc.dao;

import java.util.List;

import com.java.doc.model.BookSendOut;
import com.java.doc.util.TableSorter;

public interface BookSendOutDAO {

	public void save(BookSendOut sendout);
	public List<BookSendOut> listSendOut();
	public List<BookSendOut> ListPageSendOut(TableSorter table);
	public int CountSelect();
	public int LastID(); 
}
