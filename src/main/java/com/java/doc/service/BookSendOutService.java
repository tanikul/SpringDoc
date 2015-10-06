package com.java.doc.service;

import java.util.List;

import com.java.doc.model.BookSendOut;
import com.java.doc.util.TableSorter;
import com.java.doc.validator.BookSendOutValidator;

public interface BookSendOutService {
	
	public void saveBookOut(BookSendOut sendout);
	public BookSendOut convert(BookSendOutValidator send);
	public List<BookSendOut> listSendOut();
	public List<BookSendOut> ListPageSendOut(TableSorter table);
	public int CountSelect();
	public int LastID();
}
