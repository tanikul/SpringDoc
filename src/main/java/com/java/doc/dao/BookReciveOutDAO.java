package com.java.doc.dao;
// master 5
import java.util.List;

import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.util.TableSorter;

import net.sf.jasperreports.engine.JRDataSource;

public interface BookReciveOutDAO {

	public List<BookReciveOut> listRecive();
	public long Count();
	public BookReciveOutTable ListPageRecive(TableSorter table);
	public int CountSelect();
	public boolean Save(BookReciveOut recive);
	public boolean saveOrUpdate(BookReciveOut recive);
	public boolean merge(BookReciveOut recive);
	public JRDataSource getDataRecivePDF();
	public BookReciveOut getDataFromId(int id);
	public int LastID();
	public Integer getNextBrNum(int brYear);
	public List<Integer> getYear();
	public String delete(int id);
}
