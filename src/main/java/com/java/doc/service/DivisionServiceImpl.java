package com.java.doc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.java.doc.dao.DivisionDAO;
import com.java.doc.model.Divisions;

@Service("divisionService")
public class DivisionServiceImpl implements DivisionService {

	@Autowired
	@Qualifier("divisionDao")
	private DivisionDAO divisions;

	@Override
	public List<Divisions> listDivision() {
		return divisions.listDivision();
	}

	@Override
	public Map<Integer, String> selectDivision() {
		return divisions.selectDivision();
	}

	@Override
	public Divisions getDivisionByCode(String code) {
		// TODO Auto-generated method stub
		return divisions.getDivisionByCode(code);
	}

	
}
