package com.java.doc.service;

import java.util.List;

import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.util.TableSorter;

import net.sf.jasperreports.engine.JRDataSource;

public interface BookReciveOutService {

	public List<BookReciveOut> loadReciveOut();
	public long Count();
	public BookReciveOutTable ListPageRecive(TableSorter table);
	public int CountSelect();
	public boolean SaveReciveOut(BookReciveOut recive);
	public JRDataSource getDataRecivePDF();
	public BookReciveOut getDataFromId(int id);
	public int LastID();
	public Integer getNextBrNum(int brYear);
	public List<Integer> getYear();
	public String delete(int id);
}
