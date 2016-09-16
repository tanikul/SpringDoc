package com.java.doc.util;

import java.util.Date;
import java.util.Locale;

public class UtilDateTime {
	
	public static String getCurrentDate() {
		//Locale locale = new Locale("th", "TH");
		//Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
		return format.format(new Date());
	}
	
	public static Integer getCurrentYear() {
		//Locale locale = new Locale("th", "TH");
		//Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy", new Locale("th", "TH"));
		return Integer.valueOf(format.format(new Date()));
	}
	
	public static String convertToDateTH(Date date) {
		if(date == null) return "";
		//Locale locale = new Locale("th", "TH");
		//Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
		return format.format(date);
	}
}
