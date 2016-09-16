package com.java.doc.service;

import java.util.HashMap;
import java.util.List;

import com.java.doc.model.TypeSecret;

public interface TypeSecretService {

	public List<TypeSecret> listTypeSecret() throws Exception;

	HashMap<Integer, String> SelectSecret() throws Exception;
	
	public String getTypeSecretById(Integer id) throws Exception;
}
