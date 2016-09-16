package com.java.doc.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.TypeSecretDAO;
import com.java.doc.model.TypeSecret;

@Service
public class TypeSecretServiceImpl implements TypeSecretService {

	private TypeSecretDAO secret;
	
	public void setSecret(TypeSecretDAO secret) {
		this.secret = secret;
	}

	@Transactional
	@Override
	public List<TypeSecret> listTypeSecret() throws Exception{
		return secret.listTypeSecret();
	}
	
	@Override
	@Transactional
	public HashMap<Integer, String> SelectSecret() throws Exception {
		return this.secret.SelectSecret();
	}

	@Override
	public String getTypeSecretById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return secret.getTypeSecretById(id);
	}
}
