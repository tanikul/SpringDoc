package com.java.doc.dao;

import java.util.List;
import java.util.Map;

import com.java.doc.model.Divisions;

public interface DivisionDAO {

	public List<Divisions> listDivision();
	public Map<Integer, String> selectDivision();
}
