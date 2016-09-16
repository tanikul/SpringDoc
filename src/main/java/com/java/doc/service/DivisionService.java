package com.java.doc.service;

import java.util.List;
import java.util.Map;

import com.java.doc.model.Divisions;

public interface DivisionService {

	public List<Divisions> listDivision();
	public Map<Integer, String> selectDivision();
}
