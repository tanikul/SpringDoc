package com.java.doc.dao;

import java.util.HashMap;
import java.util.List;

import com.java.doc.model.TypeQuick;

public interface TypeQuickDAO {

	List<TypeQuick> listTypeQuick() throws Exception;
	HashMap<Integer, String> SelectQuick() throws Exception;
	String getTypeQuickById(Integer id) throws Exception;
}
