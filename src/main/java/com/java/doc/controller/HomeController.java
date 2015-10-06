package com.java.doc.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookSendOut;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.BookSendOutService;
import com.java.doc.util.TableSorter;
import com.java.doc.validator.LoginValidator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private Logger logger = Logger.getLogger(HomeController.class);
	
	private BookSendOutService sendout;
	private BookReciveOutService reciveout;

	@Autowired(required = true)
	@Qualifier(value = "sendoutservice")
	public void setSendout(BookSendOutService sendout) {
		this.sendout = sendout;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "recive")
	public void setReciveout(BookReciveOutService reciveout) {
		this.reciveout = reciveout;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			/* The user is logged in :) */
			return new ModelAndView("redirect:/home");
		}
		// logger.info("Welcome home! The client locale is {}.", locale);
		ModelAndView model = new ModelAndView();
		model.addObject("login", new LoginValidator());
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public String home() {
		return "home";
	}
	
	@RequestMapping(value = "/loadData", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public  @ResponseBody Map<String, Object> loadData(@RequestBody TableSorter table) throws UnsupportedEncodingException {
		List<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
		List<String> headers = new ArrayList<String>();
		headers.add("ปี");
		headers.add("วันที่รับหนังสือ");
		headers.add("เลขทะเบียนรับ");
		headers.add("ที่");
		headers.add("ลงวันที่");
		headers.add("จาก");
		headers.add("ถึง");
		headers.add("เรื่อง");
		headers.add("หมายเหตุ");
		headers.add("แก้ไข/ลบ");
		Map<String, Object> rs = new HashMap<String, Object>();
		if(table.getSearch().getType().equals("1")){
			List<BookReciveOut> list = reciveout.ListPageRecive(table);
			
			for(BookReciveOut item : list){
				HashMap<String, String> row = new HashMap<String, String>();
				row.put("ปี", item.getBrYear().toString());
				row.put("วันที่รับหนังสือ", item.getBrRdate());
				row.put("เลขทะเบียนรับ", item.getBrNum());
				row.put("ที่", item.getBrPlace());
				row.put("ลงวันที่", item.getBrDate());
				row.put("จาก", item.getBrFrom());
				row.put("ถึง", item.getBrTo());
				row.put("เรื่อง", item.getBrSubject());
				row.put("หมายเหตุ", item.getBrRemark());
				row.put("แก้ไข/ลบ", "<span class='glyphicon glyphicon-pencil' id='edit-"+item.getBrId()+"' style='padding-left:6px;cursor:pointer;' onclick='inter.edit("+ item.getBrId() +");'></span>  <span class='glyphicon glyphicon-trash' style='padding-left:6px;cursor:pointer;'></span>");
				rows.add(row);
			}
			rs.put("total_rows", reciveout.CountSelect());
		}else{
			List<BookSendOut> list = sendout.ListPageSendOut(table);
			for(BookSendOut item : list){
				HashMap<String, String> row = new HashMap<String, String>();
				row.put("ปี", item.getBsYear().toString());
				row.put("วันที่รับหนังสือ", item.getBsRdate());
				row.put("เลขทะเบียนรับ", item.getBsNum());
				row.put("ที่", item.getBsPlace());
				row.put("ลงวันที่", item.getBsDate());
				row.put("จาก", item.getBsFrom());
				row.put("ถึง", item.getBsTo());
				row.put("เรื่อง", item.getBsSubject());
				row.put("หมายเหตุ", item.getBsRemark());
				row.put("แก้ไข/ลบ", "<span class='glyphicon glyphicon-pencil' id='edit-"+item.getBsId()+"' style='padding-left:6px;cursor:pointer;'></span>  <span class='glyphicon glyphicon-trash' style='padding-left:6px;cursor:pointer;'></span>");
				rows.add(row);
			}
			rs.put("total_rows", sendout.CountSelect());
		}
		rs.put("headers", headers);
		rs.put("rows", rows);
		return rs;
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView doSalesReportPDF(ModelAndView modelAndView) 
	{
//		List<BookReciveOut> dataprovider = reciveout.loadReciveOut();
//		List<BookReciveOut> datasource = new ArrayList<BookReciveOut>();
//		int j = 0;
//		for(BookReciveOut i : dataprovider){
//			if(j == 10) break;
//			datasource.add(i);
//			j++;
//		}
//		Map<String,Object> parameterMap = new HashMap<String,Object>();
//		parameterMap.put("datasource", datasource);
//		modelAndView = new ModelAndView("pdfReport", parameterMap);
		JRDataSource datasource  = reciveout.getDataRecivePDF();
		Map<String,Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("datasource", datasource);
		modelAndView = new ModelAndView("pdfReport", parameterMap);
		
		
		return modelAndView;
	}
}
