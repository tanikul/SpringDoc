package com.java.doc.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.TypeQuickDAO;
import com.java.doc.model.TypeQuick;

@Service("typeQuickService")
public class TypeQuickServiceImpl implements TypeQuickService {

	@Autowired
	@Qualifier("typeQuickDao")
	private TypeQuickDAO type;

	@Override
	@Transactional
	public List<TypeQuick> listTypeQuick() throws Exception {
		return this.type.listTypeQuick();
	}

	@Override
	@Transactional
	public HashMap<Integer, String> SelectQuick() throws Exception {
		return this.type.SelectQuick();
	}

	@Override
	public String getTypeQuickById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return type.getTypeQuickById(id);
	}
}
