package com.java.doc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public static Date convertDateThaiToDate(String strDate){
		Date startDate = null;
		String[] arr = new String[3];
	    if(strDate != null){
	        arr = strDate.split("/");
	        if(arr.length > 2){
	        	String day = (arr[0].length() > 1) ? arr[0] : "0" + arr[0];
	        	String month = (arr[1].length() > 1) ? arr[1] : "0" + arr[1];
	        	int years = Integer.parseInt(arr[2]) - 543;
	        	String startDateString = day + "/" + month + "/" + years;
	            DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
	            try {
	                startDate = df.parse(startDateString);
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return startDate;
	}
	
}
