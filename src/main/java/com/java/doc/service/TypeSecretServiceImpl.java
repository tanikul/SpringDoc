package com.java.doc.service;

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
	public List<TypeSecret> listTypeSecret() {
		return secret.listTypeSecret();
	}

}
