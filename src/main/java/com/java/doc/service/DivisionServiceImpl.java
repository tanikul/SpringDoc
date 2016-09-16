package com.java.doc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.DivisionDAO;
import com.java.doc.model.Divisions;

@Service
public class DivisionServiceImpl implements DivisionService {

	private DivisionDAO divisions;
	
	public void setDivisions(DivisionDAO divisions) {
		this.divisions = divisions;
	}

	@Override
	@Transactional
	public List<Divisions> listDivision() {
		return divisions.listDivision();
	}

	@Override
	@Transactional
	public Map<Integer, String> selectDivision() {
		return divisions.selectDivision();
	}

	
}
