package com.java.doc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class Constants {
	
	public final static String OBJECT_NAME_BOOK_RECIVE_OUT = "BOOK_RECIVE_OUT";
	public final static String OBJECT_NAME_BOOK_SEND_OUT = "BOOK_SEND_OUT";
	public static LinkedHashMap<String, String> ROLES;
	public final static String[] PREFIXS = {"นาย", "นาง", "นางสาว"};
	public static Map<Integer, String> STATUS;
	public static String SUCCESS = "SUCCESS";
	public static String FAIL = "FAIL";
	
	public static LinkedHashMap<String, String> getRoles() {
		ROLES = new LinkedHashMap<String, String>();
		ROLES.put("ADMIN", "เจ้าหน้าที่ดูแลระบบ");
		ROLES.put("BOARD", "ผู้บริหาร");
		ROLES.put("DEPARTMENT", "เจ้าหน้าที่รับ-ส่งหนังสือ ของสำนัก");
		ROLES.put("GROUP", "เจ้าหน้าที่รับ-ส่งหนังสือ ของฝ่าย/กลุ่มงาน");
		//ROLES.put("SECTION", "เจ้าหน้าที่รับ-ส่งหนังสือ ของกลุ่มงาน");
		ROLES.put("USER", "คนรับมอบงาน");
		return ROLES;
	}

	public static Map<Integer, String> getStatus() {
		STATUS = new HashMap<Integer, String>();
		STATUS.put(1, "DEPARTMENT");
		STATUS.put(2, "GROUP");
		STATUS.put(3, "USER");
		STATUS.put(4, "SUCCESS");
		return STATUS;
	}
	
	public String getProperty(String key) throws IOException {
		String result = "";
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			if (inputStream != null) {
				prop.load(inputStream);
				result = prop.getProperty(key);
			} 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}
