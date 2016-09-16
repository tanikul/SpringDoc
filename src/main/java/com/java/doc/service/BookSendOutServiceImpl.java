package com.java.doc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.BookSendOutDAO;
import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.util.TableSorter;
import com.java.doc.validator.BookSendOutValidator;

@Service
public class BookSendOutServiceImpl implements BookSendOutService {

	private BookSendOutDAO sendout;
	
	public BookSendOutDAO getSendout() {
		return sendout;
	}

	public void setSendout(BookSendOutDAO sendout) {
		this.sendout = sendout;
	}

	@Override
	@Transactional
	public boolean saveBookOut(BookSendOut sendout) {
		return this.sendout.merge(sendout);
	}

	@Override
	@Transactional
	public BookSendOut convert(BookSendOutValidator send) {
		BookSendOut old = new BookSendOut();
		old.setBsDate(send.getBsDate());
		old.setBsDivision(send.getBsDivision());
		old.setBsId(send.getBsId());
		old.setBsImage(send.getBsImage());
		old.setBsNum(send.getBsNum());
		old.setBsPcode(send.getBsPcode());
		old.setBsPlace(send.getBsPlace());
		old.setBsRdate(send.getBsRdate());
		old.setBsRemark(send.getBsRemark());
		old.setBsStatus(send.getBsStatus());
		old.setBsSubject(send.getBsSubject());
		old.setBsTo(send.getBsTo());
		old.setBsTypeQuick(send.getBsTypeQuick());
		old.setBsTypeSecret(send.getBsTypeSecret());
		old.setBsYear(send.getBsYear());
		return old;
	}

	@Override
	@Transactional
	public List<BookSendOut> listSendOut() {
		return sendout.listSendOut();
	}

	@Override
	public BookSendOutTable ListPageSendOut(TableSorter table) {
		return sendout.ListPageSendOut(table);
	}

	@Override
	public int CountSelect() {
		return sendout.CountSelect();
	}

	@Override
	public int LastID() {
		return sendout.LastID();
	}

	@Override
	public List<Integer> getYear() {
		return sendout.getYear();
	}

	@Override
	public boolean saveOrUpdate(BookSendOut recive) {
		return sendout.saveOrUpdate(recive);
	}

	@Override
	public boolean merge(BookSendOut recive) {
		return sendout.merge(recive);
	}

	@Override
	public Integer getNextBsNum(int bsYear) {
		return sendout.getNextBsNum(bsYear);
	}

	@Override
	public BookSendOut getDataFromId(int id) {
		return sendout.getDataFromId(id);
	}

	@Override
	public String delete(int id) {
		return sendout.delete(id);
	}

}
