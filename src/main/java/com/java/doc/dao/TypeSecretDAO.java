package com.java.doc.dao;

import java.util.HashMap;
import java.util.List;

import com.java.doc.model.TypeSecret;

public interface TypeSecretDAO {
	
	public List<TypeSecret> listTypeSecret() throws Exception;

	public HashMap<Integer, String> SelectSecret() throws Exception;
	
	String getTypeSecretById(Integer id) throws Exception;
}
