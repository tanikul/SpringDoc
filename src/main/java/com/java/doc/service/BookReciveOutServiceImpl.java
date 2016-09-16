package com.java.doc.service;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.BookReciveOutDAO;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.util.TableSorter;

@Service
public class BookReciveOutServiceImpl implements BookReciveOutService {

	private BookReciveOutDAO reciveout;
	
	public void setReciveout(BookReciveOutDAO reciveout) {
		this.reciveout = reciveout;
	}

	public BookReciveOutDAO getReciveout() {
		return reciveout;
	}

	@Override
	@Transactional
	public List<BookReciveOut> loadReciveOut() {
		return reciveout.listRecive();
	}

	@Override
	@Transactional
	public long Count() {
		return reciveout.Count();
	}

	@Override
	public BookReciveOutTable ListPageRecive(TableSorter table) {
		return reciveout.ListPageRecive(table);
	}

	@Override
	public int CountSelect() {
		return reciveout.CountSelect();
	}

	@Override
	public boolean SaveReciveOut(BookReciveOut recive) {
		//return reciveout.Save(recive);
		return reciveout.merge(recive);
	}

	@Override
	public JRDataSource getDataRecivePDF() {
		return reciveout.getDataRecivePDF();
	}

	@Override
	public BookReciveOut getDataFromId(int id) {
		return reciveout.getDataFromId(id);
	}

	@Override
	public int LastID() {
		return reciveout.LastID();
	}

	@Override
	public List<Integer> getYear() {
		return reciveout.getYear();
	}

	@Override
	public Integer getNextBrNum(int brYear) {
		return reciveout.getNextBrNum(brYear);
	}

	@Override
	public String delete(int id) {
		return reciveout.delete(id);
	}
}
